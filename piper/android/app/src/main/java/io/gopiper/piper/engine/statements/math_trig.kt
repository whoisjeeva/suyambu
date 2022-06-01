package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception
import kotlin.math.*

suspend fun Piper.executeMathTrig(statement: JSONObject, onError: (e: Exception) -> Unit): Any? {
    try {
        Log.d("hello", "execute_math_trig")
        val pointer = statement.getJSONObject("pointer")
        val op = pointer["op"].toString()
        val value = executeStatement(pointer["value"].toString())

        val valueDouble = value.toString().toDouble()

        return when (op) {
            "SIN" -> sin(valueDouble / 180 * Math.PI)
            "COS" -> cos(valueDouble / 180 * Math.PI)
            "TAN" -> tan(valueDouble / 180 * Math.PI)
            "ASIN" -> asin(valueDouble) / Math.PI * 180
            "ACOS" -> acos(valueDouble) / Math.PI * 180
            "ATAN" -> atan(valueDouble) / Math.PI * 180
            else -> null
        }
    } catch (e: Exception) {
        onError(e)
        return null
    }
}