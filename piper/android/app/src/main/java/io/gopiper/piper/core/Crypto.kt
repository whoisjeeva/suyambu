package io.gopiper.piper.core

import android.util.Base64
import kotlin.math.ceil


fun String.singleEncode(): String {
    var part1 = ""
    var part2 = ""
    var count = 0
    for (c in this) {
        if (count % 2 == 0) {
            part1 += c
        } else {
            part2 += c
        }
        count += 1
    }

    return part1 + part2
}

fun String.singleDecode(): String {
    var part1 = ""
    var part2 = ""
    var o = ""
    var count = 0
    val partCount = ceil(this.length / 2f).toInt()
    for (c in this) {
        if (count < partCount) {
            part1 += c
        } else {
            part2 += c
        }
        count += 1
    }

    count = 0
    var part1Count = 0
    var part2Count = 0
    for (c in this) {
        if (count % 2 == 0) {
            o += part1[part1Count]
            part1Count += 1
        } else {
            o += part2[part2Count]
            part2Count += 1
        }
        count += 1
    }
    return o
}

fun String.encode(): String {
    val data = toByteArray(charset("UTF-8"))
    val o = Base64.encodeToString(data, Base64.NO_WRAP)
    return o.reversed()
}


fun String.decode(): String {
    val o = this.reversed()
    return String(Base64.decode(o, Base64.NO_WRAP), charset("UTF-8"))
}