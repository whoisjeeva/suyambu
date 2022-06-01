package io.gopiper.piper.util

import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale

fun String.titlecase(): String {
    val nodes = this.split(" ")
    val out = ArrayList<String>()

    for (node in nodes) {
        out.add(node.capitalize(Locale.current))
    }

    return out.joinToString(" ")
}