(ns clojure-boids.core
  (:require [clojure-boids.graphics.core :as graphics]
            [clojure-boids.world :as world]
            )
  (:import [org.lwjgl.opengl Display DisplayMode]))

(def world-size [800 600])
(def boid-count 20)

(defn init [world]
  (let [display-mode (DisplayMode. (world :width) (world :height))]
    (Display/setDisplayMode display-mode)
    (Display/create)
    (graphics/init world)))

(defn cleanup []
  (Display/destroy)
  (prn "Bye!"))

(defn main-loop [world]
  (if (not (Display/isCloseRequested))
    (do
      (graphics/draw world)
      (Display/update)
      (recur (world/update world)))))

(defn start-game [] 
  (let [world (world/initial world-size boid-count)]
    (init world)
    (main-loop world)
    (cleanup)))

(defn -main [] 
  (.start (Thread. start-game)))
