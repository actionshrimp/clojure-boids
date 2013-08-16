(ns clojure-boids.boid
  (:import [java.lang.Math]))

(def velocity 1)

(defn random [world-w world-h n]
  (let [m 50
        w (- world-w (* 2 m))
        h (- world-h (* 2 m))]
  {:x (+ (rand w) m) :y (+ (rand h) m) :a (/ Math/PI 2)}))

(defn update [world {:keys [x y a] :as boid}]
  (let [v-x (Math/sin a) v-y (Math/cos a)]
    (assoc boid :x (+ x v-x) :y (+ y v-y))))
