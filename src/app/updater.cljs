
(ns app.updater (:require [respo.cursor :refer [mutate]]))

(defn updater [store op op-data op-id op-time]
  (case op
    :states (update store :states (mutate op-data))
    :hydrate-storage op-data
    :input (assoc store :input op-data)
    :submit
      (-> store
          (assoc :input "")
          (update :records (fn [records] (conj records (:input store)))))
    :remove
      (update
       store
       :records
       (fn [records] (->> records (filter (fn [x] (not= x op-data))) vec)))
    (do (println "Unknown op:" op) store)))
