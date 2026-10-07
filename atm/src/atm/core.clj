(ns atm.core
  "Documentation text here"
  (:require [clojure.set :as s])
  (:gen-class))

(defn cli-prompt [prompt-text] 
  (let [_ (println (str prompt-text "\n"))]
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
      (if (= nil (first matcher))
        (recur text)
        (Integer/parseInt matcher))
    )))

;; can not change pin
(defn update-accounts [accounts new-entry]
  (let [current-entry (account-for-pin accounts (str (:pin new-entry)))]
    (disj (conj accounts new-entry) current-entry))
  )

(defn loop-menu-option [accounts account]
  (loop [all-accounts accounts
         current-account account]
    (let [option (cli-menu-option "Please enter an option between 1-4:\n\t1) Get balance\n\t2) Withdraw\n\t3) Deposit\n\t4) Exit" #"[1234]")]
      (cond (== option 1) (let [_ (print (str (:first-name current-account) " your balance is " (:balance current-account) "\n"))]
                            (recur all-accounts current-account))
            (== option 2) (let [updated-account (update current-account :balance (partial + (* -1 (cli-prompt-for-amount "Please enter the amount you wish to withdraw: "))))
                                updated-accounts (update-accounts all-accounts updated-account)]
                            (recur updated-accounts updated-account))
            (== option 3) (let [updated-account (update current-account :balance (partial + (cli-prompt-for-amount "Please enter the amount you wish to deposit: ")))
                                updated-accounts (update-accounts all-accounts updated-account)]
                            (recur updated-accounts updated-account))
            (== option 4) (System/exit 0))
      )
    )
  )

(defn loop-ask-for-pin [pin, accounts]
  (loop [next-pin pin
         next-accounts accounts]
    (let [next-account (cli-get-account next-accounts "Please enter your pin: ")]
      (cond (nil? next-account) (recur next-pin next-accounts)
            (not (nil? next-account)) (let [_ (loop-menu-option next-accounts next-account)]
                                        (recur next-pin next-accounts))
            )
      )
    ))

;; Read and write accounts to disk? on load and terminate
(defn -main
  [& args]
  (let [accounts #{{:first-name "Ashraf" :balance 1000 :pin 1234}
                    {:first-name "Alex"   :balance 2000 :pin 4321}
                    {:first-name "Edward" :balance 0    :pin 2332}}]
    (loop-ask-for-pin nil accounts)
    ))

