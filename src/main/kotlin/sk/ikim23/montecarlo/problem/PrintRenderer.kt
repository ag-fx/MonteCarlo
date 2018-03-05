package sk.ikim23.montecarlo.problem

import sk.ikim23.montecarlo.core.IResultRenderer

class PrintRenderer : IResultRenderer<LotteryResult> {
    override fun render(results: List<LotteryResult>) {
        for (result in results) {
            val first = if (result.keepGuessWins) 1 else 0
            val second = if (result.keepGuessWins) 0 else 1
            println("Rep: ${result.x} ${first} ${second}")
        }
    }
}