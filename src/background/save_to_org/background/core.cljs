(ns save-to-org.background.core
  (:require-macros [utils.logging :refer [log info]])
  (:require [goog.object :as gobj]
            [goog.events :as events]
            [oops.core :refer [oget oset! ocall oapply ocall! oapply!
                               oget+ oset!+ ocall+ oapply+ ocall!+ oapply!+]]))

(defn update-tab
  [tabs]
  (when-let [current-tab (get tabs 0)]
    (log (oget current-tab :url))))

(defn update-active-tab
  [tabs]
  (let [getting-active-tab (.query js/browser.tabs #js {:active true :currentWindow true})]
    (.then getting-active-tab update-tab)
    ))

(defn init!
  []
  (log "background init!")
  (events/listen js/document (oget events/EventType :WHEEL) #(log %))
  #_(log events/EventType)

  ;; listen to tab URL changes
  #_(.addListener js/browser.tabs.onUpdated update-active-tab)

  ;; listen to tab switching
  #_(.addListener js/browser.tabs.onActivated update-active-tab)

  ;; listen for window switching
  #_(.addListener js/browser.windows.onFocusChanged update-active-tab)

  )
