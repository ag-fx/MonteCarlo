package sk.ikim23.montecarlo.model

import javafx.beans.property.*
import sk.ikim23.montecarlo.core.Status
import tornadofx.*

class RenderControlModel(private val statusProperty: ReadOnlyProperty<Status>, reps: Int = 1_000_000, maxPts: Int = 1_000) {
    private val replications = SimpleIntegerProperty(reps)
    private val maxPoints = SimpleIntegerProperty(maxPts)
    private val startDisable = SimpleBooleanProperty()
    private val pauseDisable = SimpleBooleanProperty()
    private val stopDisable = SimpleBooleanProperty()

    init {
        statusProperty.onChange { update() }
        replications.onChange { update() }
        maxPoints.onChange { update() }
        update()
    }

    fun replicationsProperty() = replications
    fun maxPointsProperty() = maxPoints
    fun startDisableProperty(): ReadOnlyBooleanProperty = startDisable
    fun pauseDisableProperty(): ReadOnlyBooleanProperty = pauseDisable
    fun stopDisableProperty(): ReadOnlyBooleanProperty = stopDisable
    fun skipPoints() = ((1.0 / maxPoints.value) * replications.value).toInt()

    private fun update() {
        val validData = 0 < maxPoints.value && maxPoints.value < replications.value
        if (validData) {
            when (statusProperty.value) {
                Status.RUNNING -> {
                    startDisable.set(true)
                    pauseDisable.set(false)
                    stopDisable.set(false)
                }
                Status.PAUSED -> {
                    startDisable.set(false)
                    pauseDisable.set(true)
                    stopDisable.set(false)
                }
                else -> {
                    startDisable.set(false)
                    pauseDisable.set(true)
                    stopDisable.set(true)
                }
            }
        } else {
            startDisable.set(true)
            pauseDisable.set(true)
            stopDisable.set(true)
        }
    }
}