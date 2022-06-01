package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import me.gumify.hiper.util.onUiThread
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeSwitchTab(statement: JSONObject, onError: (e: Exception) -> Unit) {
    try {
        Log.d("hello", "execute_switch_tab")
        val pointer = statement["pointer"].toString()
        val index = executeStatement(pointer).toString().toInt() - 1

        onUiThread {
            try {
                browser.switchTab(index)
            } catch (e: Exception) {
                onError(e)
            }
        }
    } catch (e: Exception) {
        onError(e)
    }
}