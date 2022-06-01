package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeListsRepeat(statement: JSONObject, onError: (e: Exception) -> Unit): List<Any?>? {
    try {
        Log.d("hello", "execute_lists_repeat")
        val pointer = statement.getJSONObject("pointer")
        val times = executeStatement(pointer["times"].toString())?.toString()?.toInt() ?: return null
        val value = executeStatement(pointer["value"].toString())
        val output = ArrayList<Any?>()
        for (i in 0 until times) {
            output.add(value)
        }
        return output
    } catch (e: Exception) {
        onError(e)
    }
    return null
}