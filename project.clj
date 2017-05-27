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

  :cljsbuild {:builds [{:id           "popup"
                        :source-paths ["src/popup"]
                        :compiler     {:main          save-to-org.popup
                                       :asset-path    "js/popup"
                                       :output-to     "resources/public/js/popup/main.js"
                                       :output-dir    "resources/public/js/popup"
                                       :source-map    true
                                       :optimizations :none}
                        :figwheel     true}
                       {:id           "background"
                        :source-paths ["src/background"]
                        :compiler     {:main          save-to-org.background
                                       :asset-path    "js/popup"
                                       :output-to     "resources/public/js/background/main.js"
                                       :output-dir    "resources/public/js/background"
                                       :source-map    true
                                       :optimizations :none}
                        :figwheel     true}
                       {:id           "content"
                        :source-paths ["src/content"]
                        :compiler     {:main          save-to-org.content
                                       :asset-path    "js/content"
                                       :output-to     "resources/public/js/content/main.js"
                                       :output-dir    "resources/public/js/content"
                                       :source-map    true
                                       :optimizations :none}
                        :figwheel     true}]}

  :clean-targets ^{:protect false} ["target" "resources/public/js"])
