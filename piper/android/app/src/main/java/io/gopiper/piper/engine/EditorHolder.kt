package io.gopiper.piper.engine

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
import android.view.inputmethod.InputMethodManager
import android.webkit.*
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat.getSystemService
import io.gopiper.piper.PromptActivity
import io.gopiper.piper.R
import io.gopiper.piper.util.promptDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.gumify.hiper.util.WeeDB
import java.util.*
import javax.inject.Inject


class EditorHolder(private val context: Context) {
    private var listener: Listener? = null
    private var uiContext: Context? = null
    var wee: WeeDB? = null

    @SuppressLint("SetJavaScriptEnabled")
    val webView = WebView(context).apply {
        settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            databaseEnabled = true
//            setLayerType(View.LAYER_TYPE_HARDWARE, null)
            cacheMode = WebSettings.LOAD_NO_CACHE
            useWideViewPort = true
            loadWithOverviewMode = true
        }
        webChromeClient = object : WebChromeClient() {
            override fun onJsPrompt(
                view: WebView?,
                url: String?,
                message: String?,
                defaultValue: String?,
                result: JsPromptResult?
            ): Boolean {
                if (wee == null) {
                    uiContext?.promptDialog(
                        message = message.toString(),
                        defaultValue = defaultValue.toString(),
                        onConfirm = {
                            result?.confirm(it)
                        },
                        onCancel = {
                            result?.cancel()
                        }
                    )
                } else {
                    val key = UUID.randomUUID().toString()
                    val intent = Intent(context, PromptActivity::class.java)
                    intent.putExtra("message", message)
                    intent.putExtra("defaultValue", defaultValue)
                    intent.putExtra("key", key)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(intent)

                    CoroutineScope(Dispatchers.IO).launch {
                        var v: String? = null
                        while (v == null) {
                            wee?.get(key)?.let {
                                v = it
                                if (v == "") {
                                    result?.cancel()
                                } else {
                                    result?.confirm(v)
                                }
                            }
                            delay(100)
                        }
                    }
                }
                return true
            }
        }
        webViewClient = WebViewClient()
        addJavascriptInterface(
            NativeInterface(
                context = context,
                onOpenBrowser = { listener?.onOpenBrowser() }
            ),
            "Native"
        )
    }


    fun setListener(listener: Listener) {
        this.listener = listener
    }

    fun setUiContext(uiContext: Context) {
        this.uiContext = uiContext
    }

    fun destroy() {
        uiContext = null
    }


    interface Listener {
        fun onOpenBrowser()
    }
}