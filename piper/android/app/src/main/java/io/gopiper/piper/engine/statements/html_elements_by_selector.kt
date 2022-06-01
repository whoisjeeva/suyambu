package io.gopiper.piper.engine.statements

import android.util.Log
import kotlinx.coroutines.delay
import io.gopiper.piper.cheese.js
import io.gopiper.piper.engine.Piper
import io.gopiper.piper.engine.model.HTMLElement
import io.gopiper.piper.engine.waitForAtLeastSingleElement
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeHtmlElementsBySelector(statement: JSONObject, onError: (e: Exception) -> Unit): List<HTMLElement> {
    try {
        Log.d("hello", "execute_html_elements_by_selector")
        val selector = statement.getString("pointer")
        if (selector.contains('"')) {
            onError(Exception("Double quotes not allowed in selector"))
            return listOf()
        }
        val isFound = waitForAtLeastSingleElement(browser, selector)
        if (!isFound) {
            onError(Exception("HTMLElement not found"))
            return listOf()
        }
        val els = ArrayList<HTMLElement>()
        var isDone = false
        browser.currentTab.value?.webView?.js("""
            var out = [];
            var els = document.querySelectorAll("$selector");
            for (var i = 0; i < els.length; i++) {
                out.push({ selector: "body $selector", elIndex: i, attrs: [] });
            }
            return out;
        """.trimIndent()) {
            try {
                val json = JSONArray(it)
                for (i in 0 until json.length()) {
                    val el = json.getJSONObject(i)
                    els.add(HTMLElement(selector = el.getString("selector"), elIndex = el.getInt("elIndex"), attrs = emptyList()))
                }
            } catch (e: Exception) {
                onError(e)
            }
            isDone = true
        }

        while (!isDone) {
            if (isTerminated) break
            delay(5)
        }
        return els
    } catch (e: Exception) {
        onError(e)
        return emptyList()
    }
}