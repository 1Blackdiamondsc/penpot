;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.
;;
;; Copyright (c) 2016 Andrey Antukh <niwi@niwi.nz>

(ns uxbox.main.ui.shapes.icon
  (:require [uxbox.util.mixins :as mx :include-macros true]
            [uxbox.main.ui.shapes.common :as common]
            [uxbox.main.ui.shapes.attrs :as attrs]
            [uxbox.main.geom :as geom]))

;; --- Icon Component

(declare icon-shape)

(mx/defc icon-component
  {:mixins [mx/static mx/reactive]}
  [{:keys [id] :as shape}]
  (let [selected (mx/react common/selected-ref)
        selected? (contains? selected id)
        on-mouse-down #(common/on-mouse-down % shape selected)]
    [:g.shape {:class (when selected? "selected")
               :on-mouse-down on-mouse-down}
     (icon-shape shape identity)]))

;; --- Icon Shape

(mx/defc icon-shape
  {:mixins [mx/static]}
  [{:keys [x1 y1 content id metadata] :as shape} factory]
  (let [key (str "shape-" id)
        ;; rfm (geom/transformation-matrix shape)
        view-box (apply str (interpose " " (:view-box metadata)))
        size (geom/size shape)
        attrs (merge {:id key :key key ;; :transform (str rfm)
                      :x x1 :y y1 :view-box view-box
                      :preserve-aspect-ratio "none"
                      :dangerouslySetInnerHTML {:__html content}}
                     size
                     (attrs/extract-style-attrs shape)
                     (attrs/make-debug-attrs shape))]
    [:svg attrs]))

;; --- Icon SVG

(mx/defc icon-svg
  {:mixins [mx/static]}
  [{:keys [content id metadata] :as shape}]
  (let [view-box (apply str (interpose " " (:view-box metadata)))
        id (str "icon-svg-" id)
        props {:view-box view-box :id id
               :dangerouslySetInnerHTML {:__html content}}]
    [:svg props]))