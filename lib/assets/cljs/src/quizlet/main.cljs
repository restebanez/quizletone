(ns quizlet.main
  (:require [reagent.core :as reagent]
            [re-frame.core :as rf]
            [clojure.string :as str]))

(defn set-list
      []
      [:ul
       [:li [:a {:href "http://github.com"} "GitHub"] [:br]  "First one"]
       [:li [:a {:href "http://github.com"} "GitHub"] [:br]  "Second one"]])

(defn ui
  []
  [:div
   [:h2 "List of sets"]
   (when true
         [set-list])])

   


;; -- Entry Point -------------------------------------------------------------

(defn ^:export run
  []
  (rf/dispatch-sync [:initialize])     ;; puts a value into application state
  (reagent/render [ui]              ;; mount the application's ui into '<div id="app" />'
                  (js/document.getElementById "app")))
