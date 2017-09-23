(set-env! :dependencies '[[org.clojure/clojure        "1.8.0"]
                          [org.clojure/clojurescript  "1.9.908"]
                          [reagent "0.7.0"]
                          [re-frame "0.10.1"]
                          [binaryage/devtools "0.9.4"]
                          [secretary "1.2.3"]
                          [adzerk/boot-cljs "2.1.3" :scope "test"]
                          [adzerk/boot-reload          "0.5.2"]
                          [cljs-ajax "0.7.2"]
                          [com.cemerick/piggieback "0.2.1" :scope "test"]
                          [pandeiro/boot-http "0.8.3" :scope "test"]
                          [weasel "0.7.0" :scope "test"]]
          :resource-paths #{"lib/assets/cljs/src"})

(require '[adzerk.boot-cljs :refer [cljs]]
         '[adzerk.boot-reload    :refer [reload]]
         '[pandeiro.boot-http :refer [serve]])

(deftask dev
  "Compile for local development."
  []
  (comp
    (serve :port 8342)
    (watch)
    (speak)
    (reload :on-jsload 'quizlet.main/run :asset-host "http://localhost:8342")
    (cljs :source-map true
          :optimizations :none
          :compiler-options {:asset-path "/assets/cljs/target/main.out"})
    (target :dir #{"public/assets/cljs/target"})))

(deftask prod
  "Compile for production."
  []
  (comp
    (cljs :optimizations :advanced
          :compiler-options {:compiler-stats true
                             :output-wrapper :true
                             :parallel-build true})
    (target :dir #{"public/assets/cljs/target"})))
