package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception
import kotlin.math.sqrt

fun subsequenceFromStartFromEnd(sequence: String, at1: Int, at2: Int): String {
    val start = at1
    val end = sequence.length - 1 - at2 + 1
    return sequence.slice(start until end)
}

fun subsequenceFromStartLast(sequence: String, at1: Int): String {
    val start = at1
    val end = sequence.length - 1 + 1
    return sequence.slice(start until end)
}

fun subsequenceFromEndFromStart(sequence: String, at1: Int, at2: Int): String {
    val start = sequence.length - 1 - at1
    val end = at2 + 1
    return sequence.slice(start until end)
}

fun subsequenceFromEndFromEnd(sequence: String, at1: Int, at2: Int): String {
    val start = sequence.length - 1 - at1
    val end = sequence.length - 1 - at2 + 1
    return sequence.slice(start until end)
}

fun subsequenceFromEndLast(sequence: String, at1: Int): String {
    val start = sequence.length - 1 - at1
    val end = sequence.length - 1 + 1
    return sequence.slice(start until end)
}

fun subsequenceFirstFromEnd(sequence: String, at2: Int): String {
    val start = 0
    val end = sequence.length - 1 - at2 + 1
    return sequence.slice(start until end)
}


suspend fun Piper.executeTextGetSubstring(statement: JSONObject, onError: (e: Exception) -> Unit): String? {
    try {
        Log.d("hello", "execute_text_getSubstring")
        val pointer = statement.getJSONObject("pointer")
        val value = executeStatement(pointer["value"].toString()).toString()
        val at1 = executeStatement(pointer["at1"].toString()).toString().toInt()
        val at2 = executeStatement(pointer["at2"].toString()).toString().toInt()

        val action1 = pointer["action1"].toString()
        val action2 = pointer["action2"].toString()


        return if (action1 === "FROM_START" && action2 === "FROM_START") {
            value.slice(at1-1 until at2)
        } else if (action1 === "FROM_START" && action2 === "FROM_END") {
            subsequenceFromStartFromEnd(value, at1-1, at2-1)
        } else if (action1 === "FROM_START" && action2 === "LAST") {
            subsequenceFromStartLast(value, at1-1)
        } else if (action1 === "FROM_END" && action2 === "FROM_START") {
            subsequenceFromEndFromStart(value, at1-1, at2-1)
        } else if (action1 === "FROM_END" && action2 === "FROM_END") {
            subsequenceFromEndFromEnd(value, at1-1, at2-1)
        } else if (action1 === "FROM_END" && action2 === "LAST") {
            subsequenceFromEndLast(value, at1-1)
        } else if (action1 === "FIRST" && action2 === "FROM_START") {
            value.slice(0 until at2)
        } else if (action1 === "FIRST" && action2 === "FROM_END") {
            subsequenceFirstFromEnd(value, at2-1)
        } else {
            value
        }
    } catch (e: Exception) {
        onError(e)
    }
    return null
}