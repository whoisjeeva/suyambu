package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeLogicOperation(statement: JSONObject, onError: (e: Exception) -> Unit): Boolean {
    try {
        Log.d("hello", "execute_logic_operation")
        val pointer = statement.getJSONObject("pointer")
        val op = pointer.getString("op")
        val value1 = executeStatement(pointer["value1"].toString())
        val value2 = executeStatement(pointer["value2"].toString())

        if (op == "&&") {
            return value1 as Boolean && value2 as Boolean
        } else if (op == "||") {
            return value1 as Boolean || value2 as Boolean
        }
        return false
    } catch (e: Exception) {
        onError(e)
        return false
    }
}