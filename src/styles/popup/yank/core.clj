(ns yank.core
  (:require [garden.def :refer [defstylesheet defstyles]]
            [garden.units :refer [px]]))

(defstyles screen
  [:body
   {:font "caption"
    :color "WindowText"
    :padding (px 10)
    :background-color "Background"}]
  [:h3 {:display "flex"
        :justify-content "space-between"
        :align-items "center"}]
  [:.bracket {:font-size (px 24)}])
