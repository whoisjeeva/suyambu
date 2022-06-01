package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeControlsFor(statement: JSONObject, onError: (e: Exception) -> Unit) {
    try {
        Log.d("hello", "execute_controls_forEach")
        loopFlow = null
        val pointer = statement.getJSONObject("pointer")
        val start = executeStatement(pointer["from"].toString()).toString().toInt()
        val end = executeStatement(pointer["to"].toString()).toString().toInt()
        val step = executeStatement(pointer["step"].toString()).toString().toInt()

        val variableName = pointer["variable"].toString()
        val loopBlock = pointer["loopBlock"].toString().trim()

        for (i in start..end step step) {
            VARIABLE_STACK[variableName] = i

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