package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeListsIndexOf(statement: JSONObject, onError: (e: Exception) -> Unit): Int? {
    try {
        Log.d("hello", "execute_lists_indexOf")
        val pointer = statement.getJSONObject("pointer")
        val list = executeStatement(pointer["list"].toString())  as? List<Any?> ?: return null
        val item = executeStatement(pointer["item"].toString())

        return when (pointer["indexOf"].toString()) {
            "FIRST" -> list.indexOf(item) + 1
            "LAST" -> list.lastIndexOf(item) + 1
            else -> -1
        }
    } catch (e: Exception) {
        onError(e)
        return null
    }
}