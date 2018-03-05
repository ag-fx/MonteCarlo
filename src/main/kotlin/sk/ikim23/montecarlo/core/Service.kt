package sk.ikim23.montecarlo.core

import java.util.concurrent.atomic.AtomicReference
import kotlin.concurrent.thread

class Service<T>(task: IServiceTask<T>, renderer: IResultRenderer<T>, maxResults: Int = 100) : IService {
    private val lock = Object()
    private val status = AtomicReference<Status>(Status.STOPPED)
    private val maxResults = maxResults
    private val results = ArrayList<T>(maxResults)
    private val task = task
    private val renderer = renderer

    override fun render() {
        synchronized(lock) {
            renderer.render(results)
            results.clear()
            try {
                lock.notify()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun start() {
        when (status.get()) {
            Status.PAUSED -> status.set(Status.RUNNING)
            Status.STOPPED -> startThread()
        }
    }

    override fun pause() {
        if (status.get() == Status.RUNNING) {
            status.set(Status.PAUSED)
        }
    }

    override fun stop() {
        if (status.get() != Status.STOPPED) {
            status.set(Status.STOPPED)
        }
    }

    private fun startThread() {
        thread {
            results.clear()
            task.initialize()
            status.set(Status.RUNNING)
            while (status.get() != Status.STOPPED) {
                if (status.get() == Status.PAUSED) {
                    synchronized(lock) {
                        try {
                            lock.wait()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
                synchronized(lock) {
                    if (results.size >= maxResults) {
                        try {
                            lock.wait()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    val result = task.tick()
                    results.add(result)
                }
            }
        }
    }
}