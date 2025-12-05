package utils

import kotlin.io.path.Path
import kotlin.io.path.readText

fun readFromPath(path: String) = readRawInput(path).lineSequence()

fun readRawInput(path: String) = Path("src/$path").readText().trim()
