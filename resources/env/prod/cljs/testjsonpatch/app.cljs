(ns testjsonpatch.app
  (:require [testjsonpatch.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init! false)
