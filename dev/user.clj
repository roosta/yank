(ns user
  (:require [figwheel-sidecar.repl-api :as ra]))

(defn start [] (ra/start-figwheel! "background" "options" "popup"))

(defn stop [] (ra/stop-figwheel!))

(defn cljs [build] (ra/cljs-repl build))
