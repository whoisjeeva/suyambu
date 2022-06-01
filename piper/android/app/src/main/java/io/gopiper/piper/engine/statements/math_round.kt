package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt

suspend fun Piper.executeMathRound(statement: JSONObject, onError: (e: Exception) -> Unit): Any? {
    try {
        Log.d("hello", "execute_math_round")
        val pointer = statement.getJSONObject("pointer")
        val op = pointer["op"].toString()
        val value = executeStatement(pointer["value"].toString()) ?: return null

        val valueDouble = value.toString().toDouble()

        val out = when (op) {
            "ROUND" -> valueDouble.roundToInt()
            "ROUNDUP" -> ceil(valueDouble)
            "ROUNDDOWN" -> floor(valueDouble)
            else -> null
        } ?: return null

        return out
    } catch (e: Exception) {
        onError(e)
    }
    return null
}