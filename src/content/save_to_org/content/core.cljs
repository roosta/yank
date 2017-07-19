(ns save-to-org.content.core
  (:require [goog.object :as gobj]
            [clojure.string :as string])
  (:require-macros [utils.logging :as d]))

(defn init!
  []
  (d/log "content init!"))
