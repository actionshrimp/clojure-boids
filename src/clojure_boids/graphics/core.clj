(ns clojure-boids.graphics.core
  (:import [org.lwjgl.opengl GL11]))

(defn init []
  (GL11/glMatrixMode GL11/GL_PROJECTION)
  (GL11/glLoadIdentity)
  (GL11/glOrtho 0 800 0 600 1 -1)
  (GL11/glMatrixMode GL11/GL_MODELVIEW))

(defn clear []
  (GL11/glClear (bit-or GL11/GL_COLOR_BUFFER_BIT GL11/GL_DEPTH_BUFFER_BIT)))

(defn draw-square []
  (GL11/glBegin GL11/GL_TRIANGLES)
  (GL11/glColor3f 0.5 0.5 1.0)
  (GL11/glVertex2f 100 100)
  (GL11/glVertex2f 300 100)
  (GL11/glVertex2f 200 300)
  (GL11/glEnd))

(defn draw []
  (clear)
  (draw-square))
