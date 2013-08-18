(ns clojure-boids.util.vec)

(defn dot [v1 v2]
  (apply + (doall (map * v1 v2))))

(defn normalize [v]
  (let [mag (Math/sqrt (dot v v))]
    (doall (map #(/ % mag) v))))

(defn mag [v]
  (Math/sqrt (dot v v)))

(defn mag-bounded [max-mag v]
  (let [m (mag v)]
    (if (> m max-mag)
      (normalize v) v)))

(defn add [& vs]
  (if (> (count vs) 1)
    (doall (apply (partial map +) vs))
    (first vs)))

(defn sub [& vs]
  (if (> (count vs) 1)
    (doall (apply (partial map -) vs))
    (first vs)))

(defn scale [s & vs]
  (doall (apply (partial map #(* s %)) vs)))
