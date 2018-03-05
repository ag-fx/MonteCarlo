package sk.ikim23.montecarlo.controllers

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import sk.ikim23.montecarlo.core.RenderCore
import sk.ikim23.montecarlo.core.Service
import sk.ikim23.montecarlo.core.Status
import sk.ikim23.montecarlo.problem.LotteryResult
import sk.ikim23.montecarlo.problem.LotteryTask
import sk.ikim23.montecarlo.problem.PrintRenderer
import tornadofx.*

class MainController : Controller() {
    val startDisableProperty = SimpleBooleanProperty()
    val pauseDisableProperty = SimpleBooleanProperty()
    val stopDisableProperty = SimpleBooleanProperty()
    val numReplicationsProperty = SimpleIntegerProperty()
    val simCore = RenderCore()

    init {
        updateButtons()
        simCore.statusProperty.onChange { updateButtons() }
        numReplicationsProperty.onChange { updateButtons() }
        simCore.registerService(Service(LotteryTask(), PrintRenderer()))
    }

    fun start() {
        simCore.statusProperty.set(Status.RUNNING)
    }

    fun pause() {
        simCore.statusProperty.set(Status.PAUSED)
    }

    fun stop() {
        simCore.statusProperty.set(Status.STOPPED)
    }

    private fun updateButtons() {
        if (numReplicationsProperty.value > 0) {
            when (simCore.statusProperty.value) {
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