package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeTextCharAt(statement: JSONObject, onError: (e: Exception) -> Unit): String? {
    try {
        Log.d("hello", "execute_text_charAt")
        val pointer = statement.getJSONObject("pointer")
        val value = executeStatement(pointer["value"].toString())?.toString() ?: return null
        val at = executeStatement(pointer["at"].toString())?.toString()?.toInt() ?: return null

        return when (pointer["action"].toString()) {
            "FROM_START" -> value[at - 1].toString()
            "FROM_END" -> value[value.length - at].toString()
            "FIRST" -> value.first().toString()
            "LAST" -> value.last().toString()
            "RANDOM" -> value.random().toString()
            else -> null
        }
    } catch (e: Exception) {
        onError(e)
    }
    return null
}