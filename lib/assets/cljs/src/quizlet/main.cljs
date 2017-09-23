(ns quizlet.main
  (:require [reagent.core :as reagent]
            [re-frame.core :as rf]
            [clojure.string :as str]
            [cljs.pprint :refer [pprint]]
            [ajax.core :refer [GET POST PUT]]))


(defn pretty-print-data-edn [data]
      [:pre {:style { :background-color "transparent" :font-size "14px" :font-family "Helvetica Neue"}}
       (with-out-str (pprint data))])

;; -- Domino 1 - Event Dispatch -----------------------------------------------

;; -- Domino 2 - Event Handlers -----------------------------------------------
;; A set has this shape:
;;   {"id":221087650,"url":"/221087650/javascript-es5-vs-es6-flash-cards/","title":"Javascript ES5 vs ES6"}
(rf/reg-event-db              ;; sets up initial application state
  :initialize                 ;; usage:  (dispatch [:initialize])
  (fn [_ _]                   ;; the two parameters are not important here, so use _
      {:sets {:fetching false
              :items []
              :action ""           ; or one of "Saving...", "Deleting..."
              :error false
              :error-collection-msg ""}}))

(rf/register-handler
  :request-sets
  (fn [db _]
      (do
        (ajax.core/GET "/api/v0/sets"  ;; query http://localhost:3000/api/v0/sets
                       {:response-format :json
                        :format          :json
                        :keywords? true
                        :handler       #(rf/dispatch [:receive-sets-success %1])
                        :error-handler #(rf/dispatch [:receive-sets-failure %1])} )
        (-> db (assoc-in [:sets :fetching] true)
               (assoc-in [:sets :action] "Fetching sets...")))))

(rf/register-handler
  :receive-sets-success
  (fn [db [_ {sets :sets }]] ;; data is inside of {:sets [{...} {...}]}
      (-> db (assoc-in [:sets :items] sets)
          (assoc-in [:sets :action] "Sets fetched")
          (assoc-in [:sets :error] false)
          (assoc-in [:sets :error-collection-msg] "")
          (assoc-in [:sets :fetching] false))))

(rf/register-handler
  :receive-sets-failure ;; TODO rails controller should through meaningfull errors
  (fn [db [response]]
      (-> db (assoc-in [:sets :items] response)
          (assoc-in [:sets :action] "Sets failed to be fetched")
          (assoc-in [:sets :error] true)
          (assoc-in [:sets :error-collection-msg] response)
          (assoc-in [:sets :fetching] false))))

;; -- Domino 4 - Query  -------------------------------------------------------

(rf/reg-sub
  :set-items
  (fn [db _]     ;; db is current app state. 2nd unused param is query vector
      (get-in db [:sets :items]))) ;; return a query computation over the application state

(rf/reg-sub
  :reported-human-status
  (fn [db _]     ;; db is current app state. 2nd unused param is query vector
      (get-in db [:sets :action])))

(rf/reg-sub
  :fetching-sets
  (fn [db _]     ;; db is current app state. 2nd unused param is query vector
      (get-in db [:sets :fetching])))

;; -- Domino 5 - View Functions ----------------------------------------------

(defn set-list
      [set-items]
      [:ul
       (for [item  set-items]
            ^{:key (:id item)}  [:li [:a {:href (:url item)} (:title item)] [:br] (:url item)])])

(defn ui
  []
  (let [set-items @(rf/subscribe [:set-items])
        reported-human-status @(rf/subscribe [:reported-human-status])
        fetching @(rf/subscribe [:fetching-sets])]
  [:div
   [:h2 "List of sets"
    (when fetching
          [:span [:i.icon-refresh.icon-spin] " Loading..."])] ;; FIX missing icon-refresh.icon-spin
   [:div
    [:p reported-human-status]
    ;[pretty-print-data-edn set-items]
    [:button {:on-click #(rf/dispatch [:request-sets])} "Fetch Sets"]
    (when (seq set-items)
         [set-list set-items])]]))

   


;; -- Entry Point -------------------------------------------------------------

(defn ^:export run
  []
  (rf/dispatch-sync [:initialize])     ;; puts a value into application state
  (rf/dispatch [:request-sets])
  (reagent/render [ui]              ;; mount the application's ui into '<div id="app" />'
                  (js/document.getElementById "app")))
