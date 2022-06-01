package io.gopiper.piper.cheese

import android.graphics.Bitmap
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import io.gopiper.piper.cheese.adblock.Adblocker
import io.gopiper.piper.cheese.web.BlankPage

class WebClient(
    private val browserClient: BrowserClient,
    private val tabId: String,
): WebViewClient() {

    private var lastUrl: String? = null

    override fun onLoadResource(view: WebView?, url: String?) {
        super.onLoadResource(view, url)

        if (lastUrl == null || !view?.url.equals(lastUrl)) {
            if (view?.url != null) {
                lastUrl = view.url
                browserClient.onReceivedUrl(tabId = tabId, view = view, url = prettyUrl(view.url))
            }
        }

        browserClient.onLoadResource(tabId = tabId, view = view, url = url)
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        browserClient.onReceivedUrl(tabId = tabId, view = view, url = prettyUrl(url))
        browserClient.onPageStarted(tabId = tabId, view = view, url = url)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        browserClient.onReceivedUrl(tabId = tabId, view = view, url = prettyUrl(url))
        browserClient.onPageFinished(tabId = tabId, view = view, url = url)
    }

    override fun shouldInterceptRequest(
        view: WebView?,
        request: WebResourceRequest?
    ): WebResourceResponse? {
        val host = request?.url?.host ?: return null
        if (Adblocker.shouldBlock(host)) {
            return Adblocker.emptyResponse
        }
        val url = request.url.toString()
        if (url.startsWith("browser://")) {
            if (url == "browser://blank") {
                return BlankPage.response
            }
        }
        browserClient.shouldInterceptRequest(tabId, view, request)
        return super.shouldInterceptRequest(view, request)
    }
}