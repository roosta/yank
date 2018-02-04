(ns yank.core
  (:require [garden.def :refer [defstylesheet defstyles]]
            [garden.units :refer [px]]))

(defstyles screen
  [:body
   {:font "caption"
    :color "WindowText"
    :text-align "center"
    :background-color "Background"}]
  [:h3 {:display "flex"
        :justify-content "space-between"
        :align-items "center"
        :flex "0 1 100%"}]
  [:.bracket {:font-size (px 24)}]
  )
