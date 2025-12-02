import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.roundToLong

fun main() {
    val input = readInput("Day02").first()

    val idRanges: List<Pair<Long, Long>> = input
        .split(",")
        .map { it.split("-").run { get(0).toLong() to get(1).toLong() } }

    val invalidIdSum = idRanges
        .flatMap(::getPossibleInvalidIdContainerSubRanges)
        .sumOf(::getSumOfInvalidIdsInSameDecimalPlaceRange)

    println("Task 1 Result: $invalidIdSum")
}

private fun getPossibleInvalidIdContainerSubRanges(range: Pair<Long, Long>): List<Pair<Long, Long>> = buildList {
    val startLength = range.first.calculateDecimalPlaces()
    val endLength = range.second.calculateDecimalPlaces()

    (startLength..endLength).forEach { length ->
        if (length % 2 != 0) return@forEach

        val subRangeStart = if (startLength == length) {
            maxOf(range.first, 10f.pow(length - 1).toLong())
        } else {
            10f.pow(length - 1).toLong()
        }

        val subRangeEnd = if (endLength == length) {
            minOf(range.second, 10f.pow(length).toLong() - 1)
        } else {
            10f.pow(length).toLong() - 1
        }

        add(subRangeStart to subRangeEnd)
    }
}

private fun Long.calculateDecimalPlaces(): Int = log10(toDouble()).toInt() + 1

private fun getSumOfInvalidIdsInSameDecimalPlaceRange(range: Pair<Long, Long>): Long {
    val rangeDecimalPlaceLength = range.first.calculateDecimalPlaces()
    require(rangeDecimalPlaceLength % 2 == 0)
    require(rangeDecimalPlaceLength == range.second.calculateDecimalPlaces())

    var invalidIdSum: Long = 0
    var currentHalfMirrorNumber = range.first / 10f.pow(rangeDecimalPlaceLength / 2).roundToLong()

    while (currentHalfMirrorNumber.mirrorNumber(rangeDecimalPlaceLength) < range.first) {
        currentHalfMirrorNumber += 1
    }

    if (currentHalfMirrorNumber.calculateDecimalPlaces() * 2 != rangeDecimalPlaceLength) return 0

    var currentMirrorNumber = currentHalfMirrorNumber.mirrorNumber(rangeDecimalPlaceLength)

    while (currentMirrorNumber <= range.second) {
        invalidIdSum += currentMirrorNumber
        currentHalfMirrorNumber += 1
        currentMirrorNumber = currentHalfMirrorNumber.mirrorNumber(rangeDecimalPlaceLength)
    }

    return invalidIdSum
}

private fun Long.mirrorNumber(decimalLength: Int): Long = this * 10f.pow(decimalLength / 2).toLong() + this
