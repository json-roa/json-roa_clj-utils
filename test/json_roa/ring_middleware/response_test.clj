; Copyright (C) 2014, 2015 Dr. Thomas Schank  (DrTom@schank.ch, Thomas.Schank@algocon.ch)
; Released under the MIT license.

(ns json-roa.ring-middleware.response-test
  (:require 
    [ring.util.response]
    [json-roa.ring-middleware.response]
    [clojure.data.json :as json]
    [cheshire.core :as cheshire]
    [clojure.tools.logging :as logging]
    [clj-logging-config.log4j :as logging-config]
    )
  (:use clojure.test))


(defn- default-json-decoder [json-str]
  (json/read-str json-str :key-fn keyword))

(deftest test-wrap-roa-json-response

  (testing "a response with body of type map including json-roa data"
    (let [input-response {:body {:_json-roa {:relations {} 
                                        :about {:version "0.0.0"}
                                        }}}
          built-response ((json-roa.ring-middleware.response/wrap
                            identity) input-response)]
      (logging/debug test-wrap-roa-json-response {:built-response built-response})
      (testing "the built response" 
        (is built-response)
        (let [headers (-> built-response :headers)]
          (logging/debug {:headers headers})
          (testing "has the correct header" 
            (is (re-matches #".*application\/json-roa\+json.*" (str headers)))))
        (let [data (-> built-response :body default-json-decoder)] 
          (logging/debug test-wrap-roa-json-response {:data data}) 
          (testing "the parsed json data" 
            (is (map? data))
            (is (:_json-roa data))
            )))))
  
  (testing "a response with body of type vector including json-roa data"
    (let [input-response {:body [{:_json-roa {:relations {} 
                                        :about {:version "0.0.0"}
                                        }}]}
          built-response ((json-roa.ring-middleware.response/wrap
                            identity) input-response)]
      (logging/debug test-wrap-roa-json-response {:built-response built-response})
      (testing "the built response" 
        (is built-response)
        (testing "has the correct header" 
          (is (re-matches #".*application\/json-roa\+json.*" (-> built-response :headers str))))
        (let [data (-> built-response :body default-json-decoder)] 
          (logging/debug test-wrap-roa-json-response {:data data}) 
          (testing "the parsed json data" 
            (is (coll? data))
            (is (-> data first :_json-roa))
            )))))
    
  

  (testing "using cheshire as a custom json encoder"
    (let [input-response {:body {:_json-roa {:relations {} 
                                        :about {:version "0.0.0"}
                                        }}}
          built-response ((json-roa.ring-middleware.response/wrap
                            identity :json-encoder cheshire/generate-string) input-response)]
      (logging/debug test-wrap-roa-json-response {:built-response built-response})
      (testing "the built response" 
        (is built-response)
        (let [headers (-> built-response :headers)]
          (logging/debug {:headers headers})
          (testing "has the correct header" 
            (is (re-matches #".*application\/json-roa\+json.*" (str headers)))))
        (let [data (-> built-response :body default-json-decoder)] 
          (logging/debug test-wrap-roa-json-response {:data data}) 
          (testing "the parsed json data" 
            (is (map? data))
            (is (:_json-roa data))
            )))))

  (testing "a response with body of type map not including json-roa data"
    (let [input-response {:body {:x 5}}
          built-response ((json-roa.ring-middleware.response/wrap
                            identity) input-response)]
      (testing "has not been modified at all" 
        (is (= input-response built-response)))))

  (testing "a response with body of type vector not including json-roa data"
    (let [input-response {:body [:x]}
          built-response ((json-roa.ring-middleware.response/wrap
                            identity) input-response)]
      (testing "has not been modified at all" 
        (is (= input-response built-response)))))

  )

