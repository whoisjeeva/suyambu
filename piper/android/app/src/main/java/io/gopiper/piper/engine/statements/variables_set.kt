package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeVariablesSet(statement: JSONObject, onError: (e: Exception) -> Unit) {
    try {
        Log.d("hello", "execute_variables_set")
        val pointer = statement.getJSONObject("pointer")
        val variableName = pointer["variable"].toString()
        val value = executeStatement(pointer["value"].toString())

        VARIABLE_STACK[variableName] = value
    } catch (e: Exception) {
        onError(e)
    }
}