package io.gopiper.piper.cheese.adblock

import android.webkit.WebResourceResponse
import io.pipend.open.hiper.Hiper
import java.io.ByteArrayInputStream
import java.util.regex.Pattern

object Adblocker {
    private val urlList = mutableListOf<String>()
    private val WHITESPACES = Pattern.compile("[ \t]+")
    private val hiper = Hiper.getInstance().async()

    val emptyResponse: WebResourceResponse by lazy {
        val empty = ByteArrayInputStream(byteArrayOf())
        WebResourceResponse("text/plain", "utf-8", empty)
    }

    init {
        hiper.get("https://raw.githubusercontent.com/AdAway/adaway.github.io/master/hosts.txt") { response ->
            if (response.isSuccessful && response.text != null) {
                val parseList = mutableListOf<String>()
                val lines = response.text!!.split("\n")
                for (line in lines) {
                    if (line.startsWith("#") || line.trim() == "") continue
                    val host = WHITESPACES.split(line)
                    if (host.size > 1 && (host[0] == "0.0.0.0" || host[0] == "127.0.0.1")) {
                        parseList.add(host[1])
                    }
                }

                urlList.clear()
                urlList.addAll(parseList)
            }
        }
    }

    fun shouldBlock(host: String): Boolean {
        return urlList.any { host.contains(it) }
    }
}