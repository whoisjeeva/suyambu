package io.gopiper.piper.engine

import android.webkit.WebView
import io.gopiper.piper.cheese.js

object Injector {
    var ELEMENT_FINDER_SCRIPT = ""
    var IS_CAPTURE = false
    var IS_RUN = false
    var IS_DEBUG = false
    var IS_HTML_SELECTION = false
    var IS_EDITOR_START = true

    fun preventNavigation(webView: WebView?) {
        webView?.js("""
            document.addEventListener('click', function (e) {
                e.stopPropagation();
            }, true);
        """.trimIndent())
    }
}