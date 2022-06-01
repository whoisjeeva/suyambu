package io.gopiper.piper.core

fun convert(value: Any?, callback: (Any?, Class<out Any>) -> Unit) {
    if (value is Boolean) {
        callback(value, value::class.java)
    }
}