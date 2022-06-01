package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeMathChange(statement: JSONObject, onError: (e: Exception) -> Unit) {
    try {
        Log.d("hello", "execute_math_change")
        val pointer = statement.getJSONObject("pointer")
        val variableName = pointer["variable"].toString()
        val value = executeStatement(pointer["value"].toString())

        if (VARIABLE_STACK.containsKey(variableName) && (VARIABLE_STACK[variableName] is Int || VARIABLE_STACK[variableName] is Double) && (value is Int || value is Double) ) {
            if (VARIABLE_STACK[variableName] is Double || value is Double) {
                VARIABLE_STACK[variableName] = VARIABLE_STACK[variableName].toString().toDouble() + value.toString().toDouble()
            } else {
                VARIABLE_STACK[variableName] = VARIABLE_STACK[variableName].toString().toInt() + value.toString().toInt()
            }
        } else {
            VARIABLE_STACK[variableName] = value
        }
    } catch (e: Exception) {
        onError(e)
    }
}