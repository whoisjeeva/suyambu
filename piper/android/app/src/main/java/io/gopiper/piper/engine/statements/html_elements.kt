package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.cheese.js
import io.gopiper.piper.engine.Injector
import io.gopiper.piper.engine.Piper
import io.gopiper.piper.engine.model.Attr
import io.gopiper.piper.engine.model.HTMLElement
import kotlinx.coroutines.delay
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

fun ArrayList<HTMLElement>.addElement(json: JSONObject) {
    val attrsObj = json.getJSONArray("attrs")
    val xpath = if (json.has("xpath")) {
        json["xpath"].toString()
    } else {
        "null"
    }
    val attrs = ArrayList<Attr>()
    for (j in 0 until attrsObj.length()) {
        val attrObj = attrsObj.getJSONObject(j)
        attrs.add(Attr(attrObj.getString("name"), attrObj.getString("value")))
    }
    attrs.add(Attr("xpath", xpath))
    val el = HTMLElement(
        selector = json.getString("selector"),
        elIndex = json.getInt("elIndex"),
        attrs = attrs
    )
    add(el)
}

suspend fun Piper.executeHtmlElements(statement: JSONObject, onError: (e: Exception) -> Unit): List<HTMLElement> {
    try {
        Log.d("hello", "execute_html_elements")
        val pointer = statement.getJSONArray("pointer")
        val els = ArrayList<HTMLElement>()
        for (i in 0 until pointer.length()) {
            val elObj = pointer.getJSONObject(i)
            val isFindSimilar = elObj.getBoolean("isFindSimilar")
            var isDone = true
            val selector = elObj.getString("selector")
            val elIndex = elObj.getInt("elIndex")
            val xpath = if (elObj.has("xpath")) {
                elObj["xpath"].toString()
            } else {
                "null"
            }
            els.addElement(elObj)

            if (isFindSimilar) {
                isDone = false
                browser.currentTab.value?.webView?.js(Injector.ELEMENT_FINDER_SCRIPT)
                browser.currentTab.value?.webView?.js("""
                    var currentEl = document.querySelectorAll("$selector")[${elIndex}];
                    if (currentEl === undefined) {
                        currentEl = elementFinder.getElementByXpath('${xpath}');
                    }
                    currentEl.style.boxShadow = "0 0 0 2px red";
                    var tree = window.elementFinder.findTree(currentEl);
                    return window.elementFinder.findSimilar(currentEl, tree);
                """.trimIndent()) {
                    try {
                        val jsonArray = JSONArray(it)
                        for (j in 0 until jsonArray.length()) {
                            val json = jsonArray.getJSONObject(j)
                            els.addElement(json)
                        }
                        isDone = true
                    } catch (e: Exception) {
                        onError(Exception("Unable to find similar elements, is the UI changed? some site load different content on different screen sizes."))
                        isDone = true
                    }
                }
            }

            while (!isDone) {
                if (isTerminated) break
                delay(5)
            }
        }
        return els
    } catch (e: Exception) {
        onError(e)
        return emptyList()
    }
}