(ns monkey-game.core
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(defn setup []
  (q/frame-rate 30)
  {:images
   {:monkey (q/load-image "resources/monkey.png")
    :man    (q/load-image "resources/man.png")
    :bush   (q/load-image "resources/bush.png")
    :banana (q/load-image "resources/banana.png")}})

(defn update-bananas [bs]
  (cond
    (nil? bs)
    [{:x -100 :y (/ (q/width) 4) :angle 0}]
    (seq bs)
    (->> bs
         (remove #(<= (q/width) (:x %)))
         (map (fn [b]
                (-> b
                    (update :angle #(mod (+ 0.2 %) 360))
                    (update :x #(min (q/width)
                                     (+ % (rand-int 6))))
                    (update :y #(min (q/height)
                                     (+ % (rand-int 3)))))))
         (seq))))

(defn update-state [state]
  (update state :bananas update-bananas))

(defn draw-state [state]
  (q/background 240)
  (q/fill 0 255 255)
  (q/image-mode :center)
  (q/with-translation [(/ (q/width) 2)
                       (/ (q/height) 2)]
    (q/image (get-in state [:images :man]) 0 0 335 480)
    (q/image (get-in state [:images :bush]) 0 0)
    (q/image (get-in state [:images :monkey]) 0 (/ (q/height) 4)))
  (doseq [{:keys [x y angle]} (:bananas state)]
    (q/push-matrix)
    (q/translate x y)
    (q/rotate angle)
    (q/image (get-in state [:images :banana]) 0 0 100 100)
    (q/pop-matrix)))

(comment
  (q/defsketch monkey-game
    :title "Hop Monkey"
    :size [500 500]
    :setup setup
    :update update-state
    :draw draw-state
    :features [:keep-on-top]
    :middleware [m/fun-mode]))
