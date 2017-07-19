(ns save-to-org.content-script.core
  (:require [goog.object :as gobj]
            [js.mousetrap]
            [clojure.string :as string])
  (:require-macros [utils.logging :as d]))

(set! *warn-on-infer* true)

(defn init!
  []
  (.bind js/Mousetrap "4" #(d/log "it works"))
  (d/log "content init!"))
