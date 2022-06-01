package io.gopiper.piper.engine.statements

import io.gopiper.piper.cheese.js
import io.gopiper.piper.engine.Piper
import io.gopiper.piper.util.setDesktopMode
import me.gumify.hiper.util.onUiThread
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeSetUserAgent(statement: JSONObject, onError: (e: Exception) -> Unit) {
    try {
        val pointer = statement["pointer"].toString()

        onUiThread {
            try {
//                val userAgent = browser.currentTab.value?.webView?.settings?.userAgentString
//                val desktopUA = userAgent?.replace("Android", "Windows NT")
//                    ?: "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36"
//                val mobileUA = "Mozilla/5.0 (Linux; Android 12) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Mobile Safari/537.36"
                if (pointer == "DESKTOP") {
//                    browser.currentTab.value?.webView?.settings?.userAgentString = desktopUA
                    browser.currentTab.value?.webView?.setDesktopMode(true)
                    browser.currentTab.value?.webView?.settings?.userAgentString = "Mozilla/5.0 (Piper) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36"
                } else if (pointer == "MOBILE") {
//                    browser.currentTab.value?.webView?.settings?.userAgentString = mobileUA
                    browser.currentTab.value?.webView?.setDesktopMode(false)
                    browser.currentTab.value?.webView?.settings?.userAgentString = "Mozilla/5.0 (Linux; Android 11; Pixel 5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.91 Mobile Safari/537.36"
                }
            } catch (e: Exception) {
                onError(e)
            }
        }
    } catch (e: Exception) {
        onError(e)
    }
}