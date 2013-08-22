(ns clojure-boids.core
  (:require [clojure-boids.graphics.core :as graphics]
            [clojure-boids.world :as world])
  (:import [org.lwjgl.opengl Display DisplayMode PixelFormat]))

(def world-size [800 600])
(def boid-count 100)
(def diagnostics? false)
(def parallel? true)

(defn init [world]
  (let [display-mode (DisplayMode. (@world :width) (@world :height))
        fsaa-samples 4
        pixel-format (PixelFormat. 8 16 0 fsaa-samples)
        ]
    (Display/setDisplayMode display-mode)
    (Display/create pixel-format)
    (graphics/init @world diagnostics?)))

(defn cleanup []
  (Display/destroy)
  (shutdown-agents)
  (prn "Bye!"))

(defn exit? []
  (Display/isCloseRequested))

(defn ui-loop [world]
  (loop []
    (if-not (exit?)
      (do 
        (graphics/render @world diagnostics?)
        (Display/update)
        (recur))
      (do 
        (world/stop world)
        (cleanup)))))

(defn simulation [] 
  (let [world (world/initial world-size boid-count)]
    (init world)
    (.start (Thread. #(world/simulate world parallel?)))
    (ui-loop world)))

(defn -main [] 
  (.start (Thread. simulation)))
