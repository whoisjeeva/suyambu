package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeTextLength(statement: JSONObject, onError: (e: Exception) -> Unit): Int? {
    try {
        Log.d("hello", "execute_text_length")
        val pointer = statement["pointer"].toString()
        val text = executeStatement(pointer)?.toString()
        return text?.length
    } catch (e: Exception) {
        onError(e)
        return null
    }
}