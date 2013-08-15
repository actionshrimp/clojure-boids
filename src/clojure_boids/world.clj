(ns clojure-boids.world)

(defn random-boid [world-w world-h n]
  (let [m 50
        w (- world-w (* 2 m))
        h (- world-h (* 2 m))]
  { :x (+ (rand w) m) :y (+ (rand h) m) }))

(defn initial [[w h :as world-size] boid-count]
  { :width w :height h :boids (map (partial random-boid w h) (range boid-count)) })
