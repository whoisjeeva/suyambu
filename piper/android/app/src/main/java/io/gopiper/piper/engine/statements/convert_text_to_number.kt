package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeConvertTextToNumber(statement: JSONObject, onError: (e: Exception) -> Unit): Any? {
    try {
        val text = executeStatement(statement["pointer"].toString()) as? String ?: return null
        val num = text.toDouble()

        if (num.toString().endsWith(".0")) {
            return num.toInt()
        }
        return num
    } catch (e: Exception) {
        onError(e)
        return null
    }
}