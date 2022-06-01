package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import io.gopiper.piper.util.C
import me.gumify.hiper.util.onUiThread
import me.gumify.hiper.util.toast
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeTextPrint(statement: JSONObject, onError: (e: Exception) -> Unit) {
    try {
        Log.d("hello", "execute_text_print")
        val pointer = statement.get("pointer").toString()
        val value = executeStatement(pointer)
        onRegisterLog(scriptId, value.toString())
//        onUiThread { context.toast(value) }
        Log.d("hello", "PRINT: " + value.toString())
    } catch (e: Exception) {
        onError(e)
    }
}