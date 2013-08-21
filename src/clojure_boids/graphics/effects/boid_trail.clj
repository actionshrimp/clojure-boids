(ns clojure-boids.graphics.effects.boid-trail
  (:require [clojure-boids.graphics.effects.core :as effect])
  (:import [org.lwjgl.opengl GL11]))

(def id :boid-trail)

(defmethod effect/init id [e {:keys [boids] :as world}]
  (assoc e :histories 
         (apply hash-map 
                (mapcat #(vector (% :n) [(% :s)]) boids))))

(defn updated-history [{:keys [histories max-history]} {:keys [n s] :as boid}]
  (let [h (histories n)]
    (if (> (count h) max-history)
      (conj (vec (rest h)) s)
      (conj h s))))

(defmethod effect/update id [e {:keys [boids] :as world}]
  (assoc e :histories 
         (apply hash-map 
                (mapcat #(vector (% :n) (updated-history e %)) boids))))

(defn draw-trail [t]
  (let [len (count t)]
    (GL11/glBegin GL11/GL_LINE_STRIP)
    (GL11/glColor4f 1 1 1 0.5)
    (doall (map #(GL11/glVertex2f (first %) (second %)) t))
    (GL11/glEnd)))
   
(defmethod effect/draw id [e]
  (doall (map #(draw-trail (second %)) (e :histories))))
