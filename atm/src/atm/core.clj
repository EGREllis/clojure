(ns atm.core
  "Documentation text here"
  (:require [clojure.set :as s]))

(def accounts #{
  {:first-name "Ashraf" :balance 1000 :pin 1234}
  {:first-name "Alex"   :balance 2000 :pin 4321}
  {:first-name "Edward" :balance 0    :pin 2332}})

(defn cli-prompt [prompt-text] 
  (let [_ (println prompt-text)]
    (read-line)))

(defn account-for-pin [pin]
  (s/select (fn [acc] (== (:pin acc) (Integer/parseInt pin)))
            accounts))

(defn -main
  [& args]
  (let [pin (cli-prompt "Enter your pin: ")]
    (print "\n" (account-for-pin pin))))

