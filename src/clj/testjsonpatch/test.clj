(ns testjsonpatch.test
  (:require [testjsonpatch.patch.core :as patch]))


(def obj1 {:test "Foud"})
(def obj2 {:test "Fout"})
;(.parse js/JSON obj1)
(patch/diff obj1 obj2)
