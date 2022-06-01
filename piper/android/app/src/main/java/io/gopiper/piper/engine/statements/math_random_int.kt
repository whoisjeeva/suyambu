package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeMathRandomInt(statement: JSONObject, onError: (e: Exception) -> Unit): Int? {
    try {
        Log.d("hello", "execute_math_random_int")
        val pointer = statement.getJSONObject("pointer")
        val minValue = executeStatement(pointer["min"].toString()) ?: return null
        val maxValue = executeStatement(pointer["max"].toString()) ?: return null

        return (minValue.toString().toInt()..maxValue.toString().toInt()).random()
    } catch (e: Exception) {
        onError(e)
        return null
    }
}