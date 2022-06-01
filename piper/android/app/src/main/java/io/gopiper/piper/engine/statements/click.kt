package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import io.gopiper.piper.engine.getXpath
import io.gopiper.piper.engine.model.HTMLElement
import io.gopiper.piper.engine.waitForElementLimited
import kotlinx.coroutines.delay
import me.gumify.hiper.util.onUiThread
import org.json.JSONObject

suspend fun Piper.executeClick(statement: JSONObject, onError: (e: Exception) -> Unit) {
    try {
        Log.d("hello", "execute_click")
        val pointer = statement.getJSONObject("pointer")
        val res = executeStatement(pointer["elements"].toString())
        val elements = if (res is List<*>) {
            res as List<HTMLElement>
        } else {
            listOf(res as HTMLElement)
        }
        val op = pointer.getString("op")
        for (el in elements) {
            val isFound = if (op == "WAIT") waitForElementLimited(browser, el) else true
            val xpath = el.attrs.getXpath()
            if (op == "WAIT") delay(1000)
            if (isFound) {
                lastClickedElement = el
                onUiThread {
                    browser.currentTab.value?.webView?.evaluateJavascript("""
                        var el = document.querySelectorAll("${el.selector}")[${el.elIndex}];
                        if (el === undefined) {
                            el = document.evaluate('${xpath}', document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;
                        }
                        if (el.tagName.toLowerCase() === "svg") {
                            el.parentElement.style.boxShadow = "0 0 0 2px yellow";
                            el.parentElement.focus();
                            el.parentElement.click();
                        } else {
                            el.style.boxShadow = "0 0 0 2px yellow";
                            el.focus();
                            el.click();
                        }
                    """.trimIndent()) {}
                }
            } else {
                throw Exception("HTMLElement not found")
            }
        }
    } catch (e: Exception) {
        onError(e)
    }
}