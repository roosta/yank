(ns yank.core-test
  (:require [clojure.test :as t :refer [deftest testing is use-fixtures]]
            [etaoin.api :as e :refer [with-firefox firefox go upload-file click]]))


(def project-version
  (-> (System/getProperty "user.dir")
      (str "/project.clj")
      slurp
      read-string
      (nth 2)))

(def extension-file
  (-> (System/getProperty "user.dir")
      (str "/releases/yank-" project-version ".xpi")))

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

(e/with-resp driver :post
  [:session (:session @driver) :moz :addon :install]
  {:path extension-file :temporary true}
  _)


;; (go driver "about:debugging")
