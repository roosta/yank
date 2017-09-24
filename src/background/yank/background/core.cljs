(ns yank.background.core
  (:require-macros [shared.logging :as d])
  (:require [goog.object :as gobj]
            [clojure.string :as s]
            [shared.options :refer [fetch-options defaults on-storage-change]]
            [goog.events :as events]))

;; for extern inference. Better waringings
(set! *warn-on-infer* true)

(def ^js/browser tabs (gobj/get js/browser "tabs"))
(def ^js/browser runtime (gobj/get js/browser "runtime"))
(def ^js/browser context-menus (gobj/get js/browser "contextMenus"))

(def options (atom defaults))

(defn create-context-menu
  []
  (.create context-menus (clj->js {:id "yank-link"
                                   :title "Yank link to clipboard"
                                   :contexts ["link"]})))
(defn execute-script
  "Execute a script using js/browser.tabs
  'obj' param is a javascript object conforming to this:
  https://developer.mozilla.org/en-US/Add-ons/WebExtensions/API/tabs/executeScript
  optionally takes a tab-id to select which tab to execute script inside
  Returns a js/Promise"
  ([obj]
   (.executeScript tabs (clj->js obj)))
  ([obj tab-id]
   (.executeScript tabs tab-id (clj->js obj))))

(defn load-clipboard-helper
  "load js function defined in clipboard-helper.js"
  [tab-id]
  (-> ^js/Promise (execute-script {:code "typeof copyToClipboard === 'function';"})
      (.then (fn [result]
               (when (or  (not result) (false? (first result)))
                 (execute-script {:file "clipboard-helper.js"} tab-id))))
      (.catch (fn [error]
                (d/error "Failed to load clipboard helper: " error)))))

(defn copy-to-clipboard
  "Copy text to clipboard by injecting the formatted input (text)
  as an argment to the loaded 'copyToClipboard"
  [tab-id text]
  (let [code (str "copyToClipboard(" (.stringify js/JSON text) ");")]
    (-> ^js/Promise (load-clipboard-helper tab-id)
        (.then (fn []
                 (execute-script {:code code} tab-id )))
        (.catch (fn [error]
                  (d/error "Failed to copy text: " error))))))

(defmulti copy-as
  "Dispatch on :action key, from runtime message, passed from content script"
  (fn [message] (:action message))
  :default "org")

(defmethod copy-as "org"
  ^{:doc "Format URL and title of current tab to org link format"}
  [{:keys [tab-id title url]}]
  (let [escape #(s/escape % {\[  "{", \] "}"})
        text (str "[[" url "][" (escape title) "]]")]
    (copy-to-clipboard tab-id text)))

(defmethod copy-as "md"
  ^{:doc "Format URL and title of current tab to md link format"}
  [{:keys [tab-id title url]}]
  (let [escape #(s/escape % {\[  "\\[", \] "\\]"})
        text (str "[" (escape title) "](" url ")")]
    (copy-to-clipboard tab-id text)))

(defmethod copy-as "textile"
  ^{:doc "Format URL and title of current tab to textile link format"}
  [{:keys [tab-id title url]}]
  (let [escape #(s/escape % {\"  "\""})
        text (str "\"" title "\":" url)]
    (copy-to-clipboard tab-id text)))

(defmethod copy-as "asciidoc"
  ^{:doc "Format URL and title of current tab to textile link format"}
  [{:keys [tab-id title url]}]
  (let [escape #(s/escape % {\[  "\\[", \] "\\]"})
        text (str url "[" (escape title) "]")]
    (copy-to-clipboard tab-id text)))

(defmethod copy-as "rest"
  ^{:doc "Format URL and title of current tab to reStructuredText link format"}
  [{:keys [tab-id title url]}]
  (let [escape #(s/escape % {\_ "\\_"})
        text (str "`" (escape title) " <" url ">`_")]
    (copy-to-clipboard tab-id text)))

(defmethod copy-as "html"
  ^{:doc "Format URL and title of current tab to HTML link"}
  [{:keys [tab-id title url]}]
  (let [escape #(s/escape % {\< "&lt;", \> "&gt;", \& "&amp;"})
        text (str "<a href=\"" url "\">" (escape title) "</a>")]
    (copy-to-clipboard tab-id text)))

(defn handle-message
  "Handle incoming runtime message, extract info and call copy-as"
  [request sender send-response]
  (when-some [action (gobj/get request "action")]
    (let [tab (gobj/get sender "tab")
          url (gobj/get tab "url")
          tab-id (gobj/get tab "id")
          title (gobj/get tab "title")]
      (copy-as {:action action
                :tab-id tab-id
                :url url
                :title title}))))
(defn handle-click
  []
  (.openOptionsPage runtime))

(defn handle-context
  [info tab]
  (let [url (gobj/get info "linkUrl")
        text (gobj/get info "linkText")]))

(defn fig-reload
  []
  (.reload runtime))

(defn init!
  []
  (d/log "background init!")
  (create-context-menu)
  (fetch-options options)
  (.addListener ^js/browser (gobj/getValueByKeys js/browser "storage" "onChanged") #(on-storage-change options %))
  (.addListener ^js/browser (gobj/getValueByKeys js/browser "browserAction" "onClicked") handle-click)
  (.addListener ^js/browser (gobj/getValueByKeys js/browser "contextMenus" "onClicked") handle-context)
  (.addListener ^js/browser (gobj/get runtime "onMessage") handle-message))
