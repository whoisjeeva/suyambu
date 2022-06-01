package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeControlsWhileUntil(statement: JSONObject, onError: (e: Exception) -> Unit) {
    try {
        Log.d("hello", "execute_controls_whileUntil")
        loopFlow = null
        val pointer = statement.getJSONObject("pointer")
        val loopBlock = pointer["loopBlock"].toString()
        val mode = pointer["mode"].toString()


        while (true) {
            val cond = try {
                executeStatement(pointer["cond"].toString()) as? Boolean ?: false
            } catch (e: Exception) {
                false
            }
            if (mode == "UNTIL") {
                if (cond) break
            } else {
                if (!cond) break
            }

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
    } catch (e: Exception) {
        onError(e)
    }
}