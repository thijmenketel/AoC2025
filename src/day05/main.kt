@file:Suppress("ktlint:standard:filename")

package day05

import utils.readRawInput
import utils.shouldBe
import kotlin.time.measureTimedValue

fun main() {
    val basePath = "day05/data"

    // TEST for part 1
    part1(readRawInput("$basePath/test-input.txt")) shouldBe 3

    // TEST for part 2
    part2(readRawInput("$basePath/test-input.txt")) shouldBe 14

    println("Solution of day05:")
    val input = readRawInput("$basePath/input.txt")
    measureTimedValue { part1(input) }.let { (part1, time) ->
        println("Part 1: $part1 took $time")
    }
    measureTimedValue { part2(input) }.let { (part2, time) ->
        println("Part 2: $part2 took $time")
    }
}

fun part1(input: String): Int =
    input.toRangesAndNumbers().let { (ranges, numbers) ->
        val merged = ranges.mergeRanges()
        numbers.sumOf {
            if (merged.any { (a, b) -> it in a..b }) 1 else 0
        }
    }

fun part2(input: String): Long =
    input.toRangesAndNumbers().let { (ranges, _) ->
        ranges.mergeRanges().sumOf { (a, b) -> b - a + 1 }
    }

fun Sequence<Pair<Long, Long>>.mergeRanges(): List<Pair<Long, Long>> =
    sortedBy { it.first }.let { sorted ->
        val merged = mutableListOf<Pair<Long, Long>>()
        sorted.forEach { (start, end) ->
            if (merged.isEmpty() || merged.last().second < start - 1) {
                merged.add(start to end)
            } else {
                val last = merged.removeLast()
                merged.add(last.first to maxOf(last.second, end))
            }
        }
        merged
    }

fun String.toRangesAndNumbers() =
    splitToSequence("\n\n")
        .let {
            val ranges = it.first().lineSequence().map { r -> r.split("-").let { (a, b) -> a.toLong() to b.toLong() } }
            val numbers = it.last().lineSequence().map { n -> n.toLong() }
            ranges to numbers
        }
