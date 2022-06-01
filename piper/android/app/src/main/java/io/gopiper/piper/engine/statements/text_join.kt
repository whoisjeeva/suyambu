package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeTextJoin(statement: JSONObject, onError: (e: Exception) -> Unit): String {
    try {
        Log.d("hello", "execute_text_join")
        val pointer = statement.getJSONArray("pointer")
        val values = ArrayList<String>()
        for (i in 0 until pointer.length()) {
            val value = pointer[i]
            values.add(executeStatement(value.toString()).toString())
        }
        return values.joinToString("")
    } catch (e: Exception) {
        onError(e)
        return ""
    }
}