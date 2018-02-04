(ns yank.core
  (:require [garden.def :refer [defstylesheet defstyles]]
            [garden.units :refer [px]]))

(defstyles screen
  [:body
   {:font "caption"
    :color "WindowText"
    :text-align "center"
    :font-size (px 16)
    :line-height 1.5}])
