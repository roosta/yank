(ns save-to-org.background.core
  (:require-macros [utils.logging :as log])
  (:require [goog.object :as gobj]
            [goog.events :as events]))

(defn on-copy
  [event text]
  (log/log "got to on-copy")
  (.removeEventListener js/document "copy" #(on-copy % text) true)

  ;; Hide the event from the page to prevent tampering.
  (.stopImmediatePropagation event)

  ;; Overwrite the clipboard content.
  (.preventDefault event)
  (.setData (gobj/get event "clipboardData") "text/plain" text))

(defn copy-to-clipboard
  "This function must be called in a visible page, such as a browserAction popup
  or a content script. Calling it in a background page has no effect!"
  [text]
  (log/log "got to copy-to-clipboard")
  ;; (events/listen js/document "copy" #(log/log "copy event fired!") true)
  (.addEventListener js/document "copy" #(log/log "copy event fired!") true)

  ;; Requires the clipboardWrite permission, or a user gesture:
  (.execCommand js/document "copy"))

(defn escape
  [text]
  (clojure.string/escape text {\< "&lt;", \> "&gt;", \& "&amp;"}))

(defn asd
  []
  (.log js/console "Yay!'"))

(defn on-active-tab
  [tabs]
  (when-some [current-tab (first tabs)]
    (let [tab-id (gobj/get current-tab "id")
          url    (escape (gobj/get current-tab "url"))
          title  (escape (gobj/get current-tab "title"))
          result (str "[[" url "][" title "]]")]
      (-> (.executeScript (gobj/get js/browser "tabs") tab-id (clj->js {:code (str (copy-to-clipboard result))}))
          (.catch (fn [error]
                    (log/error (str "Failed to copy text: " error))))))))

(defn get-active-tab
  [a b c]
  (let [getting-active-tab (.query js/browser.tabs #js {:active true :currentWindow true})]
    (.then getting-active-tab on-active-tab))
  )

(defn init!
  []
  (log/log "background init!")
  (.addListener (gobj/getValueByKeys js/browser "commands" "onCommand") get-active-tab))
