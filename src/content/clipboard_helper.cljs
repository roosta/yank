(ns clipboard-helper
  (:require [goog.object :as gobj]))

(defn- on-copy
  [event text]
  (.removeEventListener js/document "copy" #(on-copy % text) true)

  ;; Hide the event from the page to prevent tampering.
  (.stopImmediatePropagation event)

  ;; Overwrite the clipboard content.
  (.preventDefault event)
  (.setData (gobj/get event "clipboardData") "text/plain"))

(defn copy-to-clipboard
  "This function must be called in a visible page, such as a browserAction popup
  or a content script. Calling it in a background page has no effect!"
  [text]
  (.addEventListener js/document "copy" #(on-copy % text) true)

  ;; Requires the clipboardWrite permission, or a user gesture:
  (.execCommand js/document "copy"))
