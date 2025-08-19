(ns vc.r.web.controllers.todo
  (:require [vc.r.web.controllers.components :as c]
            [vc.r.web.xtdb2 :as xt2]
            [ring.util.http-response :refer [content-type ok created]]))

(defn home
  [{:keys [db.xtdb2/node]}
   {:keys [anti-forgery-token]}]
  (-> (c/main-page (xt2/list-todos node) anti-forgery-token)
      (ok)
      (content-type "text/html; charset=utf-8")))

(defn delete-item
  [{:keys [db.xtdb2/node]}
   {:keys [path-params]}]
  #_(xt2/delete-todo node (:id path-params))
  (ok))

(defn add-item
  [{:keys [db.xtdb2/node]}
   {:keys [anti-forgery-token form-params]}]
  (let [title (get form-params "value")
        completed false
        new-todo-id (xt2/add-todo node title completed)]
    (ok (c/item-row {:xt/id new-todo-id :title title :completed completed}
                    anti-forgery-token))))

(comment
  (require '[integrant.repl.state :as state]
           '[vc.r.web.xtdb2 :as xtdb2])
  (home state/system "")
  (def node (:db.xtdb2/node state/system))
  (xtdb2/list-todos node)

  (add-item state/system {:form-params {:value "abc"}})
  (c/item-row {:xt/id "123" :title "abc" :completed false}
                    "abc")


  )
