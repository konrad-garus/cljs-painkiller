(ns painkiller.macros
  (:require [clojure.string :refer [join split]]))

(defmacro obj-fn [fn-args & body]
  `(fn ~fn-args (~'this-as ~'this ~@body)))

(defmacro set-obj-fn [fn-path fn-args & body]
  (let [name-form (str "(.. " (join " -" (split (str fn-path) #"\.")) ")")]
    `(set! ~(read-string name-form)
           (obj-fn ~fn-args ~@body))))

