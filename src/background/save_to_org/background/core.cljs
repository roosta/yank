(ns save-to-org.background.core
  (:require-macros [utils.logging :as l])
  (:require [goog.object :as gobj]
            [goog.events :as events]))

(defn escape
  [text]
  (clojure.string/escape text {\< "&lt;", \> "&gt;", \& "&amp;"}))

(defn load-clipboard-helper
  [tab-id]
  (-> (.executeScript (gobj/get js/browser "tabs") #js {:code "typeof copyToClipboard === 'function';"})
      (.then (fn [result]
               (when (or (not (empty? result)) (not (first result)))
                 (.executeScript (gobj/get js/browser "tabs") #js {:file "clipboard-helper.js"}))))))

(defn on-active-tab
  [tabs]
  (when-some [current-tab (first tabs)]
    (let [tab-id (gobj/get current-tab "id")
          url    (escape (gobj/get current-tab "url"))
          title  (escape (gobj/get current-tab "title"))
          result (str "[[" url "][" title "]]")
          code (str "copyToClipboard(" (.stringify js/JSON result) ");")]
      (-> (load-clipboard-helper tab-id)
          (.then (fn []
                   (.executeScript (gobj/get js/browser "tabs") tab-id #js {:code code})))
          (.catch (fn [error]
                    (l/error "Failed to copy text: " error)))))))

(defn get-active-tab
  [a b c]
  (let [getting-active-tab (.query js/browser.tabs #js {:active true :currentWindow true})]
    (.then getting-active-tab on-active-tab))
  )

(defn init!
  []
  (.addListener (gobj/getValueByKeys js/browser "commands" "onCommand") get-active-tab))
