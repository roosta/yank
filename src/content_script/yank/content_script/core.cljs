(ns yank.content-script.core
  (:require [goog.object :as gobj]
            [clojure.walk :as w]
            [shared.options :refer [defaults fetch-options]]
            [js.mousetrap]
            [clojure.string :as string])
  (:require-macros [shared.logging :as d]))

;; for extern inference. Better waringings
(set! *warn-on-infer* true)

(def options (atom defaults))

(defn send-message
  "Sends a message using browser runtime
  Gets handled in background script"
  [e]
  (let [^js/browser runtime (gobj/get js/browser "runtime")]
    (.sendMessage runtime #js {:action (:action @options)})))

(defn watcher
  "Watch state atom so that it'll send a message on change"
  [k r old new]
  (let [new-keybind (-> new :keybind :composed)
        old-keybind (-> old :keybind :composed)]
    (when (not= old-keybind new-keybind)
      (.unbind js/Mousetrap old-keybind)
      (.bind js/Mousetrap new-keybind send-message))))

(defn on-storage-change
  [resp]
  (when-let [new (w/keywordize-keys (js->clj (gobj/getValueByKeys resp "yank" "newValue")))]
    (reset! options new)))

(defn init!
  []
  (d/log "content init!")
  (.bind js/Mousetrap (-> defaults :keybind :composed) send-message)
  (fetch-options options)
  (.addListener ^js/browser (gobj/getValueByKeys js/browser "storage" "onChanged") on-storage-change)
  (add-watch options :options watcher))
