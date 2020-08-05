(ns testjsonpatch.forms.controls
  (:require [re-frame.core :as rf]
            [testjsonpatch.forms.core :as forms]
            [cljs.spec.alpha :as s]))

(defn text-input
  [form-id field-path field-spec validation-error-msg
   {:keys [label field-classes] :as options}]
  (let [field-value @(rf/subscribe [::forms/field-value form-id field-path])
        field-valid? (s/valid? field-spec field-value)]
    [:div.field
     {:class (cond->>`  (or field-classes (list))
                 (not field-valid?) (cons "error")
                 true (clojure.string/join \space))}
     [:div.box.info
      (when label [:b label])
      [:div.right_ab
       [:input
        {:type      "text"
         :value     field-value
         :on-change #(rf/dispatch [::forms/set-text-field-value form-id field-path (-> % .-target .-value)])}]]
      (when-not field-valid? [:div.error validation-error-msg])]]))

(defn textarea-input
  [form-id field-path field-spec validation-error-msg
   {:keys [label field-classes] :as options}]
  (let [field-value @(rf/subscribe [::forms/field-value form-id field-path])
        field-valid? (s/valid? field-spec field-value)]
    [:div.field
     {:class (cond->>`  (or field-classes (list))
                 (not field-valid?) (cons "error")
                 true (clojure.string/join \space))}
     [:div.box.info
      (when label [:b label])
      [:div.right_ab
       [:textarea
        {:value     field-value
         :rows      10
         :cols      30
         :on-change #(rf/dispatch [::forms/set-text-field-value form-id field-path (-> % .-target .-value)])}]]
      (when-not field-valid? [:div.error validation-error-msg])]]))

(defn number-input
  [form-id field-path field-spec validation-error-msg
   {:keys [label field-classes] :as options} type]
  (let [field-value    @(rf/subscribe [::forms/field-value form-id field-path])
        field-valid? (s/valid? field-spec
                      (case type
                              :int (if (= (js/parseInt field-value) (js/parseFloat field-value)) (js/parseInt field-value) field-value)
                              :float (js/parseFloat field-value)
                              0))]
    [:div.field
     {:class (cond->>`  (or field-classes (list))
                 (not field-valid?) (cons "error")
                 true (clojure.string/join \space))}
     [:div.box.info
      (when label [:b label])
      [:div.right_ab
       [:input
        {:type      "text"
         :value     field-value
         :on-change #(rf/dispatch [::forms/set-text-field-value form-id field-path (-> % .-target .-value) type])}]]
      (when-not field-valid? [:div.error validation-error-msg])]]))

(defn acquirer-list
  [acquirer]
  [:option {:value (:acquirerid acquirer)} (:acquirername acquirer)])

(defn acquirer-dropdown
  [form-id field-path
   {:keys [label field-classes] :as options}]
  (let [field-value (cons {:acquirerid "" :acquirername "Select an acquirer"} @(rf/subscribe [::forms/field-value form-id field-path]))]
    [:div.field
     {:class (cond->> (or field-classes (list)))}
     [:div.box.info
      (when label label)
      [:select
           {:on-change #(rf/dispatch [::forms/set-acquirer-value form-id field-path (-> % .-target .-value)])}
           (map acquirer-list field-value)]]]))
