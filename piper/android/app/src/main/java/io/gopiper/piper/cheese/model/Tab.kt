package io.gopiper.piper.cheese.model

import android.graphics.Bitmap
import android.webkit.WebView
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class Tab(
    val tabId: String,
    val webView: WebView,
    private var initialUrl: String,
    private var isUrlChanged: Boolean = true,
    private var getHomePage: () -> String
) {
    var url by mutableStateOf(initialUrl)
    var isLoading by mutableStateOf(false)
    var icon by mutableStateOf<Bitmap?>(null)
    var title by mutableStateOf<String?>(url)
    var progress by mutableStateOf(0)

    fun load() {
        if (isUrlChanged) {
            if (!url.contains("://")) {
                url = if (url.contains(".")) {
                    "http://$url"
                } else {
                    val homePage = getHomePage()
                    when {
                        homePage.contains("bing.com") -> {
                            "https://www.bing.com/search?q=$url"
                        }
                        homePage.contains("duckduckgo.com") -> {
                            "https://www.duckduckgo.com/?q=$url"
                        }
                        else -> {
                            "https://www.google.com/search?q=$url"
                        }
                    }
                }
            }
            webView.loadUrl(url)
            isUrlChanged = false
        }
    }

    fun loadUrl(url: String) {
        this.url = url
        isUrlChanged = true
        load()
    }

    val canGoBack: Boolean
        get() = webView.canGoBack()
    fun goBack() {
        if (webView.canGoBack()) webView.goBack()
    }

    val canGoForward: Boolean
        get() = webView.canGoForward()
    fun goForward() {
        if (webView.canGoForward()) webView.goForward()
    }

    fun stopLoading() {
        webView.stopLoading()
    }

    fun reload() {
        webView.reload()
    }
}
