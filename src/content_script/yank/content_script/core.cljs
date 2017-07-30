(ns yank.content-script.core
  (:require [goog.object :as gobj]
            [clojure.walk :as w]
            [defaults]
            [js.mousetrap]
            [clojure.string :as string])
  (:require-macros [utils.logging :as d]))

;; for extern inference. Better waringings
(set! *warn-on-infer* true)

(def options (atom nil))

(defn fetch-options
  "Handle fetching options"
  []
  (let [^js/browser
        sync (gobj/getValueByKeys js/browser "storage" "sync")]
    (-> ^js/browser
        (.get sync "yank")
        ^js/browser
        (.then (fn [resp]
                 (if-let [result (w/keywordize-keys (js->clj (gobj/get resp "yank")))]
                   (reset! options result)
                   (reset! options defaults/options)))
               (fn [error]
                 (reset! options defaults/options)
                 (d/log "Failed to get options: " error))))))

(defn send-message
  "Sends a message using browser runtime
  Gets handled in background script"
  [e]
  (let [^js/browser runtime (gobj/get js/browser "runtime")]
    (.sendMessage runtime #js {:action (:action @options)})))

(defn watcher
  "Watch state atom so that it'll send a message on change"
  [k r old new]
  (when-let [key-combo (-> new :keybind :composed)]
    (.bind js/Mousetrap key-combo send-message)))

(defn init!
  []
  (d/log "content init!")
  (add-watch options :options watcher)
  (fetch-options))
