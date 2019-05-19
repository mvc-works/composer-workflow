
(ns app.container
  (:require [hsl.core :refer [hsl]]
            [respo-ui.core :as ui]
            [respo.core :refer [defcomp cursor-> <> div button textarea span]]
            [respo.comp.space :refer [=<]]
            [reel.comp.reel :refer [comp-reel]]
            [app.config :refer [dev?]]
            [composer.core :refer [render-markup extract-templates]]
            [shadow.resource :refer [inline]]
            [cljs.reader :refer [read-string]]
            [respo.comp.inspect :refer [comp-inspect]]
            [app.vm :as vm]
            [respo.util.list :refer [map-val]]))

(defcomp
 comp-container
 (reel)
 (let [store (:store reel)
       states (:states store)
       templates (extract-templates (read-string (inline "composer.edn")))
       view-model (vm/get-view-model store)]
   (div
    {:style ui/global}
    (render-markup
     (get templates "container")
     {:data view-model,
      :templates templates,
      :level 1,
      :template-name "container",
      :state-path [],
      :states states,
      :state-fns (->> vm/states-manager
                      (map (fn [[alias manager]] [alias (:init manager)]))
                      (into {}))}
     (fn [d! op context options]
       (vm/on-action d! op (dissoc context :templates :state-fns) options view-model states)))
    (when dev? (comp-inspect "Store" store {}))
    (when dev? (cursor-> :reel comp-reel states reel {})))))
