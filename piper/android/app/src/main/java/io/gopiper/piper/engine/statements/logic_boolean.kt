package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeLogicBoolean(statement: JSONObject, onError: (e: Exception) -> Unit): Boolean {
    return try {
        Log.d("hello", "execute_logic_boolean")
        statement.getBoolean("pointer")
    } catch (e: Exception) {
        onError(e)
        false
    }
}