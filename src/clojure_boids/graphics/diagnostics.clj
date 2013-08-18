(ns clojure-boids.graphics.diagnostics
  (:import [org.lwjgl.opengl GL11]
           [java.awt Font]
           [org.newdawn.slick TrueTypeFont Color]))

(defn init []
  (def awt-font (Font. "Arial" Font/BOLD 12))
  (def font (TrueTypeFont. awt-font true)))

(defn draw-orienter []
  (do (GL11/glColor3f 1 1 1)
      (GL11/glBegin GL11/GL_LINES)
      (GL11/glVertex2f 0 0)
      (GL11/glVertex2f 50 50)
      (GL11/glEnd)))

(defn draw-avg-update-time [{:keys [dt-avg]}]
  (GL11/glEnable GL11/GL_TEXTURE_2D)
  (GL11/glDisable GL11/GL_DEPTH_TEST)
  (.drawString font 50 50 
               (str "dt-avg (ms): " (float (* (first dt-avg) 1000)))
               Color/white)
  (GL11/glEnable GL11/GL_DEPTH_TEST)
  (GL11/glDisable GL11/GL_TEXTURE_2D))

(defn draw-target-vec [[s-x s-y] [target-s-x target-s-y]]
  (do (GL11/glPushMatrix)
      (GL11/glColor3f 1.0 0 0)
      (GL11/glBegin GL11/GL_LINES)
      (GL11/glVertex2f s-x s-y)
      (GL11/glVertex2f target-s-x target-s-y)
      (GL11/glEnd)
      (GL11/glPopMatrix)))

(defn draw-boid-details [{:keys [s target-s]}]
  (draw-target-vec s target-s))

(defn draw [world]
  (draw-orienter)
  (doall (map draw-boid-details (world :boids)))
  (draw-avg-update-time world))
