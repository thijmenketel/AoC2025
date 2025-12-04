@file:Suppress("ktlint:standard:filename")

package day04

import utils.readFromPath
import utils.shouldBe
import kotlin.time.measureTimedValue

fun main() {
    val basePath = "day04/data"

    // TEST for part 1
    part1(readFromPath("$basePath/test-input.txt")) shouldBe 13

    // TEST for part 2
    part2(readFromPath("$basePath/test-input.txt")) shouldBe 43

    println("Solution of day04:")
    val input = readFromPath("$basePath/input.txt")
    measureTimedValue { part1(input) }.let { (part1, time) ->
        println("Part 1: $part1 took $time")
    }
    measureTimedValue { part2(input) }.let { (part2, time) ->
        println("Part 2: $part2 took $time")
    }
}

fun part1(input: Sequence<String>): Int =
    with(input.map { it.toCharArray() }.toList()) {
        withIndex().sumOf { (row, chars) ->
            chars.withIndex().sumOf { (col, char) ->
                if (char == '@' && isFree(row, col)) 1 else 0
            }
        }
    }

fun part2(input: Sequence<String>): Int {
    var total = 0
    with(input.map { it.toCharArray() }.toList()) {
        var removed: Int
        do {
            removed = 0
            forEachIndexed { row, chars ->
                chars.forEachIndexed { col, char ->
                    if (char == '@' && isFree(row, col)) (removed++).also { this[row][col] = '.' }
                }
            }
            total += removed
        } while (removed > 0)
    }
    return total
}

/**
 * Not the most efficient way to check if a position is free, but it looks nice!
 */
fun List<CharArray>.isFree(
    row: Int,
    col: Int,
): Boolean =
    positions.sumOf { (x, y) ->
        if (getOrNull(row + x)?.getOrNull(col + y) == '@') 1 else 0
    } < 4

val positions =
    arrayOf(1 to 1, 1 to 0, 0 to 1, -1 to 1, 1 to -1, -1 to -1, -1 to 0, 0 to -1)
