(ns user
  (:require [figwheel-sidecar.repl-api :as ra]))

(defn start [] (ra/start-figwheel! "background" "options" "popup" "content-script"))

(defn stop [] (ra/stop-figwheel!))

(defn cljs-repl [build] (ra/cljs-repl build))
