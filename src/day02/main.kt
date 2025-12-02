@file:Suppress("ktlint:standard:filename")

package day02

import utils.readFromPath
import utils.shouldBe
import kotlin.time.measureTimedValue

fun main() {
    val basePath = "day02/data"

    // TEST for part 1
    part1(readFromPath("$basePath/test-input.txt")) shouldBe 1227775554

    // TEST for part 2
    part2(readFromPath("$basePath/test-input.txt")) shouldBe 4174379265

    println("Solution of day02:")
    val input = readFromPath("$basePath/input.txt")
    measureTimedValue { part1(input) }.let { (part1, time) ->
        println("Part 1: $part1 took $time")
    }
    measureTimedValue { part2(input) }.let { (part2, time) ->
        println("Part 2: $part2 took $time")
    }
}

fun part1(input: Sequence<String>) =
    input.first().splitToSequence(",").sumOf {
        it.toRange().sumOf { num ->
            val numAsString = num.toString()
            if (numAsString.length % 2 == 0 && numAsString.hasDuplicate()) num else 0
        }
    }

fun part2(input: Sequence<String>) =
    input
        .first()
        .split(",")
        .parallelStream()
        .mapToLong {
            it.toRange().sumOf { num ->
                if (num.toString().containsMultipleDup()) num else 0
            }
        }.sum()

fun String.toRange() =
    split("-")
        .let { (a, b) -> a.toLong()..b.toLong() }

fun String.hasDuplicate() = take(length / 2) == takeLast(length / 2)

fun String.containsMultipleDup(): Boolean =
    (1..length / 2)
        .any { i -> chunked(i).let { chunks -> chunks.all { it == chunks.first() } } }
