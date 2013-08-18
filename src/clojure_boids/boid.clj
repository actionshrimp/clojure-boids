(ns clojure-boids.boid
  (:require [clojure-boids.util.vec :as v])
  (:import [java.lang.Math]))

(def v-mag 100)
(def margin 50)
(def w-max-mag 2)

(defn random [world-w world-h n]
  (let [world-margin margin
        w (- world-w (* 2 world-margin))
        h (- world-h (* 2 world-margin))
        a (rand (* 2 Math/PI))
        x (+ (rand w) world-margin)
        y (+ (rand h) world-margin)]
  {:s [x y] 
   :target-v [0 0]
   :a a
   :v [(Math/sin a) (Math/cos a)]
   :r-awareness 100}))

(defn calculate-w [target-v {:keys [s v a]}]
  (let [w-mag (* w-max-mag (* -1 (- (v/dot v target-v) 1)))
        steer-right-vec [(second v) (- (first v))]
        delta-v (v/sub target-v v)
        w-direc (v/dot delta-v steer-right-vec)]
    (if (> w-direc 0) 
      w-mag 
      (- w-mag))))

;The distance from an obstacle a boid needs to start turning 
;in order to avoid it if aiming straight at it
(def panic-distance 
  (* v-mag (/ (/ Math/PI 2) w-max-mag)))

;Calculate a target position which will result in steering away from walls
(defn avoid-wall-vec [world {:keys [s v] :as boid}]
  (let [test-vec (v/scale panic-distance v)
        test-pos (v/add s test-vec)
        m margin
        w (- (world :width) m)
        h (- (world :height) m)
        x (first test-pos)
        y (second test-pos)]
    (v/sub [(cond (> x w) (- w (- x w))
                  (< x m) (- m (- x m))
                  :else x)
            (cond (> y h) (- h (- y h))
                  (< y m) (- m (- y m))
                  :else y)]
           s)))

(defn is-neighbour? [x r possible-neighbour]
  (let [p-n-x (possible-neighbour :x)
        distance (v/mag (v/sub p-n-x x))]
    (< distance r)))

(defn align-with-neighbours [neighbours {:keys [v]}])

(defn calc-target-v [world {:keys [x v r-awareness] :as boid}]
 ;(let [neighbours (filter #(is-neighbour? x r-awareness %) (world :boids))]
    (v/normalize (v/add v (avoid-wall-vec world boid)))
 ;  )
  )

(defn update [dt world {:keys [s a] :as boid}]
  (let [target-v (calc-target-v world boid)
        w (calculate-w target-v boid)
        new-a (+ a (* dt w))
        new-v-unit [(Math/sin new-a) (Math/cos new-a)]
        ds (doall (map #(* dt v-mag %) new-v-unit))
        new-s (doall (map + s ds))]
    (assoc boid
           :s new-s
           :target-v target-v
           :a new-a
           :v new-v-unit)))
