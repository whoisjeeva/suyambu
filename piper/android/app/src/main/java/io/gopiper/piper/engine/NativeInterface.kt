package io.gopiper.piper.engine

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface
import me.gumify.hiper.util.WeeDB
import me.gumify.hiper.util.onUiThread
import me.gumify.hiper.util.toast
import org.json.JSONArray

class NativeInterface(
    private val context: Context,
    private val onOpenBrowser: (() -> Unit)? = null
) {
    private val wee = WeeDB(context.applicationContext)

    @JavascriptInterface
    fun showToast(s: String) {
        context.toast(s)
    }

    @JavascriptInterface
    fun openBrowser() {
        onOpenBrowser?.invoke()
    }

    @JavascriptInterface
    fun showAlertWithElements(s: String) {
        try {
            alert(s)
        } catch (e: Exception) {
            Log.d("hello3", e.toString())
        }
    }

    @JavascriptInterface
    fun confirmElements(s: String) {
        wee.put("HTMLElements", s)
    }


    fun alert(s: String, onDismiss: (() -> Unit)? = null) {
        val elements = JSONArray(s)
        var tagNames = ""
        var attrString = "\n\n"
        for (i in 0 until elements.length()) {
            val el = elements.getJSONObject(i)
            val tag = el.getString("selector").split(" ").last().split(":").first()
            tagNames += tag
            tagNames += ", "
            val attrs = el.getJSONArray("attrs")
            attrString += "Attributes for '$tag':\n"
            if (attrs.length() == 0) {
                attrString += "-"
            } else {
                for (j in 0 until attrs.length()) {
                    val attr = attrs.getJSONObject(j)
                    attrString += "${attr.getString("name")}=${attr.getString("value")}"
                    attrString += ", "
                }
                attrString = attrString.dropLast(2)
            }
            attrString += "\n\n"

        }

        tagNames = tagNames.dropLast(2)
        attrString = attrString.dropLast(2)


        onUiThread {
            AlertDialog.Builder(context)
                .setTitle("Element Selector")
                .setMessage("Elements with tag names '${tagNames}' selected\n\nTotal of ${elements.length()} elements.$attrString")
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                    onDismiss?.invoke()
                }
                .show()
        }
    }
}