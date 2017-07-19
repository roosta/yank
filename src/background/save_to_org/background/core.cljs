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
                 (.executeScript (gobj/get js/browser "tabs") #js {:file "clipboard-helper.js"}))))))

(defn on-active-tab
  [tabs]
  (when-some [current-tab (first tabs)]
    (let [tab-id   (gobj/get current-tab "id")
          url      (escape (gobj/get current-tab "url"))
          title    (escape (gobj/get current-tab "title"))
          org-text (str "[[" url "][" title "]]")
          code     (str "copyToClipboard(" (.stringify js/JSON org-text) ");")]
      (-> (load-clipboard-helper tab-id)
          (.then (fn []
                   (.executeScript (gobj/get js/browser "tabs") tab-id #js {:code code})))
          (.catch (fn [error]
                    (d/error "Failed to copy text: " error)))))))

(defn get-active-tab
  [a b c]
  (let [getting-active-tab (.query js/browser.tabs #js {:active true :currentWindow true})]
    (.then getting-active-tab on-active-tab)))

(defn handle-click
  []
  (.openOptionsPage (gobj/get js/browser "runtime")))

(defmulti copy-as
  (fn [param] param)
  :default "org")

(defmethod copy-as "org"
  [_]
  (d/log "arrived at copy-as-org"))

(defmethod copy-as "md"
  [_]
  (d/log "arrived at copy-as-md"))

(defn handle-message
  [request sender send-response]
  (when-some [action (gobj/get request "action")]
    (copy-as action)))

(defn init!
  []
  (d/log "background init!")
  (.addListener (gobj/getValueByKeys js/browser "runtime" "onMessage") handle-message)
  (.addListener (gobj/getValueByKeys js/browser "browserAction" "onClicked") handle-click)
  #_(.addListener (gobj/getValueByKeys js/browser "commands" "onCommand") get-active-tab))
