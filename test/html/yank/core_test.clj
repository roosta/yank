(ns yank.core_test
  (:import java.net.URI)
  (:require [clojure.test :as t :refer [deftest testing is]]
            [hiccup.page :refer [include-js include-css html5 doctype]]
            [yank.core :as core]))

(deftest head
  (let [form  [:head
               [:meta {:charset "utf-8"}]
               [:title "test-title"]
               (list [:link
                      {:type "text/css"
                       :href (URI. "/css/test.css")
                       :rel "stylesheet"}])]
        html "<!DOCTYPE html>\n<html><head><meta charset=\"utf-8\"><title>test-title</title><link href=\"/css/test.css\" rel=\"stylesheet\" type=\"text/css\"></head></html>"
        result (core/head {:component "test"
                           :title "test-title"})]
    (testing "Testing head hiccup form"
      (is (= result form))
      (is (= (html5 result) html)))))
