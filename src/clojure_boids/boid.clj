(ns clojure-boids.boid)

(def velocity 1)

(defn random [world-w world-h n]
  (let [m 50
        w (- world-w (* 2 m))
        h (- world-h (* 2 m))]
  {:x (+ (rand w) m) :y (+ (rand h) m) :a 0}))

(defn update [{:keys [x y] :as boid}]
  (assoc boid :y (+ y velocity)))
