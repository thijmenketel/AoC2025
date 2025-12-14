@file:Suppress("ktlint:standard:filename")

package day11

import utils.readFromPath
import utils.shouldBe
import kotlin.time.measureTimedValue

fun main() {
    val basePath = "day11/data"

    // TEST for part 1
    part1(readFromPath("$basePath/test-input.txt")) shouldBe 5

    // TEST for part 2
    part2(readFromPath("$basePath/test-input2.txt")) shouldBe 2

    println("Solution of day11:")
    val input = readFromPath("$basePath/input.txt")
    measureTimedValue { part1(input) }.let { (part1, time) ->
        println("Part 1: $part1 took $time")
    }
    measureTimedValue { part2(input) }.let { (part2, time) ->
        println("Part 2: $part2 took $time")
    }
}

fun part1(input: Sequence<String>): Long = input.toServers().findNumOfPaths("you", "out")

fun part2(input: Sequence<String>): Long {
    val servers = input.toServers()
    val svrToFft = servers.findNumOfPaths("svr", "fft")
    val svrToDac = servers.findNumOfPaths("svr", "dac")
    val fftToDac = servers.findNumOfPaths("fft", "dac")
    val dacToFft = servers.findNumOfPaths("dac", "fft")
    val fftToOut = servers.findNumOfPaths("fft", "out")
    val dacToOut = servers.findNumOfPaths("dac", "out")
    return (svrToFft * fftToDac * dacToOut) + (svrToDac * dacToFft * fftToOut)
}

fun Map<String, List<String>>.findNumOfPaths(
    start: String,
    end: String,
    cache: MutableMap<String, Long> = mutableMapOf(),
): Long =
    cache.getOrPut(start) {
        if (start == end) return 1L
        this[start]?.sumOf {
            findNumOfPaths(it, end, cache)
        } ?: 0L
    }

fun Sequence<String>.toServers(): Map<String, List<String>> =
    map {
        it.split(':').let { (incoming, outgoing) ->
            incoming to outgoing.trim().split(' ')
        }
    }.toMap()
