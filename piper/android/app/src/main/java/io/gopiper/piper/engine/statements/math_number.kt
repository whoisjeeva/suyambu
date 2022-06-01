package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeMathNumber(statement: JSONObject, onError: (e: Exception) -> Unit): Any? {
    return try {
        Log.d("hello", "execute_math_number")
        statement["pointer"]
    } catch (e: Exception) {
        onError(e)
        null
    }
}