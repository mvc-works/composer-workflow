
(ns app.comp.container
  (:require [hsl.core :refer [hsl]]
            [respo-ui.core :as ui]
            [respo.core
             :refer
             [defcomp cursor-> action-> mutation-> <> div button textarea span]]
            [respo.comp.space :refer [=<]]
            [reel.comp.reel :refer [comp-reel]]
            [respo-md.comp.md :refer [comp-md]]
            [app.config :refer [dev?]]
            [composer.core :refer [render-markup extract-templates]]
            [shadow.resource :refer [inline]]
            [cljs.reader :refer [read-string]]
            [cumulo-util.core :refer [id! unix-time!]]
            [respo.comp.inspect :refer [comp-inspect]]))

(defcomp
 comp-container
 (reel view-model on-action)
 (let [store (:store reel)
       states (:states store)
       templates (extract-templates (read-string (inline "composer.edn")))]
   (div
    {}
    (render-markup
     (get templates "container")
     {:data view-model, :templates templates, :level 1}
     (fn [d! op param options] (on-action d! op param options view-model)))
    (when dev? (comp-inspect "Store" store {}))
    (when dev? (cursor-> :reel comp-reel states reel {})))))
