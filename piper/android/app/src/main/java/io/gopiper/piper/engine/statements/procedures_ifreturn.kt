package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeProceduresIfreturn(statement: JSONObject, onError: (e: Exception) -> Unit) {
    try {
        Log.d("hello", "execute_procedures_ifreturn")
        val pointer = statement.getJSONObject("pointer")
        val condition = executeStatement(pointer["condition"].toString()) as? Boolean ?: false
        val value = executeStatement(pointer["value"].toString())
        if (condition) {
            functionReturnValue = value
            isFunctionReturn = true
        }
    } catch (e: Exception) {
        onError(e)
    }
}