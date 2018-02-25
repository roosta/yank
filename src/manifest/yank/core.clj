(ns yank.core
  (:require [clojure.data.json :as json]
            [clojure.java.io :as io]
            [environ.core :refer [env]])
  (:gen-class))

(def project (-> (System/getProperty "user.dir")
                 (str "/project.clj")
                 slurp
                 read-string))

(def version (nth project 2))
(def description (nth project 4))
(def url (nth project 6))

(def manifest {:manifest_version 2
               :name "yank"
               :version version
               :description description
               :homepage_url url

               :icons {"48" "icons/48.png"
                       "96" "icons/96.png"}

               :permissions ["tabs"
                             "clipboardWrite"
                             "contextMenus"
                             "storage"
                             "<all_urls>"]
               :options_ui {:page "options.html"
                            :open_in_tab true}

               :content_scripts [{:matches ["http://*/*"
                                            "https://*/*"]
                                  :js ["js/content_script.js"]
                                  :all_frames true
                                  :run_at "document_end"}]

               :browser_action {:default_title "Yank format"
                                :default_icon {"16" "icons/16.png"
                                               "32" "icons/32.png"}
                                :default_popup "popup.html"}

               :applications {:gecko {:id "yank@roosta.sh"}}

               :background {:page "background.html"}})

(def manifest-dev (-> manifest
                      (assoc  :content_security_policy "script-src 'self' 'unsafe-eval'; object-src 'self'")
                      (assoc-in [:content_scripts 0 :js] ["js/content_script/main.js"])))

(defn -main
  [& args]
  (let [dev? (= (env :location) "dev")
        path (str "resources/" (env :location) "/manifest.json")
        target (if dev? manifest-dev manifest)
        out (json/write-str target :escape-slash false)]
    (spit path out)))
