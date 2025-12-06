@file:Suppress("ktlint:standard:filename")

package day06

import utils.readWithoutTrim
import utils.shouldBe
import kotlin.time.measureTimedValue

fun main() {
    val basePath = "day06/data"

    // TEST for part 1
    part1(readWithoutTrim("$basePath/test-input.txt")) shouldBe 4277556

    // TEST for part 2
    part2(readWithoutTrim("$basePath/test-input.txt")) shouldBe 3263827

    println("Solution of day06:")
    val input = readWithoutTrim("$basePath/input.txt")
    measureTimedValue { part1(input) }.let { (part1, time) ->
        println("Part 1: $part1 took $time")
    }
    measureTimedValue { part2(input) }.let { (part2, time) ->
        println("Part 2: $part2 took $time")
    }
}

fun part1(input: Sequence<String>): Long =
    input
        .toList()
        .getNumbersAndOperators()
        .let { (numbers, operators) ->
            operators.withIndex().sumOf { (i, op) ->
                var sum = numbers[0][i].toLong()
                (1 until numbers.size).forEach { j ->
                    sum = op(sum, numbers[j][i].toLong())
                }
                sum
            }
        }

fun part2(input: Sequence<String>): Long =
    input.toOperatorsAndSums().let { (operators, sums) ->
        operators.withIndex().sumOf { (i, op) ->
            sums.let {
                it[i].transpose().reduce { a, b -> op(a, b) }
            }
        }
    }

private fun List<String>.transpose() =
    (first().length - 1 downTo 0).map { i ->
        buildString {
            (0 until size).forEach { j -> append(this@transpose[j][i]) }
        }.trim().toLong()
    }

fun Sequence<String>.toOperatorsAndSums(): Pair<List<Char>, List<List<String>>> {
    val input = toList()
    val sums = mutableListOf<List<String>>()
    val operations =
        last().let {
            val ops = mutableListOf(it[0])
            var lastOpIndex = 0
            (0..it.length).forEach { i ->
                it.getOrNull(i)?.let { char ->
                    if (char != ' ') {
                        if (i != 0) {
                            ops.add(char)
                            sums.add((0 until input.size - 1).map { j -> input[j].substring(lastOpIndex, i - 1) })
                        }
                        lastOpIndex = i
                    }
                } ?: sums.add((0 until input.size - 1).map { j -> input[j].substring(lastOpIndex) })
            }
            ops.toList()
        }
    return operations to sums
}

operator fun Char.invoke(
    sum: Long,
    number: Long,
) = when (this) {
    '+' -> sum + number
    '*' -> sum * number
    else -> throw IllegalArgumentException("Unrecognized operator: $this")
}

fun List<String>.getNumbersAndOperators(): Pair<Array<Array<String>>, Sequence<Char>> {
    val operators = last().trim().splitToSequence("\\s+".toRegex()).map { it[0] }
    val numbers =
        take(this.size - 1)
            .map { line ->
                line
                    .trim()
                    .split("\\s+".toRegex())
                    .toTypedArray()
            }.toList()
            .toTypedArray()
    return numbers to operators
}
