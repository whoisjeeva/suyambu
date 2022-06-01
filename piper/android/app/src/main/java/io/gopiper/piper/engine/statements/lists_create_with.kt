package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeListsCreateWith(statement: JSONObject, onError: (e: Exception) -> Unit): ArrayList<Any?> {
    try {
        Log.d("hello", "execute_lists_create_with")
        val pointer = statement.getJSONArray("pointer")
        val output = ArrayList<Any?>()
        for (i in 0 until pointer.length()) {
            val item = executeStatement(pointer[i].toString())
            output.add(item)
        }
        return output
    } catch (e: Exception) {
        onError(e)
        return ArrayList()
    }
}