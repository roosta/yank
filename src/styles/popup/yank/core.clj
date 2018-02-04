(ns yank.core
  (:require [garden.def :refer [defstylesheet defstyles]]
            [garden.units :refer [px]]))

;; Change defstylesheet to defstyles.
(defstyles screen
  [:body
   {:font-family "sans-serif"
    :font-size (px 16)
    :line-height 1.5}])
