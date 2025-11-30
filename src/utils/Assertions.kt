package utils

infix fun <T> T.shouldBe(expected: T): Nothing? =
    takeIf { it != expected }
        ?.run { error("Assertion failed: expected $expected, actual $this") }
