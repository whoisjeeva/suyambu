package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import io.gopiper.piper.util.toCharList
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeListsSplit(statement: JSONObject, onError: (e: Exception) -> Unit): Any? {
    try {
        Log.d("hello", "execute_lists_split")
        val pointer = statement.getJSONObject("pointer")
        val value = executeStatement(pointer["value"].toString())
        val delim = executeStatement(pointer["delim"].toString()).toString()
        val mode = pointer["mode"].toString()

        if (mode == "SPLIT") {
            if (delim == "") {
                return value?.toString()?.toCharList()
            }
            return value?.toString()?.split(delim)
        } else if (mode == "JOIN") {
            return (value as? List<Any?>)?.joinToString(delim)
        }
        return null
    } catch (e: Exception) {
        onError(e)
        return null
    }
}