package io.gopiper.piper.engine.statements

import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeConvertToText(statement: JSONObject, onError: (e: Exception) -> Unit): String? {
    return try {
        executeStatement(statement["pointer"].toString()).toString()
    } catch (e: Exception) {
        onError(e)
        null
    }
}