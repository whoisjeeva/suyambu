package io.gopiper.piper.cheese

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.gopiper.piper.cheese.model.Tab
import javax.inject.Inject

@HiltViewModel
class CheeseViewModel @Inject constructor(private val browser: Browser): ViewModel() {
    var currentTab by browser.currentTab
    var tabs = browser.tabs
    var webViewHash: String? = null
    var canGoBack by browser.canGoBack
    val searchEngine: String
        get() = browser.searchEngine

    fun setBrowserClient(browserClient: BrowserClient?) {
        browser.browserClientInterface = browserClient
    }

    fun setUiContextToBrowser(uiContext: Context) {
        browser.uiContext = uiContext
    }

    fun removeUiContextFromBrowser() {
        browser.uiContext = null
    }

    fun getBrowser() = browser

    fun newTab(url: String? = null) {
        browser.newTab(url)
    }

    fun switchTab(index: Int) {
        browser.switchTab(index)
    }

    fun removeTab(tab: Tab) {
        browser.removeTab(tab)
    }

    fun loadUrl(url: String) {
        currentTab?.loadUrl(url)
    }

    fun setSearchEngine(url: String) {
        browser.searchEngine = url
    }

    fun clearAllTabs() {
        browser.tabs.clear()
    }
}