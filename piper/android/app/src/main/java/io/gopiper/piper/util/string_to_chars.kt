package io.gopiper.piper.util

fun String.toCharList(): List<String> {
    val out = ArrayList<String>()
    for (c in this) {
        out.add(c.toString())
    }
    return out
}