(ns clojure-boids.boid
  (:import [java.lang.Math]))

(def velocity 1)

(defn random [world-w world-h n]
  (let [m 50
        w (- world-w (* 2 m))
        h (- world-h (* 2 m))]
  {:x (+ (rand w) m) 
   :y (+ (rand h) m) 
   :a (rand (* 2 Math/PI))
   :w 0
   :alpha 0
   }))

(defn update [dt world {:keys [x y a w alpha] :as boid}]
  (let [v-x (Math/sin a) 
        v-y (Math/cos a)]
    (assoc boid 
           :x (+ x (* dt v-x))
           :y (+ y (* dt v-y))
           :a (+ a (* dt w))
           :w (+ w (* dt alpha)))))
