package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeTextTrim(statement: JSONObject, onError: (e: Exception) -> Unit): String? {
    try {
        Log.d("hello", "execute_text_trim")
        val pointer = statement.getJSONObject("pointer")
        val value = executeStatement(pointer["value"].toString())?.toString() ?: return null
        val action = pointer["action"].toString()

        return when (action) {
            "LEFT" -> value.trimStart()
            "RIGHT" -> value.trimEnd()
            "BOTH" -> value.trim()
            else -> value
        }
    } catch (e: Exception) {
        onError(e)
    }
    return null
}