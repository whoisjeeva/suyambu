package io.gopiper.piper.engine.statements

import android.util.Log
import kotlinx.coroutines.delay
import io.gopiper.piper.cheese.js
import io.gopiper.piper.engine.Injector
import io.gopiper.piper.engine.Piper
import io.gopiper.piper.engine.getXpath
import me.gumify.hiper.util.onUiThread
import org.json.JSONObject

suspend fun Piper.executeScroll(statement: JSONObject, onError: (e: java.lang.Exception) -> Unit) {
    try {
        Log.d("hello", "execute_scroll")
        when (statement.getString("pointer")) {
            "UP" -> {
                onUiThread {
//                    if (lastClickedElement !== null) {
//                        browser.currentTab.value?.webView?.js(Injector.ELEMENT_FINDER_SCRIPT)
//                        val xpath = lastClickedElement!!.attrs.getXpath()
//                        browser.currentTab.value?.webView?.js("""
//                            function hasScroll(el, direction) {
//                                direction = (direction === 'vertical') ? 'scrollTop' : 'scrollLeft';
//                                var result = !! el[direction];
//
//                                if (!result) {
//                                    el[direction] = 1;
//                                    result = !!el[direction];
//                                    el[direction] = 0;
//                                }
//                                return result;
//                            }
//                            var el = document.querySelectorAll("${lastClickedElement!!.selector}")[${lastClickedElement!!.elIndex}];
//                            if (!el) {
//                                el = elementFinder.getElementByXpath('${xpath}');
//                            }
//                            if (el) {
//                                while (true) {
//                                    if (hasScroll(el, "vertical") || el.tagName === "BODY") {
//                                        break;
//                                    }
//                                    el = el.parentElement;
//                                }
//                            } else {
//                                window.scrollTo({ top: 0, behavior: 'smooth' });
//                            }
//                            if (el.tagName === "BODY" || !el) {
//                                window.scrollTo({ top: 0, behavior: 'smooth' });
//                            } else {
//                                el.scroll({ top: 0, behavior: 'smooth' });
//                            }
//                        """.trimIndent())
//                    } else {
//                        browser.currentTab.value?.webView?.js("window.scrollTo({ top: 0, behavior: 'smooth' });")
//                    }
                    browser.currentTab.value?.webView?.js("window.scrollTo({ top: 0, behavior: 'smooth' });")
                }
                delay(1000)
            }
            "DOWN" -> {
                onUiThread {
//                    if (lastClickedElement !== null) {
//                        browser.currentTab.value?.webView?.js(Injector.ELEMENT_FINDER_SCRIPT)
//                        val xpath = lastClickedElement!!.attrs.getXpath()
//                        Log.d("hello3", xpath)
//                        browser.currentTab.value?.webView?.js("""
//                            function hasScroll(el, direction) {
//                                direction = (direction === 'vertical') ? 'scrollTop' : 'scrollLeft';
//                                var result = !! el[direction];
//
//                                if (!result) {
//                                    el[direction] = 1;
//                                    result = !!el[direction];
//                                    el[direction] = 0;
//                                }
//                                return result;
//                            }
//                            var el = document.querySelectorAll("${lastClickedElement!!.selector}")[${lastClickedElement!!.elIndex}];
//                            if (!el) {
//                                el = elementFinder.getElementByXpath('${xpath}');
//                            }
//                            el.style.boxShadow = "0 0 0 2px red";
//                            if (el) {
//                                while (true) {
//                                    if (hasScroll(el, "vertical") || el.tagName === "BODY") {
//                                        break;
//                                    }
//                                    el = el.parentElement;
//                                }
//                            } else {
//                                window.scrollTo({ top: document.body.scrollHeight, behavior: 'smooth' });
//                            }
//                            if (el.tagName === "BODY" || !el) {
//                                window.scrollTo({ top: document.body.scrollHeight, behavior: 'smooth' });
//                            } else {
//                                el.scroll({ top: el.scrollHeight, behavior: 'smooth' });
//                            }
//                        """.trimIndent())
//                    } else {
//                        browser.currentTab.value?.webView?.js("window.scrollTo({ top: document.body.scrollHeight, behavior: 'smooth' });")
//                    }
                    browser.currentTab.value?.webView?.js("window.scrollTo({ top: document.body.scrollHeight, behavior: 'smooth' });")
                }
                delay(1000)
            }
        }
    } catch (e: Exception) {
        onError(e)
    }
}