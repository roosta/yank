(ns save-to-org.content-script.core
  (:require [goog.object :as gobj]
            [clojure.walk :as w]
            [js.mousetrap]
            [clojure.string :as string])
  (:require-macros [utils.logging :as d]))

(set! *warn-on-infer* true)
(def keybind (atom nil))

(defn fetch-keybind
  []
  (let [^js/browser
        sync (gobj/getValueByKeys js/browser "storage" "sync")]
    (-> ^js/browser
        (.get sync "keybind-opt")
        ^js/browser
        (.then (fn [resp]
                 (when-let [result (w/keywordize-keys (js->clj (gobj/get resp "keybind-opt")))]
                   (reset! keybind result)))
               (fn [error]
                 (d/log error))))))

(defn send-message
  [e]
  (let [^js/browser runtime (gobj/get js/browser "runtime")]
    (.sendMessage runtime #js {:action "org"})))

(defn watcher
  [k r old new]
  (when-let [key-combo (:composed new)]
    (.bind js/Mousetrap key-combo send-message)))

(defn init!
  []
  (d/log "content init!")
  (add-watch keybind :keybind watcher)
  (fetch-keybind))
