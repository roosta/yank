(ns yank.runner
  (:require
   [doo.runner :refer-macros [doo-tests]]
   [yank.core-test]
   [cljs.test :as t :include-macros true]))

(set! *warn-on-infer* false)

(doo-tests 'yank.core-test)
