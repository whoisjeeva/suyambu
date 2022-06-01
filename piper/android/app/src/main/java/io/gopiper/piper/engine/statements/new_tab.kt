package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import me.gumify.hiper.util.onUiThread
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeNewTab(statement: JSONObject, onError: (e: Exception) -> Unit) {
    Log.d("hello", "execute_new_tab")
    onUiThread {
        try {
            browser.newTab("browser://blank")
        } catch (e: Exception) {
            onError(e)
        }
    }
}