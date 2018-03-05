package sk.ikim23.montecarlo.core

import javafx.animation.AnimationTimer
import javafx.beans.property.SimpleObjectProperty
import java.util.*

class RenderCore {
    val statusProperty = SimpleObjectProperty<Status>(Status.STOPPED)
    private val timer: AnimationTimer
    private val services = LinkedList<IService>()

    init {
        statusProperty.addListener { _, oldValue, newValue ->
            if (newValue != oldValue) {
                when (newValue) {
                    Status.RUNNING -> start()
                    Status.PAUSED -> pause()
                    Status.STOPPED -> stop()
                }
            }
        }
        timer = object : AnimationTimer() {
            override fun handle(now: Long) {
                synchronized(services) {
                    services.forEach { it.render() }
                }
            }
        }
    }

    fun registerService(service: IService) {
        synchronized(services) {
            services.add(service)
        }
    }

    private fun start() {
        synchronized(services) {
            services.forEach { it.start() }
        }
        timer.start()
    }

    private fun pause() {
        synchronized(services) {
            services.forEach { it.pause() }
        }
        timer.stop()
    }

    private fun stop() {
        synchronized(services) {
            services.forEach { it.stop() }
        }
        timer.stop()
    }
}