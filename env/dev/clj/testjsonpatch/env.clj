(ns testjsonpatch.env
  (:require
    [selmer.parser :as parser]
    [clojure.tools.logging :as log]
    [testjsonpatch.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[testjsonpatch started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[testjsonpatch has shut down successfully]=-"))
   :middleware wrap-dev})
