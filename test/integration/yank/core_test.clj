(ns yank.core-test
  (:require [clojure.test :as t :refer [deftest testing is use-fixtures]]
            [etaoin.api :refer [with-firefox firefox]]))

(def ^:dynamic
  *driver*)

(defn fixture-driver
  "Executes a test running a driver. Bounds a driver
   with the global *driver* variable."
  [f]
  (with-firefox {} driver
    (binding [*driver* driver]
      (f))))

(use-fixtures
  :each ;; start and stop driver for each test
  fixture-driver)

(deftest ^:integration
  i-pass
  (is (= 1 1)))
