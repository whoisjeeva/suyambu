package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeLogicTernary(statement: JSONObject, onError: (e: Exception) -> Unit): Any? {
    try {
        Log.d("hello", "execute_logic_ternary")
        val pointer = statement.getJSONObject("pointer")
        val cond = executeStatement(pointer["cond"].toString()) ?: false
        if (cond as Boolean) {
            return executeStatement(pointer["value1"].toString())
        }
        return executeStatement(pointer["value2"].toString())
    } catch (e: Exception) {
        return onError(e)
    }
}