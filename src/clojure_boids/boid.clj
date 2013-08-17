(ns clojure-boids.boid
  (:import [java.lang.Math]))

(def v-mag 100)
(def w-max-mag 2.0)

(defn random [world-w world-h n]
  (let [world-margin 50
        w (- world-w (* 2 world-margin))
        h (- world-h (* 2 world-margin))
        a (rand (* 2 Math/PI))]
  {:s [(+ (rand w) world-margin) 
       (+ (rand h) world-margin)] 
   :a a
   :v [(Math/sin a) (Math/cos a)] }))

(defn mag-bounded [max-mag value]
  (max (- max-mag) (min max-mag value)))

(defn dot [v1 v2]
  (apply + (doall (map * v1 v2))))

(defn normalize [v]
  (let [mag (Math/sqrt (dot v v))]
    (doall (map #(/ % mag) v))))

(defn sub [v1 v2]
  (doall (map - v1 v2)))

(defn calculate-w [t {:keys [s v a]}]
  (let [relative-t (sub t s)
        target-v (normalize relative-t)
        w-mag (* w-max-mag (* -1 (- (dot v target-v) 1)))
        steer-right-vec [(second v) (- (first v))]
        delta-v (sub target-v v)
        w-direc (dot delta-v steer-right-vec)]
    (if (> w-direc 0) w-mag (- w-mag))))

(defn update [dt world {:keys [s a] :as boid}]
  (let [target-s [400 400] 
        w (calculate-w target-s boid)
        new-a (+ a (* dt w))
        new-v-unit [(Math/sin new-a) (Math/cos new-a)]
        ds (doall (map #(* dt v-mag %) new-v-unit))
        new-s (doall (map + s ds))]
    (assoc boid
           :s new-s
           :a new-a
           :v new-v-unit)))

;The distance from an obstacle a boid needs to start turning 
;in order to avoid it if aiming straight at it
(def panic-distance 
  ())
