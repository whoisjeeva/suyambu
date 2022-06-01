package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeListsLength(statement: JSONObject, onError: (e: Exception) -> Unit): Int? {
    try {
        Log.d("hello", "execute_lists_length")
        val pointer = statement["pointer"].toString()
        val list = executeStatement(pointer) as? List<Any?> ?: return null
        return list.size
    } catch (e: Exception) {
        onError(e)
        return null
    }
}