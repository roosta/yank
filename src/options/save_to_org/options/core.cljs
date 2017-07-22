(ns save-to-org.options.core
  (:require [goog.events :as events]
            [goog.object :as gobj]
            [clojure.walk :as w]
            [clojure.string :as string]
            [goog.dom :as dom])
  (:require-macros [utils.logging :as d]))

;; extend js/RegExp to be callable
(extend-type js/RegExp
  cljs.core/IFn
  (-invoke ([this s] (re-matches this s))))

;; define default if nothing is present in storage
(def default {:action "org"
              :keybind {:keycode 89
                        :key "y"
                        :alt? false
                        :shift? false
                        :ctrl? true
                        :composed "ctrl+y"}})

(def options (atom default))

(defn save-options
  "save options Takes either an event object and options map or only options"
  ([e options] (let [sync (gobj/getValueByKeys js/browser "storage" "sync")
                     el (dom/getElement "keybind-input")]
                 (.set sync (clj->js {:yank options}))
                 (.preventDefault e)))
  ([options] (let [sync (gobj/getValueByKeys js/browser "storage" "sync")]
               (.set sync (clj->js {:yank options})))))

(defn restore-options
  "Get options map and reset state atom with fetched value"
  []
  (let [el (dom/getElement "keybind-input")
        sync (gobj/getValueByKeys js/browser "storage" "sync")]
    (-> (.get sync "yank")
        (.then (fn [resp]
                 (when-let [result (w/keywordize-keys (js->clj (gobj/get resp "yank")))]
                   (gobj/set el "value" (-> result :keybind :composed))
                   (reset! options result)))
               (fn [error]
                 (d/error "Failed to restore options: " error))))))

(defn handle-keydown
  "handle valid keybinds and reset state atom"
  [e el]
  (let [keycode (.-keyCode e)
        key (#"^[a-z1-9]" (string/lower-case (.fromCharCode js/String keycode)))
        alt? (.-altKey e)
        shift? (.-shiftKey e)
        ctrl? (.-ctrlKey e)
        modifier-one (and (or alt? ctrl?) (not (and alt? ctrl?)))
        modifier-two (and modifier-one shift?)]
    (.preventDefault e)
    (when (and key modifier-one)
      (let [raw (remove string/blank? [(when alt? "alt") (when ctrl? "ctrl") (when modifier-two "shift") key])
            composed (string/join "+" raw)]
        (gobj/set el "value" composed)
        (swap! options assoc :keybind {:keycode keycode
                                       :key key
                                       :alt? alt?
                                       :shift? shift?
                                       :ctrl? ctrl?
                                       :composed composed})))))

(defn handle-reset
  "Reset value in state and input field"
  [e el]
  (.preventDefault e)
  (gobj/set el "value" (-> default :keybind :composed))
  (reset! options default)
  (save-options default))

(defn init!
  []
  (d/log "opts init!")
  (let [form (dom/getElement "keybind-form")
        field (dom/getElement "keybind-input")]
    (restore-options)
    (events/listen form (.-KEYDOWN events/EventType) #(handle-keydown % field))
    (events/listen form (.-RESET events/EventType) #(handle-reset % field))
    (events/listen form (.-SUBMIT events/EventType) #(save-options % @options))))
