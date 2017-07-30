(ns yank.options.core
  (:require [goog.events :as events]
            [goog.object :as gobj]
            [clojure.walk :as w]
            [defaults.options :refer [defaults]]
            [clojure.string :as string]
            [goog.dom :as dom])
  (:require-macros [utils.logging :as d]))

;; extend js/RegExp to be callable
(extend-type js/RegExp
  cljs.core/IFn
  (-invoke ([this s] (re-matches this s))))

(def options (atom defaults))

(def elements {:keybind-input (dom/getElement "keybind-input")
               :format-select (dom/getElement "format-select")
               :form (dom/getElement "options-form")})

(defn save-options
  "save options Takes either an event object and options map or only options"
  ([e options] (let [sync (gobj/getValueByKeys js/browser "storage" "sync")]
                 (.set sync (clj->js {:yank options}))
                 (.preventDefault e)))
  ([options] (let [sync (gobj/getValueByKeys js/browser "storage" "sync")]
               (.set sync (clj->js {:yank options})))))

(defn restore-options
  "Get options map and reset state atom with fetched value"
  []
  (let [sync (gobj/getValueByKeys js/browser "storage" "sync")]
    (-> (.get sync "yank")
        (.then (fn [resp]
                 (when-let [result (w/keywordize-keys (js->clj (gobj/get resp "yank")))]
                   (reset! options result)))
               (fn [error]
                 (d/error "Failed to restore options, using defaults. Error: " error))))))

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
        (swap! options assoc :keybind {:keycode keycode
                                       :key key
                                       :alt? alt?
                                       :shift? shift?
                                       :ctrl? ctrl?
                                       :composed composed})))))

(defn input-sync
  "Keep input field up to date with options atom"
  [k r old new]
  (gobj/set (:format-select elements) "value" (:action new))
  (gobj/set (:keybind-input elements) "value" (-> new :keybind :composed)))

(defn handle-reset
  "Reset value in state and input field"
  [e]
  (.preventDefault e)
  (reset! options defaults)
  (save-options defaults))

(defn handle-format-change
  "set options :action field on <select> change"
  [e]
  (let [value (gobj/getValueByKeys e "target" "value")]
    (swap! options assoc :action value)))

(defn init!
  []
  (d/log "opts init!")
  (add-watch options :input-sync input-sync)
  (restore-options)
  (events/listen (:keybind-input elements) (.-KEYDOWN events/EventType) handle-keydown)
  (events/listen (:format-select elements) (.-CHANGE events/EventType) handle-format-change)
  (events/listen (:form elements) (.-RESET events/EventType) handle-reset)
  (events/listen (:form elements) (.-SUBMIT events/EventType) #(save-options % @options)))
