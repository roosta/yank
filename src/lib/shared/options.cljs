(ns shared.options
  (:require-macros [shared.logging :as d])
  (:require [goog.object :as gobj]
            [clojure.walk :as w]))

;; define default if nothing is present in storage
(def defaults {:action "org"
               :keybind {:keycode 89
                         :key "y"
                         :alt? false
                         :meta? false
                         :shift? false
                         :ctrl? true
                         :composed "ctrl+y"}})

(def ^js/browser sync (gobj/getValueByKeys js/browser "storage" "sync"))
(def ^js/browser runtime (gobj/get js/browser "runtime"))

(defn on-storage-change
  [ref resp]
  (when-let [new (w/keywordize-keys (js->clj (gobj/getValueByKeys resp "yank" "newValue")))]
    (reset! ref new)))

(defn restore-options
  "Get options map and reset state atom with fetched value"
  [ref]
  (let [^js/Promise options-promise (.get sync "yank")]
    (.then options-promise
           (fn [resp]
             (when-let [result (w/keywordize-keys (js->clj (gobj/get resp "yank")))]
               (reset! ref result)))
           (fn [error]
             (d/error "Failed to restore options, using defaults. Error: " error)))))

(defn fetch-options
  "Handle fetching options, takes an atom as a param"
  [ref]
  (let [^js/browser
        sync (gobj/getValueByKeys js/browser "storage" "sync")]
    (-> ^js/browser
        (.get sync "yank")
        ^js/browser
        (.then (fn [resp]
                 (if-let [result (w/keywordize-keys (js->clj (gobj/get resp "yank")))]
                   (reset! ref result)))
               (fn [error]
                 (d/log "Failed to get options: " error))))))
