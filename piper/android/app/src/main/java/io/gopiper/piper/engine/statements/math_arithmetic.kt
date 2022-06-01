package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception
import kotlin.math.pow

suspend fun Piper.executeMathArithmetic(statement: JSONObject, onError: (e: Exception) -> Unit): Any {
    try {
        Log.d("hello", "execute_math_arithmetic")
        val pointer = statement.getJSONObject("pointer")
        val op = pointer["op"].toString()
        val value1 = executeStatement(pointer["value1"].toString())
        val value2 = executeStatement(pointer["value2"].toString())

        val value1Double = value1.toString().toDouble()
        val value1Int = value1.toString().toInt()

        val value2Double = value2.toString().toDouble()
        val value2Int = value2.toString().toInt()

        val output = if (value1 is Double || value2 is Double) {
            when (op) {
                "ADD" -> value1Double + value2Double
                "MINUS" -> value1Double - value2Double
                "MULTIPLY" -> value1Double * value2Double
                "DIVIDE" -> value1Double / value2Double
                "POWER" -> value1Double.pow(value2Double)
                else -> 0
            }
        } else {
            when (op) {
                "ADD" -> value1Int + value2Int
                "MINUS" -> value1Int - value2Int
                "MULTIPLY" -> value1Int * value2Int
                "DIVIDE" -> value1Int / value2Int
                "POWER" -> value1Double.pow(value2Double)
                else -> 0
            }
        }

        if (output.toString().endsWith(".0")) {
            return output.toInt()
        }
        return output
    } catch (e: Exception) {
        return onError(e)
    }
}