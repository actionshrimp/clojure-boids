(ns clojure-boids.graphics.effects.boid-trail
  (:require [clojure-boids.graphics.effects.core :as effect]
            [clojure-boids.boid :as boid])
  (:import [org.lwjgl.opengl GL11]))

(def id :boid-trail)

(defmethod effect/init id [e {:keys [boids] :as world}]
  (assoc e :histories 
         (apply hash-map 
                (mapcat #(vector (% :n) [{:t (world :t) :s (% :s)}]) boids))))

(defn updated-history [{:keys [histories length]} t {:keys [n s] :as boid}]
  (let [h (histories n)
        cutoff-time (- t (/ length boid/v-mag))]
    (filter #(>= (% :t) cutoff-time) (conj h {:t t :s s}))))

(defmethod effect/update id [e {:keys [t boids] :as world}]
  (assoc e :histories 
         (apply hash-map 
                (mapcat #(vector (% :n) (updated-history e t %)) boids))))

(defn draw-trail [t]
  (let [len (count t)
        start-alpha 0.5
        alpha #(- start-alpha (* % (/ start-alpha len)))]
    (GL11/glLineWidth 5)
    (GL11/glBegin GL11/GL_LINE_STRIP)
    (doall (map-indexed (fn [i {:keys [s]}]
                          (GL11/glColor4f 1 1 1 (alpha i))
                          (GL11/glVertex2f (first s) (second s)))
                        t))
    (GL11/glEnd)))
   
(defmethod effect/draw id [e]
  (doall (map (fn [[_ history]] (draw-trail history)) (e :histories))))
