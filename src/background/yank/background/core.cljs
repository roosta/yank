(ns yank.background.core
  (:require-macros [utils.logging :as d])
  (:require [goog.object :as gobj]
            [clojure.walk :as w]
            [clojure.string :as s]
            [goog.events :as events]))

(defn execute-script
  "Execute a script using js/browser.tabs
  'obj' param is a javascript object conforming to this:
  https://developer.mozilla.org/en-US/Add-ons/WebExtensions/API/tabs/executeScript
  optionally takes a tab-id to select which tab to execute script inside
  Returns a js/Promise"
  ([obj]
   (.executeScript (gobj/get js/browser "tabs") (clj->js obj)))
  ([obj tab-id]
   (.executeScript (gobj/get js/browser "tabs") tab-id (clj->js obj))))

(defn load-clipboard-helper
  "load js function defined in clipboard-helper.js"
  [tab-id]
  (-> (execute-script {:code "typeof copyToClipboard === 'function';"})
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
    (-> (load-clipboard-helper tab-id)
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
  (.openOptionsPage (gobj/get js/browser "runtime")))

(defn fig-reload
  []
  (let [runtime (gobj/get js/browser "runtime")]
    (.reload runtime)))

(defn init!
  []
  (d/log "background init!")
  (.addListener (gobj/getValueByKeys js/browser "browserAction" "onClicked") handle-click)
  (.addListener (gobj/getValueByKeys js/browser "runtime" "onMessage") handle-message))
