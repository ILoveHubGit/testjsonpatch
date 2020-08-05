(ns testjsonpatch.view
  (:require
    [kee-frame.core :as kf]
    [markdown.core :refer [md->html]]
    [reagent.core :as r]
    [re-frame.core :as rf]
    [testjsonpatch.forms.controls :as forms]
    [testjsonpatch.validation :as val]))

(defn nav-link [title page]
  [:a.navbar-item
   {:href   (kf/path-for [page])
    :class (when (= page @(rf/subscribe [:nav/page])) "is-active")}
   title])

(defn navbar []
  (r/with-let [expanded? (r/atom false)]
    [:nav.navbar.is-info>div.container
     [:div.navbar-brand
      [:a.navbar-item {:href "/" :style {:font-weight :bold}} "testjsonpatch"]
      [:span.navbar-burger.burger
       {:data-target :nav-menu
        :on-click #(swap! expanded? not)
        :class (when @expanded? :is-active)}
       [:span][:span][:span]]]
     [:div#nav-menu.navbar-menu
      {:class (when @expanded? :is-active)}
      [:div.navbar-start
       [nav-link "Home" :home]
       [nav-link "About" :about]]]]))

(defn about-page []
  [:section.section>div.container>div.content
   [:img {:src "/img/warning_clojure.png"}]])

(defn home-page []
  [:section.section>div.container>div.content
   [:div "JSON Patch builder online"
     [:div.ui.form
      [:div.ui.section
       [:div.box.wrappertop
        [:div.box.datatop "Source (current state)"
         [:div.info [forms/textarea-input :jsonpatch [:source] ::val/source
                     "Should be valid JSON"
                     {:label "Source" :field-classes ["required"]}]]]
        [:div.box.datatop "Target (current state)"
         [:div.info [forms/textarea-input :jsonpatch [:target] ::val/target
                     "Should be valid JSON"
                     {:label "Target" :field-classes ["required"]}]]]]
       [:div.box
        [:div.info
         [:button.btn.btn-danger.center
          {:on-click #(rf/dispatch [:createpatch])}
          "Click to build the Patch document"]
         [:button.btn.btn-danger.center
          {:on-click #(rf/dispatch [:applypatch])}
          "Apply Patch to source document"]]]
       [:div.box "JSON Patch message"
        [:div.info [forms/textarea-input :jsonpatch [:createpatch] string?
                    "Should be valid JSON-Patch"
                    {:label "Patch" :field-classes ["required"]}]]]]]]])

(defn root-component []
  [:div
   [navbar]
   [kf/switch-route (fn [route] (get-in route [:data :name]))
    :home home-page
    :about about-page
    nil [:div ""]]])
