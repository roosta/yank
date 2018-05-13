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
        html (slurp "test/html/yank/head.html")
        result (core/head {:component "test"
                           :title "test-title"})]
    (testing "Testing head html and hiccup form"
      (is (= result form))
      (is (= (html5 result) html)))))

(deftest popup-body
  (let [html (slurp "test/html/yank/popup-body.html")
        form [:body
              [:h3
               [:span {:class "bracket" :id "left-backet"} "["]
               "Yank format"
               [:span {:class "bracket" :id "right-bracket"} "]"]]
              [:select
               {:class "input" :id "format-select"}
               (list
                [:option {:value "org"} "Org-mode"]
                [:option {:value "md"} "Markdown"]
                [:option {:value "textile"} "Textile"]
                [:option {:value "asciidoc"} "AsciiDoc"]
                [:option {:value "rest"} "reStructuredText"]
                [:option {:value "html"} "HTML"])]
              (list [:script
                     {:type "text/javascript"
                      :src (URI. "js/popup/goog/base.js")}])
              (list [:script
                     {:type "text/javascript"
                      :src (URI. "js/popup/cljs_deps.js")}])
              (list [:script
                     {:type "text/javascript"
                      :src (URI. "setup.js")}])
              (list [:script
                     {:type "text/javascript"
                      :src (URI. "popup.js")}])]
        result (core/popup-body)]
    (testing "Testing popup-body html and hiccup form"
      (is (= result form))
      (is (= (html5 result) html)))))

(deftest options-body
  (let [form [:body
              [:form
               {:id "options-form"}
               [:h3
                [:span {:class "bracket", :id "left-backet"} "["]
                "Yank options"
                [:span {:class "bracket", :id "right-bracket"} "]"]]
               [:div
                {:class "block", :id "keybinding"}
                [:label {:class "label"} "Keybinding"]
                [:input {:class "input", :type "text", :id "keybind-input"}]]
               [:div
                {:class "block", :id "format"}
                [:label {:class "label"} "Markup format"]
                [:select
                 {:class "input", :id "format-select"}
                 (list [:option {:value "org"} "Org-mode"]
                       [:option {:value "md"} "Markdown"]
                       [:option {:value "textile"} "Textile"]
                       [:option {:value "asciidoc"} "AsciiDoc"]
                       [:option {:value "rest"} "reStructuredText"]
                       [:option {:value "html"} "HTML"])]]
               [:div
                {:class "block", :id "buttons"}
                [:button {:type "reset"} "Reset"]
                [:button {:type "submit"} "Save"]]]
              (list [:script
                     {:type "text/javascript",
                      :src (URI. "js/options/goog/base.js")}])
              (list [:script
                     {:type "text/javascript",
                      :src (URI. "js/options/cljs_deps.js")}])
              (list [:script
                     {:type "text/javascript",
                      :src (URI. "setup.js")}])
              (list [:script
                     {:type "text/javascript",
                      :src (URI. "options.js")}])]
        result (core/options-body)
        html (slurp "test/html/yank/options-body.html")]
    (testing "Testing options body hiccup form and HTML"
      (is (= result form))
      (is (= (html5 result) html)))))
