package sk.ikim23.montecarlo.problem

import sk.ikim23.montecarlo.controllers.XYData
import sk.ikim23.montecarlo.core.IServiceTask
import java.util.*

class ChangeGuessTask : IServiceTask<XYData> {
    val randGuess: Random
    val randCar: Random
    val numDoors = 3
    var reps = 0
    var wins = 0.0

    init {
        val rand = Random()
        randGuess = Random(rand.nextLong())
        randCar = Random(rand.nextLong())
    }

    override fun initialize() {
        reps = 0
        wins = 0.0
    }

    override fun tick(): XYData {
        val car = randCar.nextInt(numDoors)
        val guess = randGuess.nextInt(numDoors)
        if (car != guess) {
            wins++
        }
        reps++
        return XYData(reps, wins / reps)
    }
}