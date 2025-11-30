package utils

/**
 * Caches the result of a function so that subsequent calls with the same parameter
 * do not recompute the result but instead return the cached value.
 *
 * Example:
 * ```
 * val factorial = memoize<Int, Long> { n ->
 *     if (n <= 1) 1
 *     else n * factorial(n - 1)
 * }
 * println(factorial(5)) // Computes and caches 5!
 * println(factorial(5)) // Returns cached result
 * ```
 *
 * @param function The function to be memoized. This function takes a parameter of type P
 *                 and returns a result of type R.
 * @return A memoized version of the provided function that caches its results
 *         based on the input parameter.
 */
fun <P, R> memoize(function: (P) -> R): (P) -> R {
    val cache = mutableMapOf<P, R>()
    return { cache.getOrPut(it) { function(it) } }
}
