(ns yank.core-test
  (:require [clojure.test :as t :refer [deftest testing is are]]
            [hickory.core :refer [parse as-hickory]]
            [hickory.select :as s]
            [yank.core :as core]))

(deftest popup-html
  (testing "head-html using component and title"
    (let [tree (-> (core/popup-html true ".css")
                   parse
                   as-hickory)
          css-path (-> (s/select (s/child (s/tag :head)) tree)
                       first
                       :content
                       last
                       :attrs
                       :href)
          scripts (s/select (s/child (s/tag :script)) tree)
          select (-> (s/select (s/child (s/tag :select)) tree)
                     first
                     :content)]
      (are [result expected] (= result expected)
        css-path "/css/popup.css"
        (count select) 6
        (map (comp :src :attrs) scripts) '("js/popup/goog/base.js" "setup.js" "js/popup/cljs_deps.js" "popup.js")
        (map (comp :value :attrs) select) '("org" "md" "textile" "asciidoc" "rest" "html")
        ))
    )
  )

(deftest background-html

  (testing "background-html using dev profile"
    (let [tree (-> (core/background-html true)
                   parse
                   as-hickory)
          scripts (s/select (s/child (s/tag :script)) tree)
          charset (-> (s/select (s/child (s/tag :head)) tree)
                      first
                      :content
                      first
                      :attrs
                      :charset)]
      (are [result expected] (= result expected)
        charset "utf-8"
        (map (comp :src :attrs) scripts) '("js/background/goog/base.js" "setup.js" "js/background/cljs_deps.js" "background.js"))))

  (testing "Testing background-html using release profile"
    (let [tree (-> (core/background-html false)
                   parse
                   as-hickory)
          scripts (s/select (s/child (s/tag :script)) tree)]
      (are [result expected] (= result expected)
        (count scripts) 1
        (-> (first scripts) :attrs :src) "js/background.js"))))
