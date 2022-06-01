package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeMathModulo(statement: JSONObject, onError: (e: Exception) -> Unit): Any? {
    try {
        Log.d("hello", "execute_math_modulo")
        val pointer = statement.getJSONObject("pointer")
        val value1 = executeStatement(pointer["value1"].toString())
        val value2 = executeStatement(pointer["value2"].toString())

        val out = value1.toString().toDouble() % value2.toString().toDouble()
        return if (out.toString().endsWith(".0")) {
            out.toInt()
        } else {
            out
        }
    } catch (e: Exception) {
        onError(e)
    }
    return null
}