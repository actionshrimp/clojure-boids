(ns clojure-boids.world
  (:require [clojure-boids.boid :as boid]))

(defn initial [[w h :as world-size] boid-count]
  {:width w :height h :boids (map #(boid/random w h %) (range boid-count))})

(defn update [world]
  (assoc world :boids (map boid/update (world :boids))))
