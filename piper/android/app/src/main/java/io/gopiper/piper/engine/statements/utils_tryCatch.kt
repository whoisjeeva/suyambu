package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeUtilsTryCatch(statement: JSONObject, onError: (e: Exception) -> Unit) {
    val tryBlock: String
    val catchBlock: String

    try {
        val pointer = statement.getJSONObject("pointer")
        tryBlock = pointer["try"].toString()
        catchBlock = pointer["catch"].toString()
    } catch (e: Exception) {
        onError(e)
        return
    }

    try {
        canShowErrorAlert = false
        execute(tryBlock)
        canShowErrorAlert = true
        if (isTerminated) {
            isTerminated = false
            throw Exception()
        }
    } catch (e: Exception) {
        try {
            execute(catchBlock)
        } catch (e: Exception) {
            onError(e)
        }
    }
}