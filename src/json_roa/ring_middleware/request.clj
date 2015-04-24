; Copyright (C) 2013, 2014 Dr. Thomas Schank  (DrTom@schank.ch, Thomas.Schank@algocon.ch)
; Released under the MIT license.

(ns json-roa.ring-middleware.request
  (:require 
    [clojure.data.json :as json]
    [ring.util.response]
    [ring.middleware.accept]
    ))

(defn- default-json-decoder [json-str]
  (json/read-str json-str :key-fn keyword))

(defn- dispatch [json-roa-handler request response]
  (let [mime (or (-> request :accept :mime) 
                 "")]
    (if (re-matches #"application\/.*\bjson-roa\b.*" mime)
      (json-roa-handler request response)
      response)))


(defn- wrap-dispatch [handler json-roa-handler]
  (fn [request]
    (let [response (handler request)]
      (dispatch json-roa-handler request response)
      )))

;### accept ###################################################################

(defn not-acceptable [request]
  (-> 
    {:status 406
     :body "This resource accepts 'application/json-roa+json' or 'application/json' only."}
    (ring.util.response/header "Content-Type" "text/plain")
    (ring.util.response/charset "UTF-8")))

(defn wrap-accept [handler]
  (fn [request]
    (let [mime (-> request :accept :mime)] 
      (cond 
        (not mime) (not-acceptable request)
        (re-matches #".*json.*" mime) (handler request)
        :else (not-acceptable request)))))

(defn wrap-negotiate-accept [handler]
  (ring.middleware.accept/wrap-accept 
    handler
    {:mime 
     ["application/json-roa+json" :qs 1 
      "application/json" :qs 0.5
      ]}))


;### wrap #####################################################################

(defn wrap [handler json-roa-handler
            & {:keys [json-decoder] :or {json-decoder default-json-decoder}}]
  (-> handler 
      (wrap-dispatch json-roa-handler)
      wrap-accept
      wrap-negotiate-accept
      )) 




