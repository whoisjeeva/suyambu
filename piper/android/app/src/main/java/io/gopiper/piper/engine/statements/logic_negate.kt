package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeLogicNegate(statement: JSONObject, onError: (e: Exception) -> Unit): Boolean {
    try {
        Log.d("hello", "execute_logic_negate")
        val pointer = statement.get("pointer").toString()
        val value = executeStatement(pointer)
        if (value is Boolean) {
            return !value
        }
        return false
    } catch (e: Exception) {
        onError(e)
        return false
    }
}