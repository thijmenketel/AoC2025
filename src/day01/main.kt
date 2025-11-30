@file:Suppress("ktlint:standard:filename")

package day01

import utils.readFromPath
import utils.shouldBe
import kotlin.time.measureTimedValue

fun main() {
    val basePath = "day01/data"

    // TEST for part 1
    part1(readFromPath("$basePath/test-input.txt")) shouldBe 4

    // TEST for part 2
    part2(readFromPath("$basePath/test-input.txt")) shouldBe 4

    println("Solution of day01:")
    val input = readFromPath("$basePath/input.txt")
    measureTimedValue { part1(input) }.let { (part1, time) ->
        println("Part 1: $part1 took $time")
    }
    measureTimedValue { part2(input) }.let { (part2, time) ->
        println("Part 2: $part2 took $time")
    }
}

fun part1(input: Sequence<String>): Int = input.toList().size

fun part2(input: Sequence<String>): Int = input.toList().size
