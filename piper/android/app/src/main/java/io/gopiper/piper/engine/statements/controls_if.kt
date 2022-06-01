package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeControlsIf(statement: JSONObject, onError: (e: Exception) -> Unit) {
    try {
        Log.d("hello", "execute_controls_if")
        val pointer = statement.getJSONArray("pointer")
        for (i in 0 until pointer.length()) {
            val block = pointer.getJSONObject(i)
            if (block.has("else")) {
                execute(block.getString("else"))
            } else {
                val cond = executeStatement(block.get("cond").toString())
                if (cond is Boolean) {
                    if (cond) {
                        val doBlock = block.get("do").toString().trim()
                        if (doBlock != "") execute(doBlock)
                        break
                    }
                }
            }
        }
    } catch (e: Exception) {
        onError(e)
    }
}