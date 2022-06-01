package io.gopiper.piper.cheese

import android.content.Context
import android.graphics.Bitmap
import android.os.Message
import android.webkit.WebResourceRequest
import android.webkit.WebView
import androidx.compose.runtime.*
import io.gopiper.piper.cheese.model.Tab
import java.util.*

class Browser(private val context: Context, val isHeadless: Boolean = false) {
    val tabs = mutableStateListOf<Tab>()
    var currentTab = mutableStateOf<Tab?>(null)
    var canGoBack = mutableStateOf(false)
    var searchEngine = "browser://blank"
    var browserClientInterface: BrowserClient? = null
    var uiContext: Context? = null

    fun findTab(tabId: String): Tab? {
        return tabs.find { it.tabId == tabId }
    }

    private fun findTabIndex(tabId: String): Int? {
        var index: Int? = null
        for (i in 0 until tabs.size) {
            if (tabs[i].tabId == tabId) {
                index = i
                break
            }
        }
        return index
    }

    private val browserClient = object : BrowserClient {
        override fun onReceivedUrl(tabId: String, view: WebView?, url: String?) {
//            val tab = findTab(tabId)
//            if (tab != null) {
//                if (tab.progress < 11 && !tab.isLoading) {
//                    tab.progress = 10
//                    tab.isLoading = true
//                }
//            }
            if (url != null) {
                tabs.findTab(tabId)?.url = url
                canGoBack.value = view?.canGoBack() ?: false
            }
            browserClientInterface?.onReceivedUrl(tabId, view, url)
        }

        override fun onPageStarted(tabId: String, view: WebView?, url: String?) {
            val tab = findTab(tabId)
            if (tab != null) {
                tab.progress = 1
                tab.isLoading = true
                findTab(tabId)?.icon = null
            }
            canGoBack.value = view?.canGoBack() ?: false
            browserClientInterface?.onPageStarted(tabId, view, url)
        }

        override fun onPageFinished(tabId: String, view: WebView?, url: String?) {
            val tab = findTab(tabId)
            if (tab != null) {
                tab.isLoading = false
                tab.progress = 0
            }
            canGoBack.value = view?.canGoBack() ?: false
            browserClientInterface?.onPageFinished(tabId, view, url)
        }

        override fun onLoadResource(tabId: String, view: WebView?, url: String?) {
            browserClientInterface?.onLoadResource(tabId, view, url)
        }

        override fun onNewTab(
            view: WebView?,
            isDialog: Boolean,
            isUserGesture: Boolean,
            resultMsg: Message?,
            url: String?
        ) {
            if (url != null) newTab(url)
            browserClientInterface?.onNewTab(view, isDialog, isUserGesture, resultMsg, url)
        }

        override fun onReceivedIcon(tabId: String, view: WebView?, icon: Bitmap?) {
            findTab(tabId)?.icon = icon
            browserClientInterface?.onReceivedIcon(tabId, view, icon)
        }

        override fun onReceivedTitle(tabId: String, view: WebView?, title: String?) {
            findTab(tabId)?.title = title
            browserClientInterface?.onReceivedTitle(tabId, view, title)
        }

        override fun shouldInterceptRequest(tabId: String, view: WebView?, request: WebResourceRequest) {
            browserClientInterface?.shouldInterceptRequest(tabId, view, request)
        }

        override fun onProgressChanged(tabId: String, view: WebView?, newProgress: Int) {
            findTab(tabId)?.progress = newProgress
            browserClientInterface?.onProgressChanged(tabId, view, newProgress)
        }
    }

    init {
        if (tabs.isEmpty()) {
            newTab("browser://blank")
        }
    }

    fun newTab(url: String?) {
        val tabId = UUID.randomUUID().toString()
        tabs.add(Tab(
            tabId = tabId,
            webView =  WebViewFactory(
                context = context,
                browserClient = browserClient,
                tabId = tabId,
                getUiContext = {
                    return@WebViewFactory uiContext
                },
                isHeadless = isHeadless
            ).get(),
            initialUrl = url ?: "browser://blank",
            getHomePage = {
                return@Tab searchEngine
            }
        ))
        currentTab.value = tabs.last()
        canGoBack.value = tabs.last().webView.canGoBack()
    }

    fun switchTab(index: Int) {
        currentTab.value = tabs[index]
        canGoBack.value = tabs[index].webView.canGoBack()
    }

    fun removeTab(tab: Tab) {
        val tabIndex = findTabIndex(tab.tabId) ?: return
        when {
            tabs.size <= 1 -> {
                newTab("browser://blank")
                tabs.removeAt(tabIndex)
            }
            tab.tabId == currentTab.value?.tabId -> {
                var toIndex = 0
                if (tabIndex - 1 >= 0) {
                    toIndex = tabIndex - 1
                } else if (tabIndex + 1 < tabs.size - 1) {
                    toIndex = tabIndex + 1
                }
                tabs.removeAt(tabIndex)
                switchTab(toIndex)
            }
            else -> {
                tabs.removeAt(tabIndex)
            }
        }
    }
}