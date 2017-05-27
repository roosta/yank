(ns save-to-org.background.core
  (:require-macros [utils.logging :refer [log info]])
  (:require [goog.object :as gobj]
            [goog.events :as events]))

(defn update-tab
  [tabs]
  (when-let [current-tab (get tabs 0)]
    (let [url (gobj/get current-tab "url")
          title (gobj/get current-tab "title")]
      #_(log (format "[[%1][%2]]" url title))
      (log (str "[[" url "][" title "]]")))))

(defn update-active-tab
  [tabs]
  (let [getting-active-tab (.query js/browser.tabs #js {:active true :currentWindow true})]
    (.then getting-active-tab update-tab)))

(defn init!
  []
  (log "background init!")
  (.addListener js/browser.commands.onCommand update-active-tab))
