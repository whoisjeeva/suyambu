package io.gopiper.piper.util

fun isPrime(n: Int): Boolean {
    if (n == 1) {
        return false
    }
    for (i in 2 until n) {
        if (n % i == 0) return false
    }
    return true
}