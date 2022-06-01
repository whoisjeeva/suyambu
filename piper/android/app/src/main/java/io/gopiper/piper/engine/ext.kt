package io.gopiper.piper.engine

import android.util.Log
import android.view.KeyEvent
import io.gopiper.piper.cheese.Browser
import io.gopiper.piper.cheese.js
import io.gopiper.piper.engine.model.Attr
import io.gopiper.piper.engine.model.HTMLElement
import kotlinx.coroutines.delay
import me.gumify.hiper.util.onUiThread


fun List<Attr>.getXpath(): String {
    for (attr in this) {
        if (attr.key == "xpath") {
            return attr.value
        }
    }
    return "null"
}

suspend fun waitForAtLeastSingleElement(browser: Browser, selector: String): Boolean {
    var isFound = false
    var waitTime = 0

    while (!isFound) {
        if (waitTime > 5000) {
            break
        }
        browser.currentTab.value?.webView?.js("""
            return document.querySelector("$selector") !== null;
        """.trimIndent()) {
            isFound = it == "true"
        }
        delay(5)
        waitTime += 5
    }

    return isFound
}


suspend fun waitForElementLimited(browser: Browser, el: HTMLElement): Boolean {
    var isFound = false
    var waitTime = 0
    val xpath = el.attrs.getXpath()

    while (!isFound) {
        if (waitTime > 5000) {
            break
        }
        onUiThread {
            browser.currentTab.value?.webView?.evaluateJavascript("""
                (function() {
                    var el = document.querySelectorAll("${el.selector}")[${el.elIndex}];
                    if (el === undefined) {
                        el = document.evaluate('${xpath}', document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;
                    }
                    if (el === undefined || el === null) {
                        return false;
                    }
                    return true;
                })();
            """) {
                isFound = it == "true"
            }
        }
        delay(5)
        waitTime += 5
    }
    return isFound
}


fun pressKey(browser: Browser, key: Int, onError: (e: Exception) -> Unit) {
    onUiThread {
        try {
            browser.currentTab.value?.webView?.apply {
                dispatchKeyEvent(KeyEvent(0, 0, KeyEvent.ACTION_DOWN, key, 0))
                dispatchKeyEvent(KeyEvent(0, 0, KeyEvent.ACTION_UP, key, 0))
            }
        } catch (e: Exception) {
            onError(e)
        }
    }
}


suspend fun waitForAllElementsUnlimited(browser: Browser, els: List<HTMLElement>): Boolean {
    var isFound = false
    while (true) {
        var isFoundSingle: Boolean? = null
        for (el in els) {
            isFoundSingle = null
            val xpath = el.attrs.getXpath()
            browser.currentTab.value?.webView?.js("""
                var el = document.querySelectorAll("${el.selector}")[${el.elIndex}];
                if (el === undefined) {
                    el = document.evaluate('${xpath}', document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;
                }
                if (el === undefined || el === null) {
                    return false;
                }
                return true;
            """.trimIndent()) {
                    isFoundSingle = it == "true"
            }
            while (isFoundSingle == null) delay(5)
            if (isFoundSingle == true) break
        }
        if (isFoundSingle == true) {
            isFound = true
            break
        }
        delay(5)
    }
    return isFound
}


suspend fun waitForElementUnlimited(browser: Browser, el: HTMLElement): Boolean {
    var isFound = false
    val xpath = el.attrs.getXpath()
    while (true) {
        onUiThread {
            browser.currentTab.value?.webView?.evaluateJavascript("""
                (function() {
                    var el = document.querySelectorAll("${el.selector}")[${el.elIndex}];
                    if (el === undefined) {
                        el = document.evaluate('${xpath}', document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;
                    }
                    if (el === undefined || el === null) {
                        return false;
                    }
                    return true;
                })();
            """) {
                isFound = it == "true"
            }
        }
        if (isFound) break
        delay(100)
    }
    return isFound
}
