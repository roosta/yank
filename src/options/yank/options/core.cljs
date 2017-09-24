(ns yank.options.core
  (:require [goog.events :as events]
            [goog.object :as gobj]
            [clojure.walk :as w]
            [shared.options :refer [defaults]]
            [clojure.string :as string]
            [goog.dom :as dom])
  (:require-macros [shared.logging :as d]))

;; for extern inference, better warnings
(set! *warn-on-infer* true)

(def options (atom defaults))

;; mac win android linux
(def os (atom nil))

;; Grab url elements since they are statically defined in html
(def elements {:keybind-input (dom/getElement "keybind-input")
               :format-select (dom/getElement "format-select")
               :form (dom/getElement "options-form")})

(def ^js/browser sync (gobj/getValueByKeys js/browser "storage" "sync"))
(def ^js/browser runtime (gobj/get js/browser "runtime"))

(defn get-os
  []
  (let [^js/Promise platform-info (.getPlatformInfo runtime)]
    (.then platform-info
           (fn [resp]
             (reset! os (gobj/get resp "os")))
           (fn [error]
             (d/error "Failed to get os from runtime. Error: " error)))))

(defn save-options
  "save options Takes either an event object and options map or only options"
  ([^js/Event e opts]
   (.set sync (clj->js {:yank opts}))
   (.preventDefault e))
  ([opts]
   (.set sync (clj->js {:yank opts}))))

(defn restore-options
  "Get options map and reset state atom with fetched value"
  []
  (let [^js/Promise options-promise (.get sync "yank")]
    (.then options-promise
           (fn [resp]
             (when-let [result (w/keywordize-keys (js->clj (gobj/get resp "yank")))]
               (reset! options result)))
           (fn [error]
             (d/error "Failed to restore options, using defaults. Error: " error)))))

(defn handle-keydown
  "handle valid keybinds and reset state atom"
  [^js/Event e]
  (let [keycode (.-keyCode e)
        key (re-matches #"^[a-z1-9]" (string/lower-case (.fromCharCode js/String keycode)))
        alt? (.-altKey e)
        shift? (.-shiftKey e)
        meta? (.-metaKey e)
        ctrl? (.-ctrlKey e)]
    (.preventDefault e)
    (when key
      (let [alt (when alt? (if (not= @os "mac")
                             "alt"
                             "option"))
            ctrl (when ctrl? "ctrl")
            shift (when shift? "shift")
            meta (when meta? (if (not= @os "mac")
                               "meta"
                               "command"))
            raw (remove string/blank? [alt ctrl shift meta key])
            composed (string/join "+" raw)]
        (swap! options assoc :keybind {:keycode keycode
                                       :key key
                                       :meta? meta?
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
  [^js/Event e]
  (.preventDefault e)
  (reset! options defaults)
  (save-options defaults))

(defn handle-format-change
  "set options :action field on <select> change"
  [e]
  (let [value (gobj/getValueByKeys e "target" "value")]
    (swap! options assoc :action value)))

(defn fig-reload
  []
  (let [runtime ^js/browser (gobj/get js/browser "runtime")]
    (.reload runtime)))

(defn init!
  []
  (d/log "opts init!")
  (add-watch options :input-sync input-sync)
  (restore-options)
  (get-os)
  (events/listen (:keybind-input elements) "keydown" handle-keydown)
  (events/listen (:format-select elements) "change" handle-format-change)
  (events/listen (:form elements) "reset" handle-reset)
  (events/listen (:form elements) "submit" #(save-options % @options)))
