import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.roundToLong

fun main() {
    val input = readInput("Day02").first()

    val idRanges: List<Pair<Long, Long>> = input
        .split(",")
        .map { it.split("-").run { get(0).toLong() to get(1).toLong() } }

    task1(idRanges)

    task2(idRanges)
}

private fun task1(idRanges: List<Pair<Long, Long>>) {
    val lengthDivisor = 2

    val invalidIdSum = idRanges
        .flatMap(::getSameLengthInvalidIdContainerSubRanges)
        .filter { lengthDivisor in getPossibleMirrorNumberLengthDivisors(it.first.calculateDecimalPlaces()) }
        .sumOf { getInvalidIdsInSameDecimalPlaceRange(range = it, lengthDivisor = lengthDivisor).sum() }

    println("Task 1 Result: $invalidIdSum")
}

private fun getSameLengthInvalidIdContainerSubRanges(range: Pair<Long, Long>): List<Pair<Long, Long>> = buildList {
    val startLength = range.first.calculateDecimalPlaces()
    val endLength = range.second.calculateDecimalPlaces()

    (startLength..endLength).forEach { length ->
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

private fun getPossibleMirrorNumberLengthDivisors(length: Int): Set<Int> = buildSet {
    (2..length).forEach { candidate ->
        if (length % candidate == 0 && all { candidate % it != 0 }) add(candidate)
    }
}

private fun getInvalidIdsInSameDecimalPlaceRange(range: Pair<Long, Long>, lengthDivisor: Int): Set<Long> {
    val rangeDecimalPlaceLength = range.first.calculateDecimalPlaces()
    require(rangeDecimalPlaceLength % lengthDivisor == 0)
    require(rangeDecimalPlaceLength == range.second.calculateDecimalPlaces())

    val invalidIds = mutableSetOf<Long>()
    var currentMirrorNumberPart = range.first /
        10f.pow(rangeDecimalPlaceLength - rangeDecimalPlaceLength / lengthDivisor).roundToLong()

    while (currentMirrorNumberPart.getMirrorNumber(rangeDecimalPlaceLength, lengthDivisor) < range.first) {
        currentMirrorNumberPart += 1
    }

    if (currentMirrorNumberPart.calculateDecimalPlaces() * lengthDivisor != rangeDecimalPlaceLength) return emptySet()

    var currentMirrorNumber = currentMirrorNumberPart.getMirrorNumber(rangeDecimalPlaceLength, lengthDivisor)

    while (currentMirrorNumber <= range.second) {
        invalidIds.add(currentMirrorNumber)
        currentMirrorNumberPart += 1
        currentMirrorNumber = currentMirrorNumberPart.getMirrorNumber(rangeDecimalPlaceLength, lengthDivisor)
    }

    return invalidIds
}

private fun Long.getMirrorNumber(decimalLength: Int, lengthDivisor: Int): Long = (1L..lengthDivisor.toLong())
    .fold(0) { acc, _ -> acc * 10f.pow(decimalLength / lengthDivisor).toLong() + this }

private fun task2(idRanges: List<Pair<Long, Long>>) {
    val result = idRanges
        .flatMap(::getSameLengthInvalidIdContainerSubRanges)
        .map { sameLengthSubRange ->
            sameLengthSubRange to getPossibleMirrorNumberLengthDivisors(
                sameLengthSubRange.first.calculateDecimalPlaces()
            )
        }
        .sumOf { subrangeWithLengthDivisors ->
            subrangeWithLengthDivisors.second
                .flatMap { getInvalidIdsInSameDecimalPlaceRange(subrangeWithLengthDivisors.first, it) }
                .toSet()
                .sum()
        }

    println("Task 2 result: $result")
}
