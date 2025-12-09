@file:Suppress("ktlint:standard:filename")

package day08

import utils.readFromPath
import utils.shouldBe
import kotlin.time.measureTimedValue

fun main() {
    val basePath = "day08/data"

    // TEST for part 1
    part1(readFromPath("$basePath/test-input.txt"), 10) shouldBe 40

    // TEST for part 2
    part2(readFromPath("$basePath/test-input.txt")) shouldBe 25272

    println("Solution of day08:")
    val input = readFromPath("$basePath/input.txt")
    measureTimedValue { part1(input, 1000) }.let { (part1, time) ->
        println("Part 1: $part1 took $time")
    }
    measureTimedValue { part2(input) }.let { (part2, time) ->
        println("Part 2: $part2 took $time")
    }
}

fun part1(
    input: Sequence<String>,
    connections: Int,
): Int {
    val points = input.toPoints()
    val distances = points.getDistances()
    val networks = points.map { mutableSetOf(it) }.toMutableSet()
    distances.sortedBy { it.distance }.take(connections).forEach { (first, second) ->
        networks.connect(first, second)
    }

    return networks
        .map { it.size }
        .sortedDescending()
        .take(3)
        .reduce { a, b -> a * b }
}

fun part2(input: Sequence<String>): Long {
    val points = input.toPoints()
    val distances = points.getDistances()
    val networks = points.map { mutableSetOf(it) }.toMutableSet()
    distances.sortedBy { it.distance }.forEach { (first, second) ->
        networks.connect(first, second)
        if (networks.size == 1) return first.x * second.x
    }
    return 0
}

data class Edge(
    val point1: Point,
    val point2: Point,
    val distance: Long,
)

data class Point(
    val x: Long,
    val y: Long,
    val z: Long,
)

fun MutableSet<MutableSet<Point>>.connect(
    p1: Point,
    p2: Point,
) {
    val set1 = find { p1 in it }
    val set2 = find { p2 in it }
    if (set1 != null && set2 != null && set1 !== set2) {
        add((set1 union set2) as MutableSet<Point>)
        remove(set1)
        remove(set2)
    }
}

fun List<Point>.getDistances(): List<Edge> =
    mapIndexed { i, p1 ->
        drop(i + 1).map { p2 -> Edge(p1, p2, p1 distanceTo p2) }
    }.flatten()

fun Sequence<String>.toPoints() =
    map {
        it.split(",").let { p -> Point(p[0].toLong(), p[1].toLong(), p[2].toLong()) }
    }.toList()

infix fun Point.distanceTo(other: Point) = (x - other.x).squared() + (y - other.y).squared() + (z - other.z).squared()

fun Long.squared() = this * this
