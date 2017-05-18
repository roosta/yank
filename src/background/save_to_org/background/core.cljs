(ns save-to-org.background.core
  (:require [goog.object :as gobj]))

(defn init!
  []
  (.log js/console "Background init!"))
