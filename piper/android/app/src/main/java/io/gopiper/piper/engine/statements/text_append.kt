package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeTextAppend(statement: JSONObject, onError: (e: Exception) -> Unit) {
    try {
        Log.d("hello", "execute_text_append")
        val pointer = statement.getJSONObject("pointer")
        val variableName = pointer["variable"].toString()
        val text = executeStatement(pointer["text"].toString()).toString()

        if (VARIABLE_STACK.containsKey(variableName)) {
            VARIABLE_STACK[variableName] = VARIABLE_STACK[variableName].toString() + text
        } else {
            VARIABLE_STACK[variableName] = text
        }
    } catch (e: Exception) {
        onError(e)
    }
}