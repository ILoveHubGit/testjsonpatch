(ns testjsonpatch.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[testjsonpatch started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[testjsonpatch has shut down successfully]=-"))
   :middleware identity})
