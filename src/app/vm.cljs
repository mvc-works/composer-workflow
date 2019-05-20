
(ns app.vm (:require [clojure.string :as string] [app.config :refer [dev?]]))

(defn get-view-model [store] store)

(def state-header
  {:init (fn [props state] (or state {:draft ""})),
   :update (fn [d! op context options state mutate!]
     (case op
       :input (mutate! (assoc state :draft (:value options)))
       :submit
         (when-not (string/blank? (:draft state))
           (d! :submit (:draft state))
           (mutate! (assoc state :draft "")))
       (do (println "Unknown op:" op))))})

(def state-task
  {:init (fn [props state] (or state {})),
   :update (fn [d! op context options state mutate!]
     (case op (:remove (d! :remove (:param options)))))})

(def states-manager {"header" state-header, "task" state-task})

(defn on-action [d! op context options view-model states]
  (let [param (:param options)
        template-name (:template-name context)
        state-path (:state-path context)
        mutate! (fn [x] (d! :states [state-path x]))
        this-state (get-in states (conj state-path :data))]
    (when dev? (println "Action" op param context (pr-str options)))
    (if (contains? states-manager template-name)
      (let [action-handler (get-in states-manager [template-name :update])
            state-fn (get-in states-manager [template-name :init])
            state (if (fn? state-fn) (state-fn (:data context) this-state) this-state)]
        (action-handler d! op context options state mutate!))
      (println "Unhandled template:" template-name))))
