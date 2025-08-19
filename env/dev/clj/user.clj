(ns user
  "Userspace functions you can run by default in your local REPL."
  (:require
   [clojure.pprint]
   [clojure.spec.alpha :as s]
   [clojure.tools.namespace.repl :as repl]
   [criterium.core :as c]                                  ;; benchmarking
   [expound.alpha :as expound]
   [integrant.core :as ig]
   [integrant.repl :refer [clear go halt prep init reset reset-all]]
   [integrant.repl.state :as state]
   [kit.api :as kit]
   [lambdaisland.classpath.watch-deps :as watch-deps]      ;; hot loading for deps
   [vc.r.core :refer [start-app]]
   [clojure.string :as str]))

;; uncomment to enable hot loading for deps
(watch-deps/start! {:aliases [:dev :test]})

(alter-var-root #'s/*explain-out* (constantly expound/printer))

(add-tap (bound-fn* clojure.pprint/pprint))

(defn dev-prep!
  []
  (integrant.repl/set-prep! (fn []
                              (-> (vc.r.config/system-config {:profile :dev})
                                  (ig/expand)))))

(defn test-prep!
  []
  (integrant.repl/set-prep! (fn []
                              (-> (vc.r.config/system-config {:profile :test})
                                  (ig/expand)))))

;; Can change this to test-prep! if want to run tests as the test profile in your repl
;; You can run tests in the dev profile, too, but there are some differences between
;; the two profiles.
(dev-prep!)

(repl/set-refresh-dirs "src/clj")

(def refresh repl/refresh)



(comment
  (go)
  (reset))

(comment

  (require '[xtdb.api :as xt]
           '[xtdb.node :as xtn]
           '[vc.r.web.xtdb2 :as xtdb2])
  (def node (:db.xtdb2/node state/system))

  (node-status {:db.xtdb2/node node} {})

  (xt/q node "select * from items")

  (xtdb2/list-todos node)
  ;; => [{:xt/id 2, :completed true, :title "buy carrots"}]

  (xtdb2/add-todo node "change socks" false)

  (xt/submit-tx node [["INSERT INTO items (_id, title, completed) VALUES (2, 'buy carrots', ?)"
                       true]])
  )
