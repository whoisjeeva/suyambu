package io.gopiper.piper.engine.statements

import io.gopiper.piper.cheese.js
import io.gopiper.piper.engine.Piper
import me.gumify.hiper.util.onUiThread
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeExecuteJavascript(statement: JSONObject, onError: (e: Exception) -> Unit) {
    try {
        val pointer = executeStatement(statement["pointer"].toString()).toString()
        browser.currentTab.value?.webView?.js(pointer)
    } catch (e: Exception) {
        onError(e)
    }
}