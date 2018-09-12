(ns yank.core
  (:require [garden.def :refer [defstylesheet defstyles]]
            [garden.units :refer [px]]))

(defstyles screen
  [:body
   {:color "WindowText"
    :font "caption"
    :display "flex"
    :margin 0
    :min-height "100vh"
    :justify-content "center"
    :align-items "center"
    :background-color "Window"
    :line-height 1.5}]
  [:.bracket {:font-size (px 24)}]
  [:h3 {:display "flex"
        :justify-content "space-between"
        :align-items "center"
        :flex "0 1 100%"}]
  [:.input {:width "100%"}]
  [:#options-form {:display "flex"
                   :width (px 180)
                   :flex-wrap "wrap"}]
  [:.block {:display "flex"
            :margin-bottom (px 10)
            :flex-wrap "wrap"
            :flex "0 1 100%"}]
  [:#buttons {:justify-content "space-between"}]

  [:.label {:flex "0 1 100%"}]
  )
