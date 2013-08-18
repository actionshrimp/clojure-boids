(ns clojure-boids.world
  (:require [clojure-boids.boid :as boid])
  (:import [java.lang.System]
           [java.util.concurrent TimeUnit]))

(defn get-time []
  (/ (System/nanoTime) 1000000000))

(defn initial [[w h :as world-size] boid-count]
  (atom {:running true
         :t (get-time)
         :dt-avg [0 0]
         :width w :height h 
         :boids (map #(boid/random w h %) (range boid-count))}))

(defn update-boids [dt {:keys [boids] :as w}]
  (doall (map #(boid/update dt w %) boids)))

(defn update-boids-parallel [dt {:keys [boids] :as w}]
  (let [n (count boids)
        num-chunks 4
        chunk-size (int (+ 1 (/ n num-chunks)))
        chunks (partition chunk-size chunk-size [] boids)
        update-boid #(boid/update dt w %)]
    (doall (apply concat (pmap #(doall (map update-boid %)) chunks)))))

(defn update [world]
  (let [{:keys [t dt-avg boids] :as w} @world
        [dt-avg-val dt-avg-n] dt-avg
        t2 (get-time)
        dt (- t2 t)
        new-boids (update-boids-parallel dt w)]
    (swap! world assoc
           :dt-avg [(/ (+ dt (* dt-avg-n dt-avg-val)) (+ 1 dt-avg-n))
                    (+ 1 dt-avg-n)]
           :t t2
           :boids new-boids)))

(defn simulate [world]
  (loop []
    (if (@world :running)
      (do
        (update world)
        (recur)))))

(defn stop [world]
  (swap! world assoc :running false))
