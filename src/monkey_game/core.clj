(ns monkey-game.core
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [clojure.java.io :as io]))

(defn setup []
  ; Set frame rate to 30 frames per second.
  (q/frame-rate 30)
  ; Set color mode to HSB (HSV) instead of default RGB.
  (q/color-mode :hsb)
  ; setup function returns initial state. It contains
  ; circle color and position.
  {:color  0
   :images {:monkey (q/load-image "resources/monkey.png")
            :man    (q/load-image "resources/man.png")
            :bush   (q/load-image "resources/bush.png")}})

(defn update-state [state]
  ; Update sketch state by changing circle color and position.
  (assoc state
    :color (mod (+ (:color state) 0.7) 255)))

(defn draw-state [state]
  ; Clear the sketch by filling it with light-grey color.
  (q/background 240)
  ; Set circle color.
  (q/fill (:color state) 255 255)
  ; Calculate x and y coordinates of the circle.
  ; Move origin point to the center of the sketch.
  (q/with-translation [(/ (q/width) 2)
                       (/ (q/height) 2)]
    (q/image-mode :center)
    (q/image (get-in state [:images :man]) 0 0 (/ 837 2.5) (/ 1200 2.5))
    (q/image (get-in state [:images :bush]) 0 0)
    (q/image (get-in state [:images :monkey]) 0 (/ (q/height) 4))))

(comment
  (q/defsketch monkey-game
    :title "Hop Monkey"
    :size [500 500]
    ; setup function called only once, during sketch initialization.
    :setup setup
    ; update-state is called on each iteration before draw-state.
    :update update-state
    :draw draw-state
    :features [:keep-on-top]
    ; This sketch uses functional-mode middleware.
    ; Check quil wiki for more info about middlewares and particularly
    ; fun-mode.
    :middleware [m/fun-mode]))
