(ns clojure-boids.graphics.core
  (:import [org.lwjgl.opengl GL11]))

(defn init [world]
  (GL11/glMatrixMode GL11/GL_PROJECTION)
  (GL11/glLoadIdentity)
  (GL11/glOrtho 0 (world :width) 0 (world :height) 1 -1)
  (GL11/glMatrixMode GL11/GL_MODELVIEW))

(defn clear []
  (GL11/glClear (bit-or GL11/GL_COLOR_BUFFER_BIT GL11/GL_DEPTH_BUFFER_BIT)))

(defn draw-boid [{:keys [x y]}]
  (GL11/glPushMatrix)
  (GL11/glTranslatef x y 0)
  (GL11/glBegin GL11/GL_TRIANGLES)
  (GL11/glColor3f 0.5 0.5 1.0)
  (GL11/glVertex2f 0 0)
  (GL11/glVertex2f 10 0)
  (GL11/glVertex2f 5 10)
  (GL11/glEnd)
  (GL11/glPopMatrix))

(defn draw [world]
  (clear)
  (doall (map draw-boid (world :boids))))
