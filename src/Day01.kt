fun main() {
    val input = readInput("Day01")

    part1(input)
    part2(input)
}

private fun part1(input: List<String>) {
    var zeroEndPositionCounter = 0

    executeSteps(
        input = input,
        onStepEndedOnZero = { zeroEndPositionCounter += 1 },
    )

    println("Part 1 result is $zeroEndPositionCounter")
}

private fun executeSteps(
    input: List<String>,
    onStepEndedOnZero: (() -> Unit)? = null,
    onStepCrossedZero: (() -> Unit)? = null,
) {
    var currentPosition = 50

    input.forEach { step ->
        val stepSize = step.removeRange(0, 1).toInt()
        val isStartingFromZero = currentPosition == 0

        currentPosition = when (step.first()) {
            'L' -> currentPosition - stepSize
            'R' -> currentPosition + stepSize
            else -> throw IllegalArgumentException("Unexpected direction input: ${step.first()}")
        }

        if (currentPosition == 0) {
            onStepCrossedZero?.invoke()
        }

        var hasDecrementCrossingHappenedBefore = false
        while (currentPosition < 0) {
            currentPosition += 100

            if (hasDecrementCrossingHappenedBefore || !isStartingFromZero) {
                onStepCrossedZero?.invoke()
            }

            hasDecrementCrossingHappenedBefore = true
        }

        while (currentPosition > 99) {
            currentPosition -= 100

            onStepCrossedZero?.invoke()
        }

        if (currentPosition == 0) {
            onStepEndedOnZero?.invoke()

            if (hasDecrementCrossingHappenedBefore) {
                onStepCrossedZero?.invoke()
            }
        }
    }
}

private fun part2(input: List<String>) {
    var zeroClickCounter = 0

    executeSteps(
        input = input,
        onStepCrossedZero = { zeroClickCounter += 1 },
    )

    println("Part 2 result is $zeroClickCounter")
}
