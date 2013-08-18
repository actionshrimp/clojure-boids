(ns clojure-boids.util.vec)

(defn dot [v1 v2]
  (apply + (doall (map * v1 v2))))

(defn normalize [v]
  (let [mag (Math/sqrt (dot v v))]
    (doall (map #(/ % mag) v))))

(defn mag-bounded [max-mag v]
  (let [mag (Math/sqrt (dot v v))]
    (if (> mag max-mag)
      (normalize v) v)))

(defn add [v1 v2]
  (doall (map + v1 v2)))

(defn sub [v1 v2]
  (doall (map - v1 v2)))

(defn scale [s v1]
  (doall (map #(* s %) v1)))

(defn mag [v]
  (dot v v))
