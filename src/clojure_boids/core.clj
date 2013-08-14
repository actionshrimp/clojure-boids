(ns clojure-boids.core
  (:import [org.lwjgl.opengl Display DisplayMode]))

(defn init [] 
  (let [display-mode (DisplayMode. 800 600)]
    (Display/setDisplayMode display-mode)
    (Display/create)))

(defn draw [] ())

(defn cleanup []
  (Display/destroy)
  (prn "Bye!"))

(defn main-loop []
  (if (not (Display/isCloseRequested))
    (do
      (draw)
      (recur))
    (cleanup)))

(defn -main []
  (init)
  (.start (Thread. main-loop)))
