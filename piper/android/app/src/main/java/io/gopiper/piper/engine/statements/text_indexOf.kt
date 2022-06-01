package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeTextIndexOf(statement: JSONObject, onError: (e: Exception) -> Unit): Int {
    try {
        Log.d("hello", "execute_text_indexOf")
        val pointer = statement.getJSONObject("pointer")
        val value = executeStatement(pointer["value"].toString())?.toString() ?: return 0
        val substring = executeStatement(pointer["substring"].toString())?.toString() ?: return 0
        val indexOf = pointer["indexOf"].toString()

        if (indexOf == "FIRST") {
            return value.indexOf(substring) + 1
        } else if (indexOf == "LAST") {
            return value.lastIndexOf(substring) + 1
        }
        return 0
    } catch (e: Exception) {
        onError(e)
        return 0
    }
}