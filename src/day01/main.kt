@file:Suppress("ktlint:standard:filename")

package day01

import utils.readFromPath
import utils.shouldBe
import kotlin.math.abs
import kotlin.time.measureTimedValue

fun main() {
    val basePath = "day01/data"

    // TEST for part 1
    part1(readFromPath("$basePath/test-input.txt")) shouldBe 3

    // TEST for part 2
    part2(readFromPath("$basePath/test-input.txt")) shouldBe 6

    println("Solution of day01:")
    val input = readFromPath("$basePath/input.txt")
    measureTimedValue { part1(input) }.let { (part1, time) ->
        println("Part 1: $part1 took $time")
    }
    measureTimedValue { part2(input) }.let { (part2, time) ->
        println("Part 2: $part2 took $time")
    }
}

fun part1(input: Sequence<String>): Int {
    var zeroes = 0
    input.fold(50) { curr, elem ->
        (curr + elem.toSum()).mod(100).also { if (it == 0) zeroes++ }
    }
    return zeroes
}

fun part2(input: Sequence<String>): Int {
    var curr = 50
    return input
        .sumOf { elem ->
            var hits = 0
            val (dir, steps) = elem.toRotation()
            repeat(steps) {
                curr = (curr + dir).mod(100)
                if (curr == 0) hits++
            }
            hits
        }
}

fun part2v2(input: Sequence<String>): Int {
    var curr = 50
    return input
        .sumOf { elem ->
            elem.toRotation().let { (dir, steps) ->
                val last = curr
                var div = abs((curr + dir * steps).floorDiv(100))
                curr = (curr + dir * steps).mod(100)
                if (dir == -1) {
                    if (last == 0) div--
                    if (curr == 0) div++
                }
                div
            }
        }
}

fun String.toSum() =
    when (first()) {
        'L' -> -1 * substring(1).toInt()
        'R' -> substring(1).toInt()
        else -> error("Invalid direction")
    }

fun String.toRotation() =
    when (first()) {
        'L' -> -1 to substring(1).toInt()
        'R' -> 1 to substring(1).toInt()
        else -> error("Invalid direction")
    }
