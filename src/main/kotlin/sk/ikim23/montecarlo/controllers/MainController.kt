package sk.ikim23.montecarlo.controllers

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.chart.XYChart
import sk.ikim23.montecarlo.core.IResultRenderer
import sk.ikim23.montecarlo.core.RenderCore
import sk.ikim23.montecarlo.core.Service
import sk.ikim23.montecarlo.core.Status
import sk.ikim23.montecarlo.problem.*
import tornadofx.*

typealias XYData = XYChart.Data<Number, Number>

class MainController : Controller() {
    val startDisableProperty = SimpleBooleanProperty()
    val pauseDisableProperty = SimpleBooleanProperty()
    val stopDisableProperty = SimpleBooleanProperty()
    val numReplicationsProperty = SimpleIntegerProperty(1_000_000)
    val maxPointsProperty = SimpleIntegerProperty(1_000)
    val keepGuessSeries = XYChart.Series<Number, Number>()
    val changeGuessSeries = XYChart.Series<Number, Number>()
    val chartData = listOf(keepGuessSeries, changeGuessSeries).observable()
    val numDoorsProperty = SimpleIntegerProperty(3)
    val keepGuessVisibleProperty = SimpleBooleanProperty(true)
    val keepGuessDisableProperty = SimpleBooleanProperty()
    val changeGuessVisibleProperty = SimpleBooleanProperty(true)
    val changeGuessDisableProperty = SimpleBooleanProperty()
    val simCore = RenderCore()

    init {
        keepGuessSeries.name = "Keep Guess"
        changeGuessSeries.name = "Change Guess"
        updateButtons()
        simCore.statusProperty.onChange { status ->
            updateButtons()
            if (status == Status.RUNNING) {
                keepGuessDisableProperty.set(true)
                changeGuessDisableProperty.set(true)
            } else {
                keepGuessDisableProperty.set(false)
                changeGuessDisableProperty.set(false)
            }
        }
        numReplicationsProperty.onChange { updateButtons() }
        maxPointsProperty.onChange { updateButtons() }
    }

    fun start() {
        keepGuessSeries.data.clear()
        changeGuessSeries.data.clear()
        simCore.clear()
        val skipPoints = ((1.0 / maxPointsProperty.get()) * numReplicationsProperty.get()).toInt()
        simCore.registerService(Service(KeepGuessTask(numReplicationsProperty.get(), numDoorsProperty.get()), object : IResultRenderer<XYData> {
            override fun render(results: List<XYData>) {
                keepGuessSeries.data.addAll(results)
            }
        }, skipPoints))
        simCore.registerService(Service(ChangeGuessTask(numReplicationsProperty.get(), numDoorsProperty.get()), object : IResultRenderer<XYData> {
            override fun render(results: List<XYData>) {
                changeGuessSeries.data.addAll(results)
            }
        }, skipPoints))
        simCore.statusProperty.set(Status.RUNNING)
    }

    fun pause() {
        simCore.statusProperty.set(Status.PAUSED)
    }

    fun stop() {
        simCore.statusProperty.set(Status.STOPPED)
    }

    private fun updateButtons() {
        if (maxPointsProperty.get() > 0 &&
                numReplicationsProperty.get() > maxPointsProperty.get()) {
            when (simCore.statusProperty.get()) {
                Status.RUNNING -> {
                    startDisableProperty.set(true)
                    pauseDisableProperty.set(false)
                    stopDisableProperty.set(false)
                }
                Status.PAUSED -> {
                    startDisableProperty.set(false)
                    pauseDisableProperty.set(true)
                    stopDisableProperty.set(false)
                }
                Status.STOPPED -> {
                    startDisableProperty.set(false)
                    pauseDisableProperty.set(true)
                    stopDisableProperty.set(true)
                }
            }
        } else {
            startDisableProperty.set(true)
            pauseDisableProperty.set(true)
            stopDisableProperty.set(true)
        }
    }
}