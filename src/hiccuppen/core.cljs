;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.

(ns hiccuppen.core
  (:require [cljs.pprint :as pp :refer [pprint]]
            [cljs.reader :as reader]
            [reagent.core :as reagent :refer [atom]]
            [hiccups.runtime :as hiccups]))

(set! pp/*print-right-margin* 65)

(defonce app-state
  (atom {:hiccup
         (with-out-str
           (pprint
            [:div
             [:p "Hello, " [:em.green "Christine"] "!"]
             [:p "Here's a "
              [:a {:href "https://www.kilosecond.com/hiccuppen/"} "link"]]]))
         :css "/* styling */\n\n.green { color: green; }"}))

(defn update-app-from-textarea [k]
  (fn [e]
    (swap! app-state assoc k (-> e .-target .-value))))

(defn iframe-hiccup [styles body]
  [:html
   [:head
    [:base {:target "_parent"}]
    [:style styles]]
   [:body
    body]])

(defn update-iframe [_ _ _ new-state]
  (try
    (let [{:keys [hiccup css]} new-state
          hiccup-data (reader/read-string hiccup)
          hiccup-html (hiccups/render-html (iframe-hiccup css hiccup-data))
          iframe (.getElementById js/document "hiccup-frame")]
      (set! (.-srcdoc iframe) hiccup-html))
    (catch :default _
      nil)))

(defn hello-world []
  (fn []
    (let [rendered-html (atom "")
          valid-state (atom true)]
      (reagent/create-class
       {:component-did-mount #(update-iframe nil nil nil @app-state)
        :reagent-render
        (fn []
          (let [{:keys [hiccup css]} @app-state]
            (try
              (let [hiccup-data (reader/read-string hiccup)
                    hiccup-html (hiccups/render-html hiccup-data)]
                (reset! valid-state true)
                (reset! rendered-html hiccup-html))
              (catch :default _
                (reset! valid-state false)))
            [:div
             [:div.container
              [:textarea.edit-box {:class (if @valid-state
                                            [:valid]
                                            [:invalid])
                                   :value hiccup
                                   :on-change (update-app-from-textarea :hiccup)}]
              [:textarea.edit-box {:value css
                                   :on-change (update-app-from-textarea :css)}]]
             [:div.container.bordered @rendered-html]
             [:iframe#hiccup-frame.container.bordered]]))}))))

(add-watch app-state :update-iframe update-iframe)

(reagent/render-component [hello-world]
                          (. js/document (getElementById "app")))
