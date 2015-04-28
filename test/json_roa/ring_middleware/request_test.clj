; Copyright (C) 2014, 2015 Dr. Thomas Schank  (DrTom@schank.ch, Thomas.Schank@algocon.ch)
; Released under the MIT license.

(ns json-roa.ring-middleware.request-test
  (:require 
    [cheshire.core :as cheshire]
    [clj-logging-config.log4j :as logging-config]
    [clojure.data.json :as json]
    [clojure.tools.logging :as logging]
    [json-roa.ring-middleware.request]
    [ring.mock.request :as mock]
    )
  (:use clojure.test)
  )


(def base-request
  (-> (mock/request :post "/test")
      (mock/header "accept" "application/json")
      ))

(defn default-json-roa-handler [request json-response]
  (assoc json-response
         :body (assoc (:body json-response)
                      :_json-roa {})))
                     

(def json-response 
  {:status 200
   :body {:x 42}
   })

(defn default-json-handler [request]
  json-response
  )


(deftest test-wrap-json-roa-request 
  (let [base-resp ((json-roa.ring-middleware.request/wrap 
                     default-json-handler 
                     default-json-roa-handler) 
                   base-request)]
    (is (= json-response
           base-resp))
    (is (not (-> base-resp :body :_json-roa))
        "a pure json-response must not contain a _json-roa object")
    )

  (let [json-roa-resp ((json-roa.ring-middleware.request/wrap 
                         default-json-handler 
                         default-json-roa-handler) 
                       (mock/header 
                         base-request
                         "accept" "application/json-roa+json"))]

    (is json-roa-resp) 

    (is (-> json-roa-resp :body :_json-roa)
        "a json-roa-response must contain a _json-roa object")
    
    ))
