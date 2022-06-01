package io.gopiper.piper.cheese

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.webkit.WebView
import io.gopiper.piper.cheese.model.Tab
import me.gumify.hiper.util.onUiThread

fun List<Tab>.findTab(tabId: String): Tab? {
    return find { it.tabId == tabId }
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}


fun prettyUrl(url: String?): String? {
    return if (url != null) {
        if (url.endsWith('/')) url.trimEnd('/') else url
    } else {
        null
    }
}



fun WebView.js(s: String, callback: ((String) -> Unit)? = null) {
    onUiThread {
        if (callback == null) {
            this.evaluateJavascript("(function() {$s})();", null)
        } else {
            this.evaluateJavascript("(function() {$s})();") {
                if (it.startsWith("\"") && it.endsWith("\"")) {
                    callback(it.replace("\\n", "\n").dropLast(1).drop(1))
                } else {
                    callback(it.replace("\\n", "\n"))
                }
            }
        }
    }
}

