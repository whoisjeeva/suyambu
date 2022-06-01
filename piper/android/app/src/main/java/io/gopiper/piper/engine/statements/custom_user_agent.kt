package io.gopiper.piper.engine.statements

import io.gopiper.piper.engine.Piper
import me.gumify.hiper.util.onUiThread
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeCustomUserAgent(statement: JSONObject, onError: (e: Exception) -> Unit) {
    try {
        val agent = executeStatement(statement["pointer"].toString()).toString()
        onUiThread {
            browser.currentTab.value?.webView?.settings?.userAgentString = agent
        }
    } catch (e: Exception) {
        onError(e)
    }
}