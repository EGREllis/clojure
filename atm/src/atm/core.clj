(ns atm.core
  "Documentation text here"
  (:require [clojure.set :as s])
  (:import (java.util.regex Pattern)))

(def accounts #{
  {:first-name "Ashraf" :balance 1000 :pin 1234}
  {:first-name "Alex"   :balance 2000 :pin 4321}
  {:first-name "Edward" :balance 0    :pin 2332}})

(defn cli-prompt [prompt-text] 
  (let [_ (println prompt-text)]
    (read-line)))

(defn cli-menu-option [menu-text menu-regex]
  (loop [text menu-text
         regex menu-regex]
    (let
         [input-value (cli-prompt text)
         matcher (re-matches regex input-value)]
      (if (= nil (first matcher))
        (recur text regex)
        matcher
        ))))

(defn account-for-pin [pin]
  (s/select (fn [acc] (== (:pin acc) (Integer/parseInt pin)))
            accounts))

(defn -main
  [& args]
  (let [pin (cli-prompt "Enter your pin: ")
        account (first (account-for-pin pin))]
    (print (cli-menu-option "Please enter an option between 1-4:\n\t1) Get balance\n\t2) Withdraw\n\t3) Deposit\n\t4) Exit\n" #"[1234]"))
  ))

