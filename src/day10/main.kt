@file:Suppress("ktlint:standard:filename")

package day10

import utils.combinations
import utils.readFromPath
import utils.shouldBe
import java.util.BitSet
import kotlin.time.measureTimedValue

fun main() {
    val basePath = "day10/data"

    // TEST for part 1
    part1(readFromPath("$basePath/test-input.txt")) shouldBe 7

    // TEST for part 2
    part2(readFromPath("$basePath/test-input.txt")) shouldBe 33

    println("Solution of day10:")
    val input = readFromPath("$basePath/input.txt")
    measureTimedValue { part1(input) }.let { (part1, time) ->
        println("Part 1: $part1 took $time")
    }
    measureTimedValue { part2(input) }.let { (part2, time) ->
        println("Part 2: $part2 took $time")
    }
}

fun part1(input: Sequence<String>) =
    input.toMachines().sumOf {
        it.matchLights().getValue(it.lights.toString()).minOf { m -> m.size }
    }

fun part2(input: Sequence<String>): Int =
    input.toMachines().sumOf {
        it.matchJoltage(it.matchLights())
    }

data class Machine(
    val lights: BitSet,
    val buttons: List<BitSet>,
    val joltage: List<Int>,
)

fun Machine.matchLights(): Map<String, MutableList<List<BitSet>>> {
    val patterns = mutableMapOf<String, MutableList<List<BitSet>>>()
    (0..buttons.size).forEach { presses ->
        buttons.combinations(presses).forEach { ops ->
            val result = BitSet(lights.size())
            ops.forEach { result.xor(it) }
            patterns.getOrPut(result.toString()) { mutableListOf() }.add(ops)
        }
    }
    return patterns
}

fun Machine.matchJoltage(patterns: Map<String, List<List<BitSet>>>): Int {
    fun findPresses(
        jolts: List<Int>,
        cache: MutableMap<String, Int> = mutableMapOf(),
    ): Int =
        cache.getOrPut(jolts.toString()) {
            if (jolts.all { it == 0 }) return@getOrPut 0
            val ind =
                BitSet(jolts.size).apply {
                    jolts.forEachIndexed { i, jolt ->
                        if (jolt % 2 == 1) set(i)
                    }
                }
            var lowestPresses = 1_000_000
            patterns[ind.toString()]?.forEach loop@{ pats ->
                val newJolts = ArrayList(jolts)
                pats.forEach { it.stream().forEach { bit -> newJolts[bit]-- } }
                if (newJolts.any { it < 0 }) return@loop // continue
                val newLowestPresses = 2 * findPresses(newJolts.map { it / 2 }, cache) + pats.size
                lowestPresses = minOf(lowestPresses, newLowestPresses)
            }
            return@getOrPut lowestPresses
        }

    return findPresses(joltage)
}

private fun Sequence<String>.toMachines(): Sequence<Machine> =
    map { line ->
        line.split(']').let { (first, rest) ->
            val lights = BitSet(first.length - 1).apply { first.drop(1).forEachIndexed { i, c -> if (c == '#') set(i) } }
            val (middle, last) = rest.split('{')
            val joltage = last.dropLast(1).split(',').map { it.toInt() }
            val buttons =
                middle.trim().split(' ').map { wiring ->
                    val arr = BitSet(lights.length())
                    wiring
                        .drop(1)
                        .dropLast(1)
                        .split(',')
                        .forEach { arr.set(it.toInt()) }
                    arr
                }
            Machine(lights, buttons, joltage)
        }
    }
