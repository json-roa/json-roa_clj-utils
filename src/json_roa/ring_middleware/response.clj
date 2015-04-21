; Copyright (C) 2013, 2014 Dr. Thomas Schank  (DrTom@schank.ch, Thomas.Schank@algocon.ch)
; Released under the MIT license.

(ns json-roa.ring-middleware.response
  (:require 
    [clojure.data.json :as json]
    [clojure.tools.logging :as logging]
    [clojure.walk]
    [ring.util.response :as response]
    ))

(defn- default-json-encoder [data] 
  (json/write-str data))

(defn- build-roa-json-response [response body json-encoder]
  (-> response
      (assoc :body (json-encoder body))
      (response/header "Content-Type" "application/json-roa+json")
      (response/charset "UTF8")))

(defn- check-and-build-roa-json-response [response json-encoder]
  (if-not (coll? (:body response))
    response
    (if-let [body (-> response :body clojure.walk/keywordize-keys)]
      (if (or (and (map? body) (:_json-roa body))
              (and (coll? body) (-> body first :_json-roa)))
        (build-roa-json-response response body json-encoder)
        response)
      response)))

(defn wrap 
  "Ring middleware which converts responses with a body of either map or array
  containing a _json-roa signature into a json-roa+json response."
  [handler & {:keys [json-encoder] :or {json-encoder default-json-encoder}}]
  (fn [request]
    (let [response (handler request)]
      (check-and-build-roa-json-response response json-encoder))))

