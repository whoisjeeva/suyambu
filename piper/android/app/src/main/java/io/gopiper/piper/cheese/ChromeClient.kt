package io.gopiper.piper.cheese

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.os.Message
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView


class ChromeClient(
    private val browserClient: BrowserClient,
    private val tabId: String,
    private val getUiContext: () -> Context?,
    private val isHeadless: Boolean
): WebChromeClient() {
    override fun onCreateWindow(
        view: WebView?,
        isDialog: Boolean,
        isUserGesture: Boolean,
        resultMsg: Message?
    ): Boolean {
        val result = view!!.hitTestResult
        val data = result.extra

        browserClient.onNewTab(
            view = view,
            isDialog = isDialog,
            isUserGesture = isUserGesture,
            resultMsg = resultMsg,
            url = data
        )

        return false
    }

    override fun onReceivedIcon(view: WebView?, icon: Bitmap?) {
        super.onReceivedIcon(view, icon)
        browserClient.onReceivedIcon(tabId = tabId, view = view, icon = icon)
    }

    override fun onReceivedTitle(view: WebView?, title: String?) {
        super.onReceivedTitle(view, title)
        browserClient.onReceivedTitle(tabId, view, title)
    }

    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        browserClient.onProgressChanged(tabId, view, newProgress)
    }

    override fun onJsAlert(
        view: WebView?,
        url: String?,
        message: String?,
        result: JsResult?
    ): Boolean {
        if (!isHeadless) {
            val builder = AlertDialog.Builder(getUiContext())
            builder.setTitle("Alert")
                .setMessage(message)
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
            result?.cancel()
        }
        return true
    }
}