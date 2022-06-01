package io.gopiper.piper.engine.statements

import android.util.Log
import kotlinx.coroutines.delay
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeWaitFor(statement: JSONObject, onError: (e: Exception) -> Unit) {
    try {
        Log.d("hello", "execute_wait_for")
        val secs = statement["pointer"].toString().toDouble()
        delay((secs * 1000.0).toLong())
    } catch (e: Exception) {
        onError(e)
    }
}