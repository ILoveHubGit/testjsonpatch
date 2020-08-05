(ns testjsonpatch.core-test
  (:require [cljs.test :refer-macros [is deftest testing run-tests]]
            [pjstadig.humane-test-output]
            [reagent.core :as reagent :refer [atom]]
            [testjsonpatch.core :as rc]))

(deftest test-home
  (is (= true true)))
