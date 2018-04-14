;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.

(ns hiccuppen.core
  (:require [cljs.reader :as reader]
            [reagent.core :as reagent :refer [atom]]
            [hiccups.runtime :as hiccups]))

(defonce app-state (atom {:hiccup (str [:div "Hello " [:em "Christine"] "!"])}))

(defn update-hiccup [e]
  (swap! app-state assoc :hiccup (-> e .-target .-value)))

(defn hello-world []
  (let [hiccup-str (:hiccup @app-state)
        hiccup (reader/read-string hiccup-str)
        hiccup-html (hiccups/render-html hiccup)]
    [:div
     [:div.container
      [:textarea.hiccup {:value hiccup-str
                         :on-change update-hiccup}]]
     [:div.container.bordered hiccup-html]
     [:div.container.bordered {:dangerouslySetInnerHTML {:__html hiccup-html}}]]))

(reagent/render-component [hello-world]
                          (. js/document (getElementById "app")))
