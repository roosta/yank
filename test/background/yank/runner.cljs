(ns yank.runner
  (:require
   [doo.runner :refer-macros [doo-tests]]
   [yank.core-test]
   [cljs.test :as t :include-macros true]))

(doo-tests 'yank.core-test)
