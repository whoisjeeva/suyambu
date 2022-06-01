package io.gopiper.piper.engine.statements

import android.util.Log
import kotlinx.coroutines.delay
import io.gopiper.piper.cheese.js
import io.gopiper.piper.engine.Piper
import me.gumify.hiper.util.onUiThread
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executePageSource(statement: JSONObject, onError: (e: Exception) -> Unit): String? {
    try {
        Log.d("hello", "execute_page_source")
        var source: String? = null
        onUiThread {
            try {
                browser.currentTab.value?.webView?.js("""
                    return document.documentElement.outerHTML;
                """.trimIndent()) {
                    source = it
                }
            } catch (e: Exception) {
                onError(e)
            }
        }
        while (source == null) {
            delay(100)
        }
        return source
    } catch (e: Exception) {
        onError(e)
        return null
    }
}