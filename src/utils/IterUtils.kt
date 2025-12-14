package utils

/**
 * Lazily generate all k-combinations from a list using yield.
 * No self-use, no repetitions (indices strictly increase).
 */
fun <T> List<T>.combinations(k: Int): Sequence<List<T>> =
    sequence {
        val n = size
        if (k !in 0..n) return@sequence
        if (k == 0) {
            yield(emptyList())
            return@sequence
        }

        val indices = IntArray(k) { it } // initial [0,1,...,k-1]

        while (true) {
            // Materialize the current combination
            yield(indices.map { this@combinations[it] })

            // Compute next lexicographic combination
            var i = k - 1
            while (i >= 0 && indices[i] == i + n - k) i--
            if (i < 0) break

            indices[i]++
            for (j in i + 1 until k) {
                indices[j] = indices[j - 1] + 1
            }
        }
    }
