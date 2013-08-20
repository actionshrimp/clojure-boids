(ns clojure-boids.graphics.effects.boid-trail
  (:require [clojure-boids.graphics.effects.core :as effect]))

(def id :boid-trail)

(defmethod effect/init key [e {:keys [boids] :as world}]
  (assoc e :histories (apply hash-map (mapcat #([(% :n) [(% :s)]]) boids))))

(defn updated-history [{:keys [histories max-history]} {:keys [n s] :as boid}]
  (let [h (histories n)]
    (if (> (count h) max-history)
      (conj (vec (rest h)) s)
      (conj h s))))

(defmethod effect/update [e {:keys [boids] :as world}]
  (assoc e :histories 
         (apply hash-map 
                (mapcat #([(% :n) (updated-history e %)]) boids))))