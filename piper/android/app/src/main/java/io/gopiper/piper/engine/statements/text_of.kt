package io.gopiper.piper.engine.statements

import android.util.Log
import kotlinx.coroutines.delay
import io.gopiper.piper.cheese.js
import io.gopiper.piper.engine.Piper
import io.gopiper.piper.engine.getXpath
import io.gopiper.piper.engine.model.HTMLElement
import io.gopiper.piper.engine.waitForAllElementsUnlimited
import io.gopiper.piper.engine.waitForElementUnlimited
import me.gumify.hiper.util.onUiThread
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeTextOf(statement: JSONObject, onError: (e: Exception) -> Unit): Any? {
    try {
        Log.d("hello", "execute_text_of")
        val pointer = statement.getJSONObject("pointer")
        val elsObj = executeStatement(pointer["elements"].toString())

        val elements = if (elsObj is List<*>) {
            elsObj as List<HTMLElement>
        } else {
            listOf(elsObj as HTMLElement)
        }

        val op = pointer.getString("op")

        val els = when (op) {
            "FIRST" -> {
                waitForElementUnlimited(browser, elements.first())
                listOf(elements.first())
            }
            "LAST" -> {
                waitForElementUnlimited(browser, elements.last())
                listOf(elements.last())
            }
            else -> {
                waitForAllElementsUnlimited(browser, elements)
                elements
            }
        }

        val texts = ArrayList<String?>()
        for (el in els) {
            var isDone = false
            val xpath = el.attrs.getXpath()
            onUiThread {
                browser.currentTab.value?.webView?.js("""
                    var el = document.querySelectorAll("${el.selector}")[${el.elIndex}];
                    if (el === undefined) {
                        el = document.evaluate('${xpath}', document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;
                    }
                    if (el === undefined || el === null) {
                        return null;
                    }
                    return el.innerText;
                """.trimIndent()) {
                    val text = if (it == "null") null else it
                    texts.add(text)
                    isDone = true
                }
            }
            while (!isDone) {
                if (isTerminated) break
                delay(5)
            }
        }

        return when (op) {
            "FIRST" -> {
                texts.first()
            }
            "LAST" -> {
                texts.last()
            }
            else -> {
                texts
            }
        }
    } catch (e: Exception) {
        onError(e)
        return null
    }
}