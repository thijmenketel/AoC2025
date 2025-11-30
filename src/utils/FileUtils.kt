package utils

import kotlin.io.path.Path
import kotlin.io.path.readText

fun readFromPath(path: String) = Path("src/$path").readText().trim().lineSequence()
