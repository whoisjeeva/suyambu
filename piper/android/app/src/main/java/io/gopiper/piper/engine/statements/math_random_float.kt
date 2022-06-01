package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeMathRandomFloat(statement: JSONObject, onError: (e: Exception) -> Unit): Double {
    try {
        Log.d("hello", "execute_math_random_float")
        return Math.random()
    } catch (e: Exception) {
        onError(e)
    }
    return 0.0
}