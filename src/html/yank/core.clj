(ns yank.core
  (:require [hiccup.page :refer [include-js include-css html5 doctype]]
            [environ.core :refer [env]])
  (:gen-class))

(def formats [{:value "org" :text "Org-mode"}
              {:value "md" :text "Markdown"}
              {:value "textile" :text "Textile"}
              {:value "asciidoc" :text "AsciiDoc"}
              {:value "rest" :text "reStructuredText"}
              {:value "html" :text "HTML"}])

(defn head [{:keys [component title]}]
  [:head
   [:meta {:charset "utf-8"}]
   (when title [:title title])
   (include-css (str "/css/" component (env :css-ext)))])

(defn options-body
  []
  (into
   [:body
    [:form {:id "options-form"}

     [:h3
      [:span {:class "bracket" :id "left-backet"} "["]
      "Yank options"
      [:span {:class "bracket" :id "right-bracket"} "]"]]

     [:div {:class "block" :id "keybinding"}
      [:label {:class "label"} "Keybinding"]
      [:input {:class "input" :type "text" :id "keybind-input"}]]

     [:div {:class "block" :id "format"}
      [:label {:class "label"} "Markup format"]
      [:select {:class "input" :id "format-select"}
       (for [f formats]
         [:option {:value (:value f)} (:text f)])]]

     [:div {:class "block" :id "buttons"}
      [:button {:type "reset"}
       "Reset"]
      [:button {:type "submit"}
       "Save"]]]]

   (if (= (env :location) "dev")
     [(include-js "js/options/goog/base.js")
      (include-js "js/options/cljs_deps.js")
      (include-js "setup.js")
      (include-js "options.js")]
     [(include-js "options.js")])))

(defn -main
  [& args]
  (let [options-html (html5
                      (head {:component "options"
                             :title "Yank extension options page"})
                      (options-body))
        options-path (str "resources/" (env :location) "/options.html")]
    (spit options-path options-html)
    )
  )
