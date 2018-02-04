(ns yank.core
  (:require [hiccup.page :refer [include-js include-css html5]]
            [environ.core :refer [env]])
  (:gen-class))

(defn head []
  [:head
   [:meta {:charset "utf-8"}]
   (include-css (if (env :dev) "/css/site.css" "/css/site.min.css"))])

(defn -main
  [& args]
  (println (head))
  )
