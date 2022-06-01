package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception
import kotlin.math.*

suspend fun Piper.executeMathSingle(statement: JSONObject, onError: (e: Exception) -> Unit): Any? {
    try {
        Log.d("hello", "execute_math_single")
        val pointer = statement.getJSONObject("pointer")
        val op = pointer["op"].toString()
        val value = executeStatement(pointer["value"].toString())

        val valueDouble = value.toString().toDouble()
        val valueInt = value.toString().toInt()

        return when (op) {
            "ROOT" -> sqrt(valueDouble)
            "ABS" -> if (value is Double) abs(value) else abs(valueInt)
            "NEG" -> if (value is Double) value.unaryMinus() else valueInt.unaryMinus()
            "LN" -> ln(valueDouble)
            "LOG10" -> log10(valueDouble)
            "EXP" -> exp(valueDouble)
            "POW10" -> 10.0.pow(valueDouble)
            else -> null
        }
    } catch (e: Exception) {
        onError(e)
        return null
    }
}