(defproject json-roa/clj-utils "1.0.0-beta.6"
  :description "JSON-ROA utils for clojure. Ring middleware for negotiating, building and writing JSON-ROA"
  :url "https://github.com/json-roa/json-roa_clj-utils"
  :license {:name "The MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [
                 [org.clojure/clojure "1.6.0"]
                 [org.clojure/data.json "0.2.6"]
                 [ring-middleware-accept "2.0.3"]
                 [ring/ring-core "1.3.1"]
                 ]
  :development-dependencies[ ]
  ; :plugins [[codox "0.8.0"]]
  :profiles
  {:dev {:dependencies [
                        [cheshire "5.3.1"]
                        [cider-ci/clj-utils "2.0.1"]
                        [ring/ring-mock "0.2.0"]
                        ]}
   :1.4 {:dependencies [[org.clojure/clojure "1.4.0"]]}
   :1.5 {:dependencies [[org.clojure/clojure "1.5.1"]]}
   :1.6 {:dependencies [[org.clojure/clojure "1.6.0"]]}})
