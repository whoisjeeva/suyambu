package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeListsSort(statement: JSONObject, onError: (e: Exception) -> Unit): List<Any?>? {
    try {
        Log.d("hello", "execute_lists_sort")
        val pointer = statement.getJSONObject("pointer")
        val list = executeStatement(pointer["list"].toString()) as? ArrayList<Any?> ?: return null
        val isReversed = pointer["direction"].toString() == "-1"

        return when (pointer["type"].toString()) {
            "NUMERIC" -> {
                val s = list.sortedBy { it.toString().toDouble() }
                if (isReversed) return s.reversed()
                return s
            }
            "TEXT" -> {
                val s = list.sortedBy { it.toString() }
                if (isReversed) return s.reversed()
                return s
            }
            "IGNORE_CASE" -> {
                val s = list.sortedBy { it.toString().lowercase() }
                if (isReversed) return s.reversed()
                return s
            }
            else -> null
        }
    } catch (e: Exception) {
        onError(e)
        return null
    }
}