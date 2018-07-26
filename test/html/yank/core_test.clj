(ns yank.core-test
  (:import java.net.URI)
  (:require [clojure.test :as t :refer [deftest testing is]]
            [hiccup.page :refer [include-js include-css html5 doctype]]
            [yank.core :as core]))
