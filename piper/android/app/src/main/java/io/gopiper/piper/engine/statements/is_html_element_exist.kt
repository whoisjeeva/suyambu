package io.gopiper.piper.engine.statements

import kotlinx.coroutines.delay
import io.gopiper.piper.cheese.js
import io.gopiper.piper.engine.Piper
import io.gopiper.piper.engine.getXpath
import io.gopiper.piper.engine.model.HTMLElement
import me.gumify.hiper.util.onUiThread
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeIsHtmlElementExist(statement: JSONObject, onError: (e: Exception) -> Unit): Boolean {
    try {
        delay(1000)
        val elsObj = executeStatement(statement["pointer"].toString())

        val elements = if (elsObj is List<*>) {
            elsObj as List<HTMLElement>
        } else {
            listOf(elsObj as HTMLElement)
        }

        var out = false
        for (el in elements) {
            var isExist: Boolean? = null
            val xpath = el.attrs.getXpath()
            onUiThread {
                browser.currentTab.value?.webView?.js("""
                var el = document.querySelectorAll("${el.selector}")[${el.elIndex}];
                if (el === undefined) {
                    el = document.evaluate('${xpath}', document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;
                }
                return (el !== undefined && el !== null);
            """.trimIndent()) {
                    isExist = it == "true"
                }
            }
            while (isExist == null) {
                if (isTerminated) break
                delay(5)
            }
            out = isExist ?: false
            if (isExist == false) {
                break
            }
        }
        return out
    } catch (e: Exception) {
        onError(e)
        return false
    }
}