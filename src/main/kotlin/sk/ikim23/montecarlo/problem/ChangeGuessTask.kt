package sk.ikim23.montecarlo.problem

import sk.ikim23.montecarlo.controllers.XYData
import sk.ikim23.montecarlo.core.IServiceTask
import java.util.*

class ChangeGuessTask(val maxReps: Int, val doors: Int) : IServiceTask<XYData> {
    val randGuess1 = Random()
    val randGuess2 = Random()
    val randCar = Random()
    val randDoors = Random()
    var reps = 0
    var wins = 0.0

    override fun initialize() {
        val rand = Random()
        randGuess1.setSeed(rand.nextLong())
        randGuess2.setSeed(rand.nextLong())
        randCar.setSeed(rand.nextLong())
        randDoors.setSeed(rand.nextLong())
        reps = 0
        wins = 0.0
    }

    override fun hasNext() = reps < maxReps

    override fun tick(): XYData {
        val car = randCar.nextInt(doors)
        val guess1 = randGuess1.nextInt(doors)
        var openDoors = skipIfContains(randDoors.nextInt(doors), guess1, car)
        val guess2 = skipIfContains(randGuess2.nextInt(doors), guess1, openDoors)
        if (car == guess2) {
            wins++
        }
        reps++
        return XYData(reps, wins / reps)
    }

    fun skipIfContains(value: Int, vararg blackList: Int): Int {
        var result = value
        while (blackList.contains(result)) {
            result++
            if (result >= doors) result = 0
        }
        return result
    }
}