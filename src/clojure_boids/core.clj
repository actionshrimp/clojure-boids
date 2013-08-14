(ns clojure-boids.core
  (:require [clojure-boids.graphics.core :as graphics])
  (:import [org.lwjgl.opengl Display DisplayMode]))

(defn init []
  (let [display-mode (DisplayMode. 800 600)]
    (Display/setDisplayMode display-mode)
    (Display/create)
    (graphics/init)))

(defn cleanup []
  (Display/destroy)
  (prn "Bye!"))

(defn main-loop []
  (if (not (Display/isCloseRequested))
    (do
      (graphics/draw)
      (Display/update)
      (recur))))

(defn start-game [] 
  (init)
  (main-loop)
  (cleanup))

(defn -main [] 
  (.start (Thread. start-game)))
