(ns save-to-org.options.core
  (:require [goog.events :as events]
            [goog.object :as gobj]
            [goog.dom :as dom])
  (:require-macros [utils.logging :as d]))

(defn save-options!
  [e]
  (let [sync (gobj/getValueByKeys js/browser "storage" "sync")
        el (dom/getElement "keybind-input")
        value (gobj/get el "value")]
    (.set sync #js {:keybind-opt value}))
  (.preventDefault e))

(defn restore-options!
  []
  (let [el (dom/getElement "keybind-input")
        sync (.. js/browser -storage -sync)]
    (-> (.get sync "keybind-opt")
        (.then (fn [resp]
                 (if-let [value (gobj/get resp "keybind-opt")]
                   (gobj/set el "value" value)))
               (fn [error]
                 (d/log error))))))

(defn init!
  []
  (d/log "opts init!")
  (let [form (dom/getElement "keybind-form")]
    (restore-options!)
    (events/listen form (.-SUBMIT events/EventType) save-options!)))
