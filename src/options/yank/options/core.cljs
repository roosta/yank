(ns yank.options.core
  (:require [goog.events :as events]
            [goog.object :as gobj]
            [clojure.walk :as w]
            [defaults]
            [clojure.string :as string]
            [goog.dom :as dom])
  (:require-macros [utils.logging :as d]))

;; extend js/RegExp to be callable
(extend-type js/RegExp
  cljs.core/IFn
  (-invoke ([this s] (re-matches this s))))

(def options (atom defaults/options))

(defn save-options
  "save options Takes either an event object and options map or only options"
  ([e options] (let [sync (gobj/getValueByKeys js/browser "storage" "sync")
                     el (dom/getElement "keybind-input")]
                 (.set sync (clj->js {:yank options}))
                 (.preventDefault e)))
  ([options] (let [sync (gobj/getValueByKeys js/browser "storage" "sync")]
               (.set sync (clj->js {:yank options})))))

(defn set-input-value
  [v]
  (let [el (dom/getElement "keybind-input")]
    (gobj/set el "value" v)))

(defn restore-options
  "Get options map and reset state atom with fetched value"
  []
  (let [sync (gobj/getValueByKeys js/browser "storage" "sync")]
    (-> (.get sync "yank")
        (.then (fn [resp]
                 (if-let [result (w/keywordize-keys (js->clj (gobj/get resp "yank")))]
                   (do
                     (set-input-value (-> result :keybind :composed))
                     (reset! options result))
                   (set-input-value (-> defaults/options :keybind :composed))))
               (fn [error]
                 (d/error "Failed to restore options: " error))))))
(defn handle-keydown
  "handle valid keybinds and reset state atom"
  [e]
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
        (set-input-value composed)
        (swap! options assoc :keybind {:keycode keycode
                                       :key key
                                       :alt? alt?
                                       :shift? shift?
                                       :ctrl? ctrl?
                                       :composed composed})))))

(defn handle-reset
  "Reset value in state and input field"
  [e]
  (.preventDefault e)
  (set-input-value (-> defaults/options :keybind :composed))
  (reset! options defaults/options)
  (save-options defaults/options))

(defn init!
  []
  (d/log "opts init!")
  (let [form (dom/getElement "keybind-form")
        input (dom/getElement "keybind-input")]
    (restore-options)
    (events/listen form (.-KEYDOWN events/EventType) handle-keydown)
    (events/listen form (.-RESET events/EventType) handle-reset)
    (events/listen form (.-SUBMIT events/EventType) #(save-options % @options))))
