(defproject save-to-org "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha16"]
                 [org.clojure/clojurescript "1.9.542"]
                 [binaryage/oops "0.5.4"]]
  :profiles {:dev {:dependencies [[figwheel-sidecar "0.5.10"]]
                   :plugins      [[lein-cljsbuild "1.1.6"]
                                  [lein-figwheel "0.5.10"]]
                   :source-paths ["script"]}}

  :source-paths ["src/lib"]

  :figwheel {:server-port 6888
             :repl        false}

  :cljsbuild {:builds [{:id           "background"
                        :source-paths ["src/background"]
                        :compiler     {:main          save-to-org.background
                                       :asset-path    "js/popup"
                                       :output-to     "resources/public/js/background/main.js"
                                       :output-dir    "resources/public/js/background"
                                       :source-map    true
                                       :optimizations :none}
                        :figwheel true}
                       {:id           "options"
                        :source-paths ["src/options"]
                        :compiler     {:main          save-to-org.options
                                       :asset-path    "js/options"
                                       :output-to     "resources/public/js/options/main.js"
                                       :output-dir    "resources/public/js/options"
                                       :source-map    true
                                       :optimizations :none}
                        :figwheel true}]}

  :clean-targets ^{:protect false} ["target" "resources/public/js"])
