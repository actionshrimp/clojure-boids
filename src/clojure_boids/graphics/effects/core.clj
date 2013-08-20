(ns clojure-boids.graphics.effects.core)

(defmulti init :effect)
(defmulti update :effect)
(defmulti draw :effect)
