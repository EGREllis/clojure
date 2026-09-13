(ns atm.core
  "Documentation text here"
  (:require [clojure.set :as s]))

(defn cli-prompt [prompt-text] 
  (let [_ (println prompt-text)]
    (read-line)))

(defn account-for-pin [accounts pin]
  (first (s/select (fn [acc] (== (:pin acc) (Integer/parseInt pin))) accounts)))

(defn cli-get-account [accounts pin-prompt]
  (loop [prompt pin-prompt]
    (let [user-pin (cli-prompt pin-prompt)
          acc (account-for-pin accounts user-pin)]
      (if (= nil (:pin acc))
          (recur prompt)
          acc)
    )))

(defn cli-menu-option [menu-text menu-regex]
  (loop [text menu-text
         regex menu-regex]
    (let
         [input-value (cli-prompt text)
         matcher (re-matches regex input-value)]
      (if (= nil (first matcher))
        (recur text regex)
        (Integer/parseInt matcher)
        ))))

(defn cli-prompt-for-amount [prompt]
  (loop [text prompt]
    (let [user-amount (cli-prompt text)
          matcher (re-matches #"[\d]+" user-amount)]
      (Integer/parseInt matcher))
    ))

;; can not change pin
(defn update-accounts [accounts new-entry]
  (let [current-entry (account-for-pin accounts (str (:pin new-entry)))]
    (disj (conj accounts new-entry) current-entry))
  )

(defn -main
  [& args]
  (loop [accounts #{{:first-name "Ashraf" :balance 1000 :pin 1234}
                    {:first-name "Alex"   :balance 2000 :pin 4321}
                    {:first-name "Edward" :balance 0    :pin 2332}}]
    (let [account (cli-get-account accounts "Please enter your pin: ")
        option (cli-menu-option "Please enter an option between 1-4:\n\t1) Get balance\n\t2) Withdraw\n\t3) Deposit\n\t4) Exit\n" #"[1234]")]
      (cond (== option 1) (print (str (:first-name account) " your balance is " (:balance account)))
            (== option 2) (recur (update-accounts accounts (update account :balance (partial - (cli-prompt-for-amount "Please enter the amount you wish to withdraw: ")))))
            (== option 3) (recur (update-accounts accounts (update account :balance (partial + (cli-prompt-for-amount "Please enter the amount you wish to deposit: ")))))
            (== option 4) (System/exit 0))
    )))

