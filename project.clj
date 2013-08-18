(require 'leiningen.core.eval)

(def lwjgl-native-classifier 
  (let [classifiers {:macosx "natives-osx"
                     :linux "natives-linux"
                     :windows "natives-windows"}
        os (leiningen.core.eval/get-os)]
    (get classifiers os)))

(defproject clojure-boids "0.1.0-SNAPSHOT"
  :description "Boids"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [org.lwjgl.lwjgl/lwjgl "2.9.0"]
                 [org.lwjgl.lwjgl/lwjgl-platform "2.9.0"
                  :classifier ~lwjgl-native-classifier :native-prefix ""]
                 [slick-util "1.0.0"]]
  :main clojure-boids.core)
