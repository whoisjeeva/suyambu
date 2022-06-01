package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeControlsRepeatExt(statement: JSONObject, onError: (e: Exception) -> Unit) {
    try {
        Log.d("hello", "execute_controls_repeat_ext")
        loopFlow = null
        val pointer = statement.getJSONObject("pointer")
        val times = executeStatement(pointer["times"].toString())?.toString()?.toInt() ?: 0
        val loopBlock = pointer["loopBlock"].toString().trim()
        if (loopBlock != "") {
            for (i in 1..times) {
                execute(loopBlock)
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
        }
    } catch (e: Exception) {
        onError(e)
    }
}