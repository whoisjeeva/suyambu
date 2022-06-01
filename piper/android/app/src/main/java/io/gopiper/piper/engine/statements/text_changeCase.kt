package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import io.gopiper.piper.util.titlecase
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeTextChangeCase(statement: JSONObject, onError: (e: Exception) -> Unit): String? {
    try {
        Log.d("hello", "execute_text_changeCase")
        val pointer = statement.getJSONObject("pointer")
        val value = executeStatement(pointer["value"].toString())?.toString() ?: return null

        return when (pointer["action"].toString()) {
            "UPPERCASE" -> value.uppercase()
            "LOWERCASE" -> value.lowercase()
            "TITLECASE" -> value.titlecase()
            else -> value
        }
    } catch (e: Exception) {
        onError(e)
        return null
    }
}