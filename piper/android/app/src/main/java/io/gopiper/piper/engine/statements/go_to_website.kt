package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import kotlinx.coroutines.delay
import me.gumify.hiper.util.onUiThread
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeGoToWebsite(statement: JSONObject, onError: (e: Exception) -> Unit) {
    try {
        Log.d("hello", "execute_go_to_website")
        val pointer = statement["pointer"].toString()
        val url = executeStatement(pointer).toString()

        onUiThread {
            try {
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    loadUrl(url)
                } else {
                    loadUrl("http://$url")
                }
            } catch (e: Exception) {
                onError(e)
            }
        }
        delay(500)
    } catch (e: Exception) {
        onError(e)
    }
}