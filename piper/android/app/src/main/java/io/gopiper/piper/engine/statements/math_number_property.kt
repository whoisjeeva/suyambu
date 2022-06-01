package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import io.gopiper.piper.util.isPrime
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeMathNumberProperty(statement: JSONObject, onError: (e: Exception) -> Unit): Boolean {
    try {
        Log.d("hello", "execute_math_number_property")
        val pointer = statement.getJSONObject("pointer")
        val value1 = executeStatement(pointer["value1"].toString())
        val prop = pointer["property"].toString()

        val value1Double = value1.toString().toDouble()

        return when (prop) {
            "EVEN" -> value1Double % 2.0 == 0.0
            "ODD" -> value1Double % 2.0 != 0.0
            "PRIME" -> if (value1 is Double) false else isPrime(value1.toString().toInt())
            "WHOLE" -> value1Double % 1 == 0.0
            "POSITIVE" -> value1Double > 0
            "NEGATIVE" -> value1Double < 0
            "DIVISIBLE_BY" -> value1Double % executeStatement(pointer["value2"].toString()).toString().toInt() == 0.0
            else -> false
        }
    } catch (e: Exception) {
        onError(e)
        return false
    }
}