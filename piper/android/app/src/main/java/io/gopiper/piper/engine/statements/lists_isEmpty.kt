package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeListsIsEmpty(statement: JSONObject, onError: (e: Exception) -> Unit): Boolean? {
    try {
        Log.d("hello", "execute_lists_isEmpty")
        val pointer = statement["pointer"].toString()
        val list = executeStatement(pointer) as? List<Any?> ?: return null
        return list.isEmpty()
    } catch (e: Exception) {
        onError(e)
        return null
    }
}