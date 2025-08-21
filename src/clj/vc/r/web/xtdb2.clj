(ns vc.r.web.xtdb2
  (:require [xtdb.api :as xt]
            [xtdb.node :as xtn]
            [integrant.core :as ig]))

(defmethod ig/init-key :db.xtdb2/node
  [_ config]
  (xtn/start-node config))

(defmethod ig/halt-key! :db.xtdb2/node
  [_ xtdb-node]
  (.close xtdb-node))

(defmethod ig/init-key :db.sql/connection
  [_ config])

(defmethod ig/halt-key! :db.sql/connection
  [_ _])


(defn list-todos
  "Lists all TODO items from the XTDB database.
   Returns a vector of TODO items with their IDs and details."
  [node]
  (xt/q node "select * from items"))

(defn add-todo
  "Adds a new TODO item to the XTDB database.
   Returns the ID of the created TODO item."
  [node title completed]
  (let [todo-id (random-uuid)]
    (xt/submit-tx node [["insert into items (_id, title, completed) values (?, ?, ?)"
                         todo-id title completed]])
    todo-id))
