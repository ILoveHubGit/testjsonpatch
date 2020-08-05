(ns testjsonpatch.core
  (:require
    [kee-frame.core :as kf]
    [re-frame.core :as rf]
    [ajax.core :as http]
    [testjsonpatch.ajax :as ajax]
    [testjsonpatch.routing :as routing]
    [testjsonpatch.view :as view]
    [testjsonpatch.forms.core :as forms]
    [clj-json-patch.core :as patch]))
;    [testjsonpatch.patch.core :as patch]))

(def log (.-log js/console))

; (def obj1 "{\"Message\": \"\", \"twee\": \"2\"}")
; (def obj2 "{\"Message\": \"Hello World!\", \"twee\":  [{\"2\": 2}]}")
; ;
; ; (.stringify js/JSON (clj->js (js->clj (.parse js/JSON obj2) :keywordize true)))
; (def prefix "/")


; (clj->js (patch/diff obj1 obj2))
; (patch/diff obj1 obj2)
;; -------------------------
;;      JSON Patch
;; -------------------------
(rf/reg-sub
  :createpatch
  (fn [db _]
     (:createpatch db)))

; (def obj1 "{
;   \"popup\": {
;     \"menuitem\": [
;       {\"value\": \"New\", \"onclick\": \"CreateNewDoc()\"},
;       {\"value\": \"Open\", \"onclick\": \"OpenDoc()\"},
;       {\"value\": \"Close\", \"onclick\": \"CloseDoc()\"}
;     ]
;   }
; }")
; (def obj2 "{
;   \"popup\": {
;     \"menuitem\": [
;       {\"value\": \"New\", \"onclick\": \"CreateNewDoc()\"},
;       {\"value\": \"Open\", \"onclick\": \"OpenDoc()\"},
;       {\"value\": \"Close\", \"onclick\": \"CloseDoc()\"},
;       {\"value\": \"Delete\", \"onclick\": \"DeleteDoc()\"}
;     ]
;   }
; }")
; (def patch1 "[{\"op\": \"add\",
;                \"path\": \"/popup/menuitem/3\",
;                \"value\": {\"value\": \"Delete\", \"onclick\": \"DeleteDoc()\"}}]")
;
;
;
;
;
; (log "Obj1: " obj1)
; (patch/diff obj1 obj2)
; (patch/patch obj1 patch1)

(kf/reg-chain
  :createpatch
  (fn [{:keys [db]} _]
     {:db (dissoc db :docs :errors :createpatch)})
  (fn [{:keys [db]} _]
    (let [form (get-in db (concat forms/value-db-path [:jsonpatch]))]
      {:http-xhrio {:method           :patch
                    :uri              "/api/patch"
                    :params           (patch/diff (:source form) (:target form))
                    :format           (http/json-request-format)
                    :headers          {"Accept" "application/json"}
                    :response-format  (http/json-response-format {:keywords? true})
                    :on-failure       [:common/set-error]}}))
  (fn [{:keys [db]} result]
    (let [form (get-in db (concat forms/value-db-path [:jsonpatch]))]
      {:db (assoc-in db
                     (vec (concat forms/value-db-path [:jsonpatch] [:createpatch]))
                     (if (first result)
                       (.stringify js/JSON (clj->js (first result)))
                       result))})))

(kf/reg-chain
  :applypatch
  (fn [{:keys [db]} _]
    {:db (dissoc db :docs :errors (vec (concat forms/value-db-path [:jsonpatch] [:target])))})
  (fn [{:keys [db]} _]
    (let [form (get-in db (concat forms/value-db-path [:jsonpatch]))]
      {:db (assoc-in db
                     (vec (concat forms/value-db-path [:jsonpatch] [:target]))
                     (.stringify js/JSON (clj->js (patch/patch (:source form) (:createpatch form)))))})))

(rf/reg-event-fx
  ::load-about-page
  (constantly nil))

(kf/reg-controller
  ::about-controller
  {:params (constantly true)
   :start  [::load-about-page]})

(rf/reg-sub
  :docs
  (fn [db _]
    (:docs db)))

(kf/reg-chain
  ::load-home-page
  (fn [_ _]
    {:http-xhrio {:method          :get
                  :uri             "/docs"
                  :response-format (http/raw-response-format)
                  :on-failure      [:common/set-error]}})
  (fn [{:keys [db]} [_ docs]]
    {:db (assoc db :docs docs)}))


(kf/reg-controller
  ::home-controller
  {:params (constantly true)
   :start  [::load-home-page]})

;; -------------------------
;; Initialize app
(defn ^:dev/after-load mount-components
  ([] (mount-components true))
  ([debug?]
   (rf/clear-subscription-cache!)
   (kf/start! {:debug?         (boolean debug?)
               :routes         routing/routes
               :hash-routing?  true
               :initial-db     {}
               :root-component [view/root-component]})))

(defn init! [debug?]
  (ajax/load-interceptors!)
  (mount-components debug?))
