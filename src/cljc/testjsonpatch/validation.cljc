(ns testjsonpatch.validation
  (:require [clojure.spec.alpha :as s]))

(s/def ::op string?)
(s/def ::value (s/or :val string?
                     :val number?
                     :val map?
                     :val vector?))
; (s/def ::value string?)
(s/def ::path (s/or :val string?
                    :val vector?))
(s/def ::from string?)

;(s/def ::patch string?)
(s/def ::patch
       (s/coll-of
         (s/keys :req-un [::op ::path]
                 :opt-un [::value ::from])))



(s/def ::source (s/nilable string?))
(s/def ::target (s/nilable string?))
