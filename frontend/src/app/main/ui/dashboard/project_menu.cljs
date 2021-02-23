;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.
;;
;; This Source Code Form is "Incompatible With Secondary Licenses", as
;; defined by the Mozilla Public License, v. 2.0.
;;
;; Copyright (c) 2021 UXBOX Labs SL

(ns app.main.ui.dashboard.project-menu
  (:require
   [app.main.data.dashboard :as dd]
   [app.main.data.modal :as modal]
   [app.main.store :as st]
   [app.main.ui.components.context-menu :refer [context-menu]]
   [app.util.i18n :as i18n :refer [tr]]
   [app.util.router :as rt]
   [rumext.alpha :as mf]))

(mf/defc project-menu
  [{:keys [project team show? on-edit on-menu-close] :as props}]
  (let [delete-fn
        (mf/use-callback
          (mf/deps project)
          (fn [event]
            (st/emit! (dd/delete-project project)
                      (rt/nav :dashboard-projects {:team-id (:id team)}))))

        on-delete
        (mf/use-callback
          (mf/deps project)
          (st/emitf (modal/show
                      {:type :confirm
                       :title (tr "modals.delete-project-confirm.title")
                       :message (tr "modals.delete-project-confirm.message")
                       :accept-label (tr "modals.delete-project-confirm.accept")
                       :on-accept delete-fn})))]

    [:& context-menu {:on-close on-menu-close
                      :show show?
                      :options [[(tr "labels.rename") on-edit]
                                [(tr "labels.delete") on-delete]]}]))

