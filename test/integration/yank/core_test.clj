(ns yank.core-test
  (:import [java.awt Toolkit HeadlessException]
           [java.awt.datatransfer DataFlavor UnsupportedFlavorException]
           [java.io IOException])
  (:require [clojure.test :as t :refer [deftest testing is use-fixtures]]
            [etaoin.keys :as k]
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
  "Executes a test running a driver. Installs extension and binds a driver with the
  global *driver* variable."
  [f]
  (with-firefox {} driver
    (e/with-resp driver :post
      [:session (:session @driver) :moz :addon :install]
      {:path extension-file :temporary true}
      _)
    (binding [*driver* driver]
      (f))))

(use-fixtures
  :each ;; start and stop driver for each test
  fixture-driver)

(deftest ^:integration
  org-mode
  (doto *driver*
    (go "https://google.com")
    (e/fill {:tag :body} k/escape)
    (e/fill {:tag :body} k/control-left "y" ))
  (let [clipboard (-> (Toolkit/getDefaultToolkit)
                           (.getSystemClipboard)
                           (.getData (DataFlavor/stringFlavor)))
        expected "[[https://www.google.com/][Google]]"]
    (is (= clipboard expected))))

(deftest ^:integration
  markdown
  (doto *driver*
    (e/go "about:addons")
    (e/click :category-extension)
    (e/click {:class "addon addon-view"})
    (e/click{:class "addon-control preferences"}))
  (let [window-handles (e/get-window-handles *driver*)]
    (doto *driver*
      (e/switch-window (last window-handles))
      (e/click :format-select)
      (e/fill :format-select k/arrow-down)
      (e/click {:type :submit})
      (e/go "https://google.com")
      (e/fill {:tag :body} k/escape)
      (e/fill {:tag :body} k/control-left "y" )))
  (let [clipboard (-> (Toolkit/getDefaultToolkit)
                      (.getSystemClipboard)
                      (.getData (DataFlavor/stringFlavor)))
        expected "[Google](https://www.google.com/)"]
    (is (= clipboard expected))))
