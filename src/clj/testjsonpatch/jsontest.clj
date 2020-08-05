(ns testjsonpatch.jsontest
  (:require [clojure.spec.alpha :as s]
            [testjsonpatch.validation :as val]))

; (def patchmessage
;   [
;    {:op "string"
;     :value "string",
;     :path "string",
;     :from "string"}])

(defn patch-valid
  [patch]
  (if-not (s/valid? ::val/patch (:body patch))
    (s/explain ::val/patch (:body patch))
    (:body patch)))

; (patch-valid? patchmessage)
;
; (s/valid? ::val/patch patchmessage)
; (s/explain ::val/patch patchmessage)
; (s/explain-data ::val/patch patchmessage)
; (def tpatch {:path /CO, :value 2, :op replace})
;
; (s/explain ::val/patch [{:path "/CO", :value 2.0, :op "replace"}])
; (string? 2.0)
