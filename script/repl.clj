(use 'figwheel-sidecar.repl-api)
(start-figwheel! "popup" "background" "content") ;; <-- fetches configuration
(cljs-repl)
