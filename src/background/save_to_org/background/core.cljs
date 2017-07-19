(ns save-to-org.background.core
  (:require-macros [utils.logging :as d])
  (:require [goog.object :as gobj]
            [clojure.walk :as w]
            [goog.events :as events]))

(defn escape
  [text]
  (clojure.string/escape text {\< "&lt;", \> "&gt;", \& "&amp;"}))

(defn load-clipboard-helper
  [tab-id]
  (-> (.executeScript (gobj/get js/browser "tabs") #js {:code "typeof copyToClipboard === 'function';"})
      (.then (fn [result]
               (when (or  (not result) (false? (first result)))
                 (.executeScript (gobj/get js/browser "tabs") tab-id #js {:file "clipboard-helper.js"}))))
      (.catch (fn [error]
                (d/error "Failed to load clipboard helper: " error)))))

(defn copy-to-clipboard
  [tab-id text]
  (let [code (str "copyToClipboard(" (.stringify js/JSON text) ");")]
    (-> (load-clipboard-helper tab-id)
        (.then (fn []
                 (.executeScript (gobj/get js/browser "tabs") tab-id #js {:code code})))
        (.catch (fn [error]
                  (d/error "Failed to copy text: " error))))))

(defmulti copy-as
  (fn [message] (:action message))
  :default "org")

(defmethod copy-as "org"
  [{:keys [tab-id title url]}]
  (let [text (str "[[" url "][" title "]]")]
    (copy-to-clipboard tab-id text)))

(defmethod copy-as "md"
  [{:keys [tab-id title url]}]
  (let [text (str "[" title "](" url ")")]
    (copy-to-clipboard tab-id text)))

(defn handle-click
  []
  (.openOptionsPage (gobj/get js/browser "runtime")))

(defn handle-message
  [request sender send-response]
  (when-some [action (gobj/get request "action")]
    (let [tab (gobj/get sender "tab")
          url (escape (gobj/get tab "url"))
          tab-id (gobj/get tab "id")
          title (escape (gobj/get tab "title"))]
      (copy-as {:action action
                :tab-id tab-id
                :url url
                :title title}))))

(defn init!
  []
  (d/log "background init!")
  (.addListener (gobj/getValueByKeys js/browser "runtime" "onMessage") handle-message)
  (.addListener (gobj/getValueByKeys js/browser "browserAction" "onClicked") handle-click)
  #_(.addListener (gobj/getValueByKeys js/browser "commands" "onCommand") get-active-tab))
