package sk.ikim23.montecarlo.problem

import sk.ikim23.montecarlo.core.IServiceTask
import java.util.*

class LotteryTask : IServiceTask<LotteryResult> {
    val randGuess: Random
    val randCar: Random
    var rep = 0
    var keepGuessWins = 0
    var changeGuessWins = 0

    init {
        val rand = Random()
        randGuess = Random(rand.nextLong())
        randCar = Random(rand.nextLong())
    }

    override fun initialize() {
        rep = 0
        keepGuessWins = 0
        changeGuessWins = 0
    }

    override fun tick(): LotteryResult {
        val car = randCar.nextInt(3)
        val guess = randGuess.nextInt(3)
        val result = LotteryResult()
        if (car == guess) {
            keepGuessWins++
            result.keepGuessWins = true
        } else {
            changeGuessWins++
        }
        rep++
        return result
    }
}