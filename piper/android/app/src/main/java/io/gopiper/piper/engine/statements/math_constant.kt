package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception
import kotlin.math.sqrt

suspend fun Piper.executeMathConstant(statement: JSONObject, onError: (e: Exception) -> Unit): Any? {
    try {
        Log.d("hello", "execute_math_const")
        val pointer = statement.getJSONObject("pointer")

        return when (pointer["const"].toString()) {
            "PI" -> Math.PI
            "E" -> Math.E
            "GOLDEN_RATIO" -> (1 + sqrt(5.0)) / 2
            "SQRT2" -> sqrt(2.0)
            "SQRT1_2" -> sqrt(1.0/2.0)
            "INFINITY" -> Double.POSITIVE_INFINITY
            else -> null
        }
    } catch (e: Exception) {
        onError(e)
        return null
    }
}