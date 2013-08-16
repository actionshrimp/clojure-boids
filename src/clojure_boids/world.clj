(ns clojure-boids.world
  (:require [clojure-boids.boid :as boid])
  (:import [java.lang.System]))

(defn get-time []
  (/ (System/nanoTime) 1000000))

(defn initial [[w h :as world-size] boid-count]
  (atom {:running true
         :t (get-time)
         :width w :height h 
         :boids (map #(boid/random w h %) (range boid-count))}))

(defn update [world]
  (let [{:keys [t boids] :as w} @world
        t2 (get-time)
        dt (- t2 t)]
    (swap! world assoc
           :t t2
           :boids (doall (map #(boid/update dt w %) boids)))))

(defn simulate [world]
  (loop []
    (if (@world :running)
      (do
        (update world)
        (recur)))))

(defn stop [world]
  (swap! world assoc :running false))
