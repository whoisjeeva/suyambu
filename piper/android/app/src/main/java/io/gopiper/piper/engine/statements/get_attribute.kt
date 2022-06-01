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

suspend fun Piper.executeAttributeOf(statement: JSONObject, onError: (e: Exception) -> Unit): Any? {
    try {
        Log.d("hello", "execute_attribute_of")
        val pointer = statement.getJSONObject("pointer")
        val elsObj = executeStatement(pointer["elements"].toString())

        val elements = if (elsObj is List<*>) {
            elsObj as List<HTMLElement>
        } else {
            listOf(elsObj as HTMLElement)
        }

        val attr = pointer.getString("attr").lowercase()
        val op = pointer.getString("op")
        val attrValues: ArrayList<String?> = arrayListOf()

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

        for (el in els) {
            var isDone = false
            val xpath = el.attrs.getXpath()
            onUiThread {
                browser.currentTab.value?.webView?.js("""
                var element = document.querySelectorAll("${el.selector}")[${el.elIndex}];
                if (element === undefined) {
                    element = document.evaluate('${xpath}', document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;
                }
                if (element === undefined || element === null) {
                    return null;
                }
                element.style.boxShadow = "0 0 0 2px yellow";
                if ("$attr" == "src") {
                    return element.currentSrc;
                } else if ("$attr" == "action") {
                    return element.action;
                } else if ("$attr" == "href") {
                    return element.href;
                } else {
                    return element.getAttribute("$attr");
                }
            """.trimIndent()) {
                    val attrValue = if (it == "null") null else it
                    attrValues.add(attrValue)
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
                attrValues.first()
            }
            "LAST" -> {
                attrValues.last()
            }
            else -> {
                attrValues
            }
        }
    } catch (e: Exception) {
        onError(e)
        return null
    }
}