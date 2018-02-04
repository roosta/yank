(defproject yank "0.2.1"
  :description "Yank current page URL to clipboard as various markup formats"
  :url "https://github.com/roosta/yank"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha16"]
                 [org.clojure/clojurescript "1.9.542"]]

  :clean-targets ^{:protect false} ["target" "resources/dev/js" "resources/release/js"]

  :source-paths ["src/lib"]

  :plugins [[lein-cljsbuild "1.1.6"]
            [lein-garden "0.3.0"]
            [lein-shell "0.5.0"]]

  :aliases {"release" ["do"
                       ["clean"]
                       ["cljsbuild" "once" "background-release" "options-release" "content-script-release" "popup-release"]
                       ["shell" "script/package.sh"]]
            "package" ["shell" "script/package.sh"]
            "content" ["cljsbuild" "auto" "content-script"]
            "fig"     ["figwheel" "options" "background"]}

  :garden {:builds [{:id "popup"
                     :source-paths ["src/styles/popup"]
                     :stylesheet yank.core/screen
                     :compiler {:output-to "resources/dev/css/popup.css"
                                :pretty-print? true}}

                    {:id "options"
                     :source-paths ["src/styles/options"]
                     :stylesheet yank.core/screen
                     :compiler {:output-to "resources/dev/css/options.css"
                                :pretty-print? true}}]}

  :figwheel {:server-port 6888
             :css-dirs ["resources/dev/css"]
             :repl        true}

  :profiles {:dev {:dependencies [[figwheel-sidecar "0.5.10"]
                                  [com.cemerick/piggieback "0.2.2"]
                                  [org.clojure/tools.nrepl "0.2.10"]]
                   :plugins      [[lein-figwheel "0.5.10"]]

                   :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}

                   :source-paths ["script" "dev"]}}

  ;; :plugins [[lein-npm "0.6.2"]]
  ;; :npm {:dependencies [[source-map-support "0.4.0"]]}

  :cljsbuild {:builds [{:id           "background"
                        :source-paths ["src/background"]
                        :figwheel     {:on-jsload "yank.background.core/fig-reload"}
                        :compiler     {:main          yank.background
                                       :asset-path    "js/background"
                                       :output-to     "resources/dev/js/background/main.js"
                                       :output-dir    "resources/dev/js/background"
                                       :infer-externs true
                                       ;; :source-map    true
                                       :optimizations :none}}

                       {:id           "options"
                        :source-paths ["src/options"]
                        :figwheel     {:on-jsload "yank.options.core/fig-reload"}
                        :compiler     {:main          yank.options
                                       :asset-path    "js/options"
                                       :output-to     "resources/dev/js/options/main.js"
                                       :output-dir    "resources/dev/js/options"
                                       :infer-externs true
                                       ;; :source-map    true
                                       :optimizations :none}}

                       {:id           "popup"
                        :source-paths ["src/popup"]
                        :figwheel     {:on-jsload "yank.popup.core/fig-reload"}
                        :compiler     {:main          yank.popup
                                       :asset-path    "js/popup"
                                       :output-to     "resources/dev/js/popup/main.js"
                                       :output-dir    "resources/dev/js/popup"
                                       :infer-externs true
                                       ;; :source-map    true
                                       :optimizations :none}}

                       {:id           "content-script"
                        :source-paths ["src/content_script"]
                        :compiler     {:main          yank.content-script
                                       :asset-path    "js/content_script"
                                       :output-to     "resources/dev/js/content_script/main.js"
                                       :output-dir    "resources/dev/js/content_script"
                                       :infer-externs true
                                       :foreign-libs  [{:file     "src/js/mousetrap.js"
                                                        :file-min "src/js/mousetrap.min.js"
                                                        :provides ["js.mousetrap"]}]
                                       :pseudo-names  true
                                       :pretty-print  true
                                       :optimizations :advanced}}

                       {:id           "background-release"
                        :source-paths ["src/background"]
                        :compiler     {:main          yank.background
                                       :asset-path    "js/background"
                                       :output-to     "resources/release/js/background.js"
                                       :output-dir    "resources/release/js/background"
                                       :infer-externs true
                                       :elide-asserts true
                                       :optimizations :advanced}}

                       {:id           "options-release"
                        :source-paths ["src/options"]
                        :compiler     {:main          yank.options
                                       :asset-path    "js/options"
                                       :output-to     "resources/release/js/options.js"
                                       :output-dir    "resources/release/js/options"
                                       :infer-externs true
                                       :elide-asserts true
                                       :optimizations :advanced}}

                       {:id           "content-script-release"
                        :source-paths ["src/content_script"]
                        :compiler     {:main          yank.content-script
                                       :asset-path    "js/content-script"
                                       :output-to     "resources/release/js/content_script.js"
                                       :output-dir    "resources/release/js/content_script"
                                       :elide-asserts true
                                       :infer-externs true
                                       :foreign-libs  [{:file     "src/js/mousetrap.js"
                                                        :file-min "src/js/mousetrap.min.js"
                                                        :provides ["js.mousetrap"]}]
                                       :optimizations :advanced}}

                       {:id           "popup-release"
                        :source-paths ["src/popup"]
                        :compiler     {:main          yank.popup
                                       :asset-path    "js/popup"
                                       :output-to     "resources/release/js/popup.js"
                                       :output-dir    "resources/release/js/popup"
                                       :elide-asserts true
                                       :infer-externs true
                                       :optimizations :advanced}}]})
