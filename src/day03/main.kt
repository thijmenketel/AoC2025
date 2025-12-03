@file:Suppress("ktlint:standard:filename")

package day03

import utils.readFromPath
import utils.shouldBe
import kotlin.time.measureTimedValue

fun main() {
    val basePath = "day03/data"

    // TEST for part 1
    part1(readFromPath("$basePath/test-input.txt")) shouldBe 357

    // TEST for part 2
    part2(readFromPath("$basePath/test-input.txt")) shouldBe 3121910778619

    println("Solution of day03:")
    val input = readFromPath("$basePath/input.txt")
    measureTimedValue { part1(input) }.let { (part1, time) ->
        println("Part 1: $part1 took $time")
    }
    measureTimedValue { part2(input) }.let { (part2, time) ->
        println("Part 2: $part2 took $time")
    }
}

fun part1(input: Sequence<String>): Int =
    input.sumOf {
        val (index, first) = it.take(it.length - 1).toMaxNumber()
        val (_, second) = it.substring(index + 1).toMaxNumber()
        "$first$second".toInt()
    }

fun part2(input: Sequence<String>): Long =
    input.sumOf { str ->
        buildString {
            var iter = 0
            (11 downTo 0).forEach { i ->
                val (index, digit) = str.substring(iter, str.length - i).toMaxNumber()
                iter += index + 1
                append(digit)
            }
        }.toLong()
    }

fun String.toMaxNumber() = withIndex().maxBy { it.value.digitToInt() }
