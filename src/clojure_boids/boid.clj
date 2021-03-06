(ns clojure-boids.boid
  (:require [clojure-boids.util.vec :as v])
  (:import [java.lang.Math]))

(def v-mag 100)
(def margin 50)
(def w-max-mag 2)
;
;The distance from an obstacle a boid needs to start turning 
;in order to avoid it if aiming straight at it
(def panic-distance 
  (* v-mag (/ (/ Math/PI 2) w-max-mag)))

(def awareness panic-distance)
(def wall-evasion 5)
(def alignment 100)
(def separation 2000)
(def cohesion 200)

(defn spatial-hash-key [boid]
  (map #(int (/ % awareness)) (boid :s)))

(defn random [world-w world-h n]
  (let [world-margin margin
        w (- world-w (* 2 world-margin))
        h (- world-h (* 2 world-margin))
        a (rand (* 2 Math/PI))
        x (+ (rand w) world-margin)
        y (+ (rand h) world-margin)]
  {:n n
   :s [x y] 
   :target-v [0 0]
   :a a
   :v [(Math/sin a) (Math/cos a)]
   :r-awareness awareness}))

(defn calculate-w [target-v {:keys [s v a]}]
  (let [w-mag (* w-max-mag (* -1 (- (v/dot v target-v) 1)))
        steer-right-vec [(second v) (- (first v))]
        delta-v (v/sub target-v v)
        w-direc (v/dot delta-v steer-right-vec)]
    (if (> w-direc 0) 
      w-mag 
      (- w-mag))))

;Calculate a target position which will result in steering away from walls
(defn avoid-wall-vec [world {:keys [s v] :as boid}]
  (let [test-vec (v/scale panic-distance v)
        test-pos (v/add s test-vec)
        m margin
        w (- (world :width) m)
        h (- (world :height) m)
        x (first test-pos)
        y (second test-pos)
        [s-x s-y] s]
    [(cond (> x w) (- w (- x w) s-x)
            (< x m) (- m (- x m) s-x)
            :else 0)
      (cond (> y h) (- h (- y h) s-y)
            (< y m) (- m (- y m) s-y)
            :else 0)]))

(defn align-with-neighbours [neighbours {:keys [v]}]
  (if (> (count neighbours) 0)
    (v/normalize (apply v/add (map :v neighbours)))
    [0 0]))

(defn avoid-neighbours [neighbours {:keys [s]}]
  (if (> (count neighbours) 0)
    (apply v/add (map 
                   (fn [v] 
                     (let [relative (v/sub s v)
                           mag (v/mag relative)]
                       (v/scale (/ 1 (* mag mag)) relative)))
                   (map :s neighbours)))
    [0 0]))

(defn group-with-neighbours [neighbours {:keys [s]}]
  (if (> (count neighbours) 0)
    (v/normalize (v/sub (v/scale (/ 1 (count neighbours)) (apply v/add (map :s neighbours))) s))
    [0 0]))

(defn calc-target-v [world neighbours {:keys [n s v r-awareness] :as boid}]
  (v/normalize 
    (v/add 
      v
      (v/scale alignment (align-with-neighbours neighbours boid))
      (v/scale separation (avoid-neighbours neighbours boid))
      (v/scale cohesion (group-with-neighbours neighbours boid))
      (v/scale wall-evasion (avoid-wall-vec world boid)))))

(defn is-neighbour? [n s r poss-neighbour]
  (let [poss-neighbour-n (poss-neighbour :n)
        poss-neighbour-s (poss-neighbour :s)]
    (and (not= n poss-neighbour-n)
         (let [distance (v/mag (v/sub poss-neighbour-s s))]
           (< distance r)))))

(defn neighbours [{:keys [boid-spatial-hash]} {:keys [n s r-awareness] :as boid}]
  (let [[hx hy] (spatial-hash-key boid)
        neighbour-hashes [[(- hx 1) (- hy 1)] [hx (- hy 1)] [(+ hx 1) (- hy 1)]
                          [(- hx 1)    hy   ] [hx    hy   ] [(+ hx 1)    hy   ]
                          [(- hx 1) (+ hy 1)] [hx (+ hy 1)] [(+ hx 1) (+ hy 1)]]
        possible-neighbours (mapcat boid-spatial-hash neighbour-hashes)]
    (filter #(is-neighbour? n s r-awareness %) possible-neighbours)))

(defn update [dt world {:keys [s a] :as boid}]
  (let [neighbours (neighbours world boid)
        target-v (calc-target-v world neighbours boid)
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
