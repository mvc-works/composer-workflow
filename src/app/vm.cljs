
(ns app.vm (:require [clojure.string :as string] [app.config :refer [dev?]]))

(defn get-view-model [store] store)

(defn on-action [d! op param options view-model]
  (when dev? (println "Action" op param (pr-str options)))
  (case op
    :input (d! :input (:value options))
    :submit (when-not (string/blank? (:input view-model)) (d! :submit nil))
    :remove (d! :remove param)
    (do (println "Unknown op:" op))))
