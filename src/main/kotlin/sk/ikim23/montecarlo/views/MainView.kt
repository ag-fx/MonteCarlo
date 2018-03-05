package sk.ikim23.montecarlo.views

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.chart.NumberAxis
import sk.ikim23.montecarlo.controllers.MainController
import tornadofx.*

class MainView : View() {
    override val root = borderpane()
    val cWidth = 70.0
    val cPadding = Insets(5.0)
    val cSpacing = 5.0
    val cAlignment = Pos.CENTER_LEFT
    val controller: MainController by inject()

    init {
        title = "Monte Carlo"
        root.top {
            vbox {
                hbox {
                    padding = cPadding
                    spacing = cSpacing
                    alignment = cAlignment
                    label("Replications:")
                    textfield {
                        prefWidth = cWidth
                        textProperty().bindBidirectional(controller.numReplicationsProperty, IntConverter())
                    }
                    label("Points/Group:")
                    textfield {
                        prefWidth = cWidth
                        textProperty().bindBidirectional(controller.maxPointsProperty, IntConverter())
                    }
                    button("Start") {
                        prefWidth = cWidth
                        disableProperty().bind(controller.startDisableProperty)
                        setOnAction { controller.start() }
                    }
                    button("Pause") {
                        prefWidth = cWidth
                        disableProperty().bind(controller.pauseDisableProperty)
                        setOnAction { controller.pause() }
                    }
                    button("Stop") {
                        prefWidth = cWidth
                        disableProperty().bind(controller.stopDisableProperty)
                        setOnAction { controller.stop() }
                    }
                }
                hbox {
                    padding = cPadding
                    spacing = cSpacing
                    alignment = cAlignment
                    label("Doors:")
                    textfield {
                        prefWidth = 50.0
                        textProperty().bindBidirectional(controller.numDoorsProperty, IntConverter())
                    }
                    checkbox("Show Keep Guess") {
                        selectedProperty().bindBidirectional(controller.keepGuessVisibleProperty)
                        disableProperty().bind(controller.keepGuessDisableProperty)
                    }
                    checkbox("Show Change Guess") {
                        selectedProperty().bindBidirectional(controller.changeGuessVisibleProperty)
                        disableProperty().bind(controller.changeGuessDisableProperty)
                    }
                }
                separator()
            }
        }
        root.center {
            linechart("", NumberAxis(), NumberAxis()) {
                xAxis.animated = false
                yAxis.animated = false
                animated = false
                data = controller.chartData
            }
        }
    }
}