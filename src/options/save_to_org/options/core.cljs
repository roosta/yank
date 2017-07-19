(ns save-to-org.options.core
  (:require [goog.events :as events]
            [goog.object :as gobj]
            [clojure.walk :as w]
            [js.mousetrap]
            [clojure.string :as string]
            [goog.dom :as dom])
  (:require-macros [utils.logging :as d]))

(extend-type js/RegExp
  cljs.core/IFn
  (-invoke ([this s] (re-matches this s))))

(def keybind (atom nil))
(def default {:keycode 89
              :key "Y"
              :alt? false
              :shift? false
              :ctrl? true
              :composed "ctrl+y"})

(defn save-options!
  ([e options] (let [sync (gobj/getValueByKeys js/browser "storage" "sync")
                     el (dom/getElement "keybind-input")]
                 (.set sync (clj->js {:keybind-opt options}))
                 (.preventDefault e)))
  ([options] (let [sync (gobj/getValueByKeys js/browser "storage" "sync")]
               (.set sync (clj->js {:keybind-opt options})))))

(defn restore-options!
  []
  (let [el (dom/getElement "keybind-input")
        sync (gobj/getValueByKeys js/browser "storage" "sync")]
    (-> (.get sync "keybind-opt")
        (.then (fn [resp]
                 (when-let [result (w/keywordize-keys (js->clj (gobj/get resp "keybind-opt")))]
                   (gobj/set el "value" (:composed result))
                   (reset! keybind result)))
               (fn [error]
                 (d/log error))))))

(defn handle-keydown!
  [e el]
  (let [keycode (.-keyCode e)
        key (#"^[a-z1-9]" (string/lower-case (.fromCharCode js/String keycode)))
        alt? (.-altKey e)
        shift? (.-shiftKey e)
        ctrl? (.-ctrlKey e)
        modifier-one (and (or alt? ctrl?) (not (and alt? ctrl?)))
        modifier-two (and modifier-one shift?)]
    ;; (d/log (.fromCharCode js/String keycode))
    (.preventDefault e)
    (when (and key modifier-one)
      (let [raw (remove string/blank? [(when alt? "alt") (when ctrl? "ctrl") (when modifier-two "shift") key])
            composed (string/join "+" raw)]
        (gobj/set el "value" composed)
        (reset! keybind {:keycode keycode
                         :key key
                         :alt? alt?
                         :shift? shift?
                         :ctrl? ctrl?
                         :composed composed})))))
(defn handle-reset!
  [e el]
  (.preventDefault e)
  (gobj/set el "value" (:composed default))
  (reset! keybind default)
  (save-options! default))

(defn init!
  []
  (d/log "opts init!")
  (let [form (dom/getElement "keybind-form")
        field (dom/getElement "keybind-input")]
    (restore-options!)
    (events/listen form (.-KEYDOWN events/EventType) #(handle-keydown! % field))
    (events/listen form (.-RESET events/EventType) #(handle-reset! % field))
    (events/listen form (.-SUBMIT events/EventType) #(save-options! % @keybind))))
