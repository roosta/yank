(ns yank.core-test
  (:import [java.awt Toolkit HeadlessException]
           [java.awt.datatransfer DataFlavor UnsupportedFlavorException]
           [java.io IOException])
  (:require [clojure.test :as t :refer [deftest testing is use-fixtures]]
            [etaoin.keys :as k]
            [etaoin.api :as e ]))

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
  (e/with-firefox {} driver
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
  basic
  (doto *driver*
    (e/go "http://roosta.sh")
    (e/wait-visible :app)
    (e/fill {:tag :body} k/escape)
    (e/fill {:tag :body} k/control-left "y" ))
  (let [clipboard (-> (Toolkit/getDefaultToolkit)
                      (.getSystemClipboard)
                      (.getData (DataFlavor/stringFlavor)))
        expected "[[http://roosta.sh/][roosta.sh/]]"]
    (is (= clipboard expected))))

(deftest ^:integration
  change-opts
  (doto *driver*
    (e/go "about:addons")
    (e/wait-visible :category-extension)
    (e/click :category-extension)
    (e/click {:class "addon addon-view"})
    (e/click {:class "addon-control preferences"}))
  (let [window-handles (e/get-window-handles *driver*)]
    (doto *driver*
      (e/switch-window (last window-handles))
      (e/wait-visible :keybind-input)
      (e/click :keybind-input)
      (e/fill :keybind-input k/shift-left "l")
      (e/click :format-select)
      (e/fill :format-select k/arrow-down)
      (e/click {:type :submit})
      (e/go "http://roosta.sh")
      (e/wait-visible :app)
      (e/fill {:tag :body} k/escape)
      (e/fill {:tag :body} k/shift-left "l" )))
  (let [clipboard (-> (Toolkit/getDefaultToolkit)
                      (.getSystemClipboard)
                      (.getData (DataFlavor/stringFlavor)))
        expected "[roosta.sh/](http://roosta.sh/)"]
    (is (= clipboard expected))))
