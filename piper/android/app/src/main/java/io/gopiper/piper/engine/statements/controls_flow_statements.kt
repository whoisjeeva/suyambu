package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeControlsFlowStatement(statement: JSONObject, onError: (e: Exception) -> Unit): Any? {
    return try {
        Log.d("hello", "execute_controls_flow_statements")
        loopFlow = statement.getJSONObject("pointer")["flow"].toString()
        loopFlow
    } catch (e: Exception) {
        onError(e)
    }
}