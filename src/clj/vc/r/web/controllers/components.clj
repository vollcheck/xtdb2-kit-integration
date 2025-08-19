(ns vc.r.web.controllers.components
  (:require [hiccup.core :as h]
            [garden.core :as g]))

(defn item-row [{:keys [title completed]} aft]
  (h/html
      [:tr
       [:td title]
       [:td (if completed "completed" "not completed")]
       [:td
        [:button #_{:hx-post (str "/delete/" k)
                  :hx-target "closest tr"
                  :hx-swap "outerHTML swap:1s"
                  :hx-headers (format "{\"X-CSRF-Token\": \"%s\"}" aft)}
         "delete"]]]))

(def htmx-swapping-style
  (g/css
   [:tr.htmx-swapping
    [:td
     {:opacity 0
      :transition "opacity 1s ease-out"}]]))

(defn main-page [items aft]
  (h/html
      [:html
       [:head
        [:title "Kit + XTDB2 + HTMX"]
        [:script {:src "https://unpkg.com/htmx.org@2.0.2"}]
        [:style htmx-swapping-style]]
       [:body
        [:section
         [:div.content
          [:h2 "your list"]
          [:form {:hx-post "/add"
                  :hx-target "#table-body"
                  :hx-swap "beforeend"
                  :hx-headers (format "{\"X-CSRF-Token\": \"%s\"}" aft)
                  :hx-trigger "change from:body"}
           [:input {:type "text"
                    :name "value"
                    :hx-trigger "change"}]
           [:button {:type "submit"} "add new item"]]
          [:table#item-list
           [:thead
            [:tr
             [:th "item"]
             [:th "status"]
             [:th "actions"]]]
           [:tbody#table-body
            (for [item items]
              (item-row item aft))]]]]]]))

(comment
  (main-page [{:xt/id 2, :completed true, :title "buy carrots"}] "")
  )
