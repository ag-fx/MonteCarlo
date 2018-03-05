package sk.ikim23.montecarlo.core

interface IServiceTask<T> {
    fun initialize()
    fun tick(): T
}