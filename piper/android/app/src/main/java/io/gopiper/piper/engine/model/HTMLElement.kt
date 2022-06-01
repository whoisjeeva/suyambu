package io.gopiper.piper.engine.model

import java.util.*

data class HTMLElement(
    val selector: String,
    val elIndex: Int,
    val attrs: List<Attr>
) {
    override fun toString(): String {
        return "HTMLElement(id: ${UUID.randomUUID()})"
    }
}
