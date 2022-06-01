package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeTextIsEmpty(statement: JSONObject, onError: (e: Exception) -> Unit): Boolean? {
    try {
        Log.d("hello", "execute_text_isEmpty")
        val pointer = statement["pointer"].toString()
        val text = executeStatement(pointer)?.toString()
        return text?.isEmpty()
    } catch (e: Exception) {
        onError(e)
        return null
    }
}