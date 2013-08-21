(ns clojure-boids.graphics.core
  (:require [clojure-boids.graphics.diagnostics :as diagnostics]
            [clojure-boids.graphics.effects.core :as effect]
            [clojure-boids.graphics.effects.boid-trail :as boid-trail])
  (:import [java.lang.Math]
           [org.lwjgl.opengl GL11]))

(defn init-effects [world]
  (let [effects-list [{:effect boid-trail/id :length 50 :histories {}}]]
    (def effects (map #(effect/init % world) effects-list))))

(defn init [world diagnostics?]
  (init-effects world)
  (GL11/glShadeModel GL11/GL_SMOOTH)
  (GL11/glEnable GL11/GL_BLEND)
  (GL11/glBlendFunc GL11/GL_SRC_ALPHA GL11/GL_ONE_MINUS_SRC_ALPHA)
  (GL11/glMatrixMode GL11/GL_PROJECTION)
  (GL11/glLoadIdentity)
  (GL11/glOrtho 0 (world :width) (world :height) 0 1 -1)
  (GL11/glMatrixMode GL11/GL_MODELVIEW)
  (if diagnostics? (diagnostics/init)))

(defn clear []
  (GL11/glClear (bit-or GL11/GL_COLOR_BUFFER_BIT GL11/GL_DEPTH_BUFFER_BIT)))

(defn draw-boid [{:keys [n s a]}]
  (let [[x y] s]
    (GL11/glPushMatrix)
    (GL11/glTranslatef x y 0)
    (GL11/glRotatef (Math/toDegrees a) 0 0 -1)
    (GL11/glBegin GL11/GL_TRIANGLES)
    (GL11/glColor3f 0.5 0.5 1.0)
    (GL11/glVertex2f -5 -5)
    (GL11/glVertex2f 5 -5)
    (GL11/glVertex2f 0 5)
    (GL11/glEnd)
    (GL11/glPopMatrix)))

(defn draw-world [world]
  (doall (map draw-boid (world :boids))))

(defn render [world diagnostics?]
  (def effects (doall (map #(effect/update % world) effects)))
  (clear)
  (doall (map effect/draw effects))
  (draw-world world)
  (if diagnostics? (diagnostics/draw world)))
