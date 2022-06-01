package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeDownloadFromUrl(statement: JSONObject, onError: (e: Exception) -> Unit) {
    try {
        Log.d("hello", "execute_download_from_url")
        val pointer = statement["pointer"].toString()
        val url = executeStatement(pointer).toString()
        downloadFromUrl(url)
    } catch (e: Exception) {
        onError(e)
    }
}