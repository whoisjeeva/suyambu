package io.gopiper.piper.engine.statements

import android.util.Log
import android.view.KeyCharacterMap
import io.gopiper.piper.cheese.js
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import io.gopiper.piper.engine.Piper
import io.gopiper.piper.util.isAscii
import org.json.JSONObject

suspend fun Piper.executeType(statement: JSONObject, onError: (e: Exception) -> Unit) {
    try {
        Log.d("hello", "execute_type")
        val value = executeStatement(statement["pointer"].toString()).toString()

        if (value.isAscii()) {
            val chars = value.toCharArray()
            val charMap = KeyCharacterMap.load(KeyCharacterMap.VIRTUAL_KEYBOARD)
            val events = charMap.getEvents(chars)
            var isError = false

            for (e in events){
                CoroutineScope(Dispatchers.Main).launch {
                    try {
                        browser.currentTab.value?.webView?.dispatchKeyEvent(e)
                    } catch (e: Exception) {
                        isError = true
                        onError(e)
                    }
                }.join()
                if (isError) break
                delay(10)
            }
        } else {
            for (c in value) {
                browser.currentTab.value?.webView?.js("""
                    if (document.activeElement.value === undefined) {
                        document.execCommand('insertText', false, "${if (c == '"') "\\\"" else c }");
                    } else {
                        document.activeElement.value += "${if (c == '"') "\\\"" else c }";
                    }
                """.trimIndent())
                delay(10)
            }
        }
    } catch (e: Exception) {
        onError(e)
    }
}