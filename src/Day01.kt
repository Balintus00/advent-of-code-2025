fun main() {
    part1()
}

private fun part1() {
    val input = readInput("Day01")

    var zeroPositionCounter = 0

    var currentPosition = 50

    input.forEach { step ->
        val stepSize = step.removeRange(0, 1).toInt()

        currentPosition = when (step.first()) {
            'L' -> currentPosition - stepSize
            'R' -> currentPosition + stepSize
            else -> throw IllegalArgumentException("Unexpected turn direction input: ${step.first()}")
        }

        while (currentPosition < 0) {
            currentPosition += 100
        }

        if (currentPosition > 99) {
            currentPosition %= 100
        }

        if (currentPosition == 0) {
            zeroPositionCounter += 1
        }
    }

    println("Part 1 result is $zeroPositionCounter")
}
