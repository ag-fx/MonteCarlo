package sk.ikim23.montecarlo.views

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.chart.NumberAxis
import sk.ikim23.montecarlo.controllers.MainController
import tornadofx.*

class MainView : View() {
    override val root = borderpane()
    val btnWidth = 70.0
    val controller: MainController by inject()

    init {
        title = "Monte Carlo"
        root.top {
            hbox {
                padding = Insets(5.0)
                spacing = 5.0
                alignment = Pos.CENTER_LEFT
                label("# Replications")
                textfield {
                    prefWidth = btnWidth
                    textProperty().bindBidirectional(controller.numReplicationsProperty, IntConverter())
                }
                button("Start") {
                    prefWidth = btnWidth
                    disableProperty().bind(controller.startDisableProperty)
                    setOnAction { controller.start() }
                }
                button("Pause") {
                    prefWidth = btnWidth
                    disableProperty().bind(controller.pauseDisableProperty)
                    setOnAction { controller.pause() }
                }
                button("Stop") {
                    prefWidth = btnWidth
                    disableProperty().bind(controller.stopDisableProperty)
                    setOnAction { controller.stop() }
                }
            }
        }
        root.center {
            scatterchart("title", NumberAxis(), NumberAxis()) {
                xAxis.animated = false
                yAxis.animated = false
                animated = false
            }
        }
    }
}