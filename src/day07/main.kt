@file:Suppress("ktlint:standard:filename")

package day07

import utils.readFromPath
import utils.shouldBe
import kotlin.time.measureTimedValue

fun main() {
    val basePath = "day07/data"

    // TEST for part 1
    part1(readFromPath("$basePath/test-input.txt")) shouldBe 21

    // TEST for part 2
    part2(readFromPath("$basePath/test-input.txt")) shouldBe 40

    println("Solution of day07:")
    val input = readFromPath("$basePath/input.txt")
    measureTimedValue { part1(input) }.let { (part1, time) ->
        println("Part 1: $part1 took $time")
    }
    measureTimedValue { part2(input) }.let { (part2, time) ->
        println("Part 2: $part2 took $time")
    }
}

fun part1(input: Sequence<String>): Int {
    val tachyons = mutableSetOf(1 to input.first().indexOf('S'))
    var splits = 0
    input.drop(2).forEachIndexed { row, line ->
        val actRow = row + 2
        line.forEachIndexed { col, char ->
            if (char == '^' && (actRow - 1 to col) in tachyons) {
                tachyons += actRow to col - 1
                tachyons += actRow to col + 1
                splits++
            } else if ((actRow - 1 to col) in tachyons) {
                tachyons += actRow to col
            }
        }
    }
    return splits
}

fun part2(input: Sequence<String>): Long {
    return input.toList().findNumOfPaths2()
//    return input.toList().findNumOfPaths(1, input.first().indexOf('S'))
}

fun List<String>.findNumOfPaths(
    row: Int,
    col: Int,
    cache: MutableMap<Pair<Int, Int>, Long> = mutableMapOf(),
): Long =
    cache.getOrPut(row to col) {
        when {
            row + 1 == size -> 1
            this[row + 1][col] == '^' -> findNumOfPaths(row + 1, col - 1, cache) + findNumOfPaths(row + 1, col + 1, cache)
            else -> findNumOfPaths(row + 1, col, cache)
        }
    }

fun List<String>.findNumOfPaths2(): Long =
    LongArray(first().length).let {
        it[first().indexOf('S')] = 1
        (2 until size step 2).forEach { i ->
            this[i].forEachIndexed { j, char ->
                if (char == '^') {
                    it[j - 1] += it[j]
                    it[j + 1] += it[j]
                    it[j] = 0
                }
            }
        }
        it.sum()
    }
