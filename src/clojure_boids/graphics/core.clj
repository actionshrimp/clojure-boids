(ns clojure-boids.graphics.core
  (:import [java.lang.Math]
           [org.lwjgl.opengl GL11]))

(defn init [world]
  (GL11/glEnable GL11/GL_TEXTURE_2D)
  (GL11/glShadeModel GL11/GL_SMOOTH)
  (GL11/glEnable GL11/GL_BLEND)
  (GL11/glBlendFunc GL11/GL_SRC_ALPHA GL11/GL_ONE_MINUS_SRC_ALPHA)
  (GL11/glMatrixMode GL11/GL_PROJECTION)
  (GL11/glLoadIdentity)
  (GL11/glOrtho 0 (world :width) (world :height) 0 1 -1)
  (GL11/glMatrixMode GL11/GL_MODELVIEW))

(defn clear []
  (GL11/glClear (bit-or GL11/GL_COLOR_BUFFER_BIT GL11/GL_DEPTH_BUFFER_BIT)))

(defn draw-target-vec [[s-x s-y] [target-s-x target-s-y]]
  (GL11/glPushMatrix)
  (GL11/glColor3f 1.0 0 0)
  (GL11/glBegin GL11/GL_LINES)
  (GL11/glVertex2f s-x s-y)
  (GL11/glVertex2f target-s-x target-s-y)
  (GL11/glEnd)
  (GL11/glPopMatrix))

(defn draw-boid [{:keys [s target-s a]}]
  (let [[x y] s]
    (draw-target-vec s target-s)
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

(defn draw-orienter []
  (GL11/glColor3f 1 1 1)
  (GL11/glBegin GL11/GL_LINES)
  (GL11/glVertex2f 0 0)
  (GL11/glVertex2f 50 50)
  (GL11/glEnd))

(defn draw [world]
  (clear)
  (draw-orienter)
  (draw-world world))
