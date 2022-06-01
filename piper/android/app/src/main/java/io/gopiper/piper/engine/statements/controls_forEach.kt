package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeControlsForEach(statement: JSONObject, onError: (e: Exception) -> Unit) {
    try {
        Log.d("hello", "execute_controls_forEach")
        loopFlow = null
        val pointer = statement.getJSONObject("pointer")
        val list = executeStatement(pointer["list"].toString()) as? List<Any?> ?: listOf()
        val loopBlock = pointer["loopBlock"].toString().trim()
        val variableName = pointer["variable"].toString()

        for (item in list) {
            VARIABLE_STACK[variableName] = item

            if (loopBlock != "") execute(loopBlock)

            if (isTerminated) break
            if (loopFlow == "BREAK") {
                loopFlow = null
                break
            } else if (loopFlow == "CONTINUE") {
                loopFlow = null
                continue
            }

            if (isFunctionReturn != null) break
        }
    } catch (e: Exception) {
        onError(e)
    }
}