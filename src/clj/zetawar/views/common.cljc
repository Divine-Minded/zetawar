(ns zetawar.views.common
  #?@(
      :clj [(:require
              [clojure.pprint :refer [pprint]]
              [clojure.java.io :as io]
              [hiccup.page :refer [html5 include-css include-js]])]
      :cljs [(:require
               [cljsjs.react-bootstrap])]
      )
  )

#?(:clj
    (do

      (defn ga [tracking-id]
        [:script
         (str "(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){"
              "(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),"
              "m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)"
              "})(window,document,'script','https://www.google-analytics.com/analytics.js','ga');"
              "ga('create', '" tracking-id "', 'auto');"
              "ga('send', 'pageview');")])

      (defn rollbar [access-token environment]
        [:script
         (str "var _rollbarConfig={"
              "accessToken:'" access-token "',"
              "captureUncaught:true,"
              "captureUnhandledRejections:false,"
              "payload:{"
              "environment: '" environment "'"
              "}"
              "};"
              (-> (io/resource "js/vendor/rollbar-snippet.js") io/file slurp))])

      (defn head [{global-meta :meta :as data} title]
        [:head
         [:meta {:charset "utf-8"}]
         [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge"}]
         [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
         [:title title]
         (include-css "/css/main.css")
         (some-> (:google-analytics-tracking-id global-meta)
                 ga)
         (some-> (:rollbar-access-token global-meta)
                 (rollbar (:rollbar-environment global-meta)))])

      ))

(def nav-links
  [{:href "/"        :title "Game"}
   {:href "/blog"    :title "Blog"}
   {:href "/roadmap" :title "Roadmap"}
   {:href "/backers" :title "Backers"}])

(defn navbar
  ([] (navbar nil))
  ([active-title]
   #?(
      :clj
      [:div#navbar-wrapper {:data-active-title active-title}
       [:nav.navbar.navbar-inverse.navbar-fixed-top
        [:div.container
         [:div.navbar-header
          [:a.navbar-brand {:href "/"}
           [:img {:src "/images/navbar-logo.svg"}]
           "Zetawar"]]
         [:div#navbar-collapse.collapse.navbar-collapse
          (into [:ul.nav.navbar-nav ]
                (for [{:keys [href title]} nav-links]
                  (if (= title active-title)
                    [:li {:class "active"} [:a {:href href} title]]
                    [:li [:a {:href href} title]])))]]]]
      :cljs
      [:> js/ReactBootstrap.Navbar {:fixed-top true :inverse true}
       [:> js/ReactBootstrap.Navbar.Header
        [:> js/ReactBootstrap.Navbar.Brand
         [:a {:href "/"}
          [:img {:src "/images/navbar-logo.svg"}]
          "Zetawar"]]
        [:> js/ReactBootstrap.Navbar.Toggle]]
       [:> js/ReactBootstrap.Navbar.Collapse
        (into [:> js/ReactBootstrap.Nav]
              (map-indexed (fn [idx {:keys [href title]}]
                             (let [active (= title active-title)]
                               [:> js/ReactBootstrap.NavItem {:event-key idx :active active :href href}
                                title]))
                           nav-links))]]
      )
   ))

(defn kickstarter-alert []
  [:div.row
   [:div.col-md-12
    [:div.alert.alert-success
     [:strong
      "Zetawar is a work in progress. Follow "
      [:a {:href "https://twitter.com/ZetawarGame"}
       "@ZetawarGame"]
      " and check out the "
      [:a {:href "https://www.kickstarter.com/projects/djwhitt/zetawar/updates"}
       "Zetawar Kickstarter page"]
      " for updates."]]]])

(defn footer []
  [:div.container
   [:div#footer
    [:p
     "Follow "
     [:a {:href "https://twitter.com/ZetawarGame"} "@ZetawarGame"]
     " for updates. "
     "Questions or comments? Send us some "
     [:a {:href "http://goo.gl/forms/RgTpkCYDBk"} "feedback"]]
    [:p
     "Copyright 2016 Arugaba LLC, All Rights Reserved."]
    [:p
     "Artwork from "
     [:a {:href "https://github.com/cvincent/elite-command"} "Elite Command"]
     " Copyright 2015 Chris Vincent under "
     [:a {:href "http://creativecommons.org/licenses/by/4.0/"}
      "Creative Commons Attribution 4.0 International License"]]]])
