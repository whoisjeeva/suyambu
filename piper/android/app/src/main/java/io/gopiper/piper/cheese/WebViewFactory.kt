package io.gopiper.piper.cheese

import android.R.attr.mimeType
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.net.Uri
import android.os.Environment
import android.view.ViewGroup
import android.webkit.*


@SuppressLint("SetJavaScriptEnabled")
class WebViewFactory(
    private val context: Context,
    browserClient: BrowserClient,
    tabId: String,
    private val getUiContext: () -> Context?,
    private val isHeadless: Boolean
) {
    private val webView = WebView(context).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    init {
        webView.settings.apply {
            javaScriptEnabled = true
            setSupportMultipleWindows(true)
            allowFileAccess = true
            cacheMode = WebSettings.LOAD_NO_CACHE
            databaseEnabled = true
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = false
            useWideViewPort = true
            domStorageEnabled = true
//            javaScriptCanOpenWindowsAutomatically = true
            layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
        }
        webView.isFocusableInTouchMode = true
        webView.isFocusable = true
        webView.requestFocus()
        webView.webChromeClient = ChromeClient(
            browserClient = browserClient,
            tabId = tabId,
            getUiContext = getUiContext,
            isHeadless = isHeadless
        )
        webView.webViewClient = WebClient(browserClient, tabId = tabId)

        /* for some reason I am getting "java.lang.AbstractMethodError: abstract method" */
        webView.setDownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
            val request = DownloadManager.Request(Uri.parse(url))
            request.setMimeType(mimetype)
            val cookies = CookieManager.getInstance().getCookie(url)
            request.addRequestHeader("cookie", cookies)
            request.addRequestHeader("User-Agent", userAgent)
            request.setDescription("Downloading file...")
            request.setTitle(
                URLUtil.guessFileName(
                    url, contentDisposition,
                    mimetype
                )
            )
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(
                    url, contentDisposition, mimetype
                )
            )
            val dm = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager?
            dm!!.enqueue(request)
        }
    }

    fun get() = webView
}