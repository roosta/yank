(ns yank.core-test
  (:require [clojure.test :as t :refer [deftest testing is are]]
            [hickory.core :refer [parse as-hickory]]
            [hickory.select :as s]
            [yank.core :as core]))

(deftest background-html

    (testing "Testing background-html using dev profile"
      (let [tree (-> (core/background-html true)
                     parse
                     as-hickory)
            scripts (s/select (s/child (s/tag :script)) tree)]
        (are [result expected] (= result expected)
          (count scripts) 4
          (-> (nth scripts 0) :attrs :src) "js/background/goog/base.js"
          (-> (nth scripts 1) :attrs :src) "setup.js"
          (-> (nth scripts 2) :attrs :src) "js/background/cljs_deps.js"
          (-> (nth scripts 3) :attrs :src) "background.js")))

    (testing "Testing background-html using release profile"
      (let [tree (-> (core/background-html false)
                     parse
                     as-hickory)
            scripts (s/select (s/child (s/tag :script)) tree)]
        (are [result expected] (= result expected)
          (count scripts) 1
          (-> (first scripts) :attrs :src) "js/background.js"))))
