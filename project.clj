(defproject hiccuppen "0.1.0-SNAPSHOT"
  :description "A pen for hiccup"
  :url "http://github.com/tie-rack/hiccupen"
  :license {:name "Mozilla Public License"
            :url "https://www.mozilla.org/en-US/MPL/2.0/"}

  :jvm-opts ["--add-modules" "java.xml.bind"]

  :min-lein-version "2.7.1"

  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.9.946"]
                 [org.clojure/core.async  "0.4.474"]
                 [macchiato/hiccups "0.4.1"]
                 [reagent "0.7.0"]]

  :plugins [[lein-figwheel "0.5.15"]
            [lein-cljsbuild "1.1.7" :exclusions [[org.clojure/clojure]]]]

  :source-paths ["src"]

  :cljsbuild {:builds
              [{:id "dev"
                :source-paths ["src"]

                :figwheel {:open-urls ["http://localhost:3449/index.html"]}

                :compiler {:main hiccuppen.core
                           :asset-path "js/compiled/out"
                           :output-to "resources/public/js/compiled/hiccuppen.js"
                           :output-dir "resources/public/js/compiled/out"
                           :source-map-timestamp true
                           :preloads [devtools.preload]}}
               {:id "min"
                :source-paths ["src"]
                :compiler {:output-to "resources/public/js/compiled/hiccuppen.js"
                           :main hiccuppen.core
                           :optimizations :advanced
                           :pretty-print false}}]}

  :figwheel {:css-dirs ["resources/public/css"] ;; watch and update CSS
             }

  :profiles {:dev {:dependencies [[binaryage/devtools "0.9.9"]
                                  [figwheel-sidecar "0.5.15"]
                                  [com.cemerick/piggieback "0.2.2"]]
                   :source-paths ["src" "dev"]
                   :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
                   :clean-targets ^{:protect false} ["resources/public/js/compiled"
                                                     :target-path]}})
