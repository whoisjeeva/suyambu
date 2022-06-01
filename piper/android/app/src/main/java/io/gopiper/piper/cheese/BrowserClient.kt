package io.gopiper.piper.cheese

import android.graphics.Bitmap
import android.os.Message
import android.webkit.WebResourceRequest
import android.webkit.WebView

interface BrowserClient {
    fun onReceivedUrl(tabId: String, view: WebView?, url: String?) {}
    fun onPageFinished(tabId: String, view: WebView?, url: String?) {}
    fun onPageStarted(tabId: String, view: WebView?, url: String?) {}
    fun onLoadResource(tabId: String, view: WebView?, url: String?) {}
    fun onNewTab(view: WebView?, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message?, url: String?) {}
    fun onReceivedIcon(tabId: String, view: WebView?, icon: Bitmap?) {}
    fun onReceivedTitle(tabId: String, view: WebView?, title: String?) {}
    fun shouldInterceptRequest(tabId: String, view: WebView?, request: WebResourceRequest) {}
    fun onProgressChanged(tabId: String, view: WebView?, newProgress: Int) {}
    fun onDownloadRequest(tabId: String, url: String?, userAgent: String?, contentDisposition: String?, mimetype: String?, contentLength: Long) {}
}