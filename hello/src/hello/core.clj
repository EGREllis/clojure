(ns hello.core
  (:gen-class))

(defn cli-prompt [prompt-text] 
  (let [_ (println prompt-text)]
    (read-line)))

(defn -main
  [& args]
  (let [name (cli-prompt "Please enter your name:")] 
       (println (str "Hello, " name "!"))))

