package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception
import kotlin.math.max
import kotlin.math.min

suspend fun Piper.executeMathConstrain(statement: JSONObject, onError: (e: Exception) -> Unit): Any? {
    try {
        Log.d("hello", "execute_math_constrain")
        val pointer = statement.getJSONObject("pointer")
        val value = executeStatement(pointer["value"].toString()) ?: return null
        val minValue = executeStatement(pointer["min"].toString()) ?: return null
        val maxValue = executeStatement(pointer["max"].toString()) ?: return null

        val valueDouble = value.toString().toDouble()
        val minDouble = minValue.toString().toDouble()
        val maxDouble = maxValue.toString().toDouble()

        val out = max(min(valueDouble, maxDouble), minDouble)

        return if (out.toString().endsWith(".0")) {
            out.toInt()
        } else {
            out
        }
    } catch (e: Exception) {
        onError(e)
        return null
    }
}