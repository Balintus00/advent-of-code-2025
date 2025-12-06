import kotlin.collections.emptySet

fun main() {
    val banks: List<List<Int>> = readInput("Day03").map { bank -> bank.map { it.digitToInt() } }

    val result = banks.sumOf { bank ->
        val firstDecimalPlaceCandidateIndices = bank
            .dropLast(1)
            .foldIndexed(emptySet<Int>()) { bankIndex, maxJoltageIndices, joltage ->
                when {
                    maxJoltageIndices.firstOrNull()?.let { bank[it] < joltage } != false -> setOf(bankIndex)
                    bank[maxJoltageIndices.first()] == joltage -> maxJoltageIndices + bankIndex
                    else -> maxJoltageIndices
                }
            }

        firstDecimalPlaceCandidateIndices.maxOf { firstDecimalPlaceIndex ->
            val secondDecimalPlaceJoltage = bank
                .drop(firstDecimalPlaceIndex + 1)
                .max()

            bank[firstDecimalPlaceIndex] * 10 + secondDecimalPlaceJoltage
        }
    }

    println("Task 1 result: $result")
}
