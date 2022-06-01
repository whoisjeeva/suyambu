package io.gopiper.piper.engine.statements

import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeText(statement: JSONObject, onError: (e: Exception) -> Unit): String {
    try {
        return statement["pointer"].toString()
            .replace("\\n", "\n")
            .replace("\\t", "\t")
            .replace("\\r", "\r")
    } catch (e: Exception) {
        onError(e)
        return ""
    }
}