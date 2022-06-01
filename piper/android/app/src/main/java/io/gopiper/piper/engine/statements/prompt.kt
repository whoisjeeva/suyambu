package io.gopiper.piper.engine.statements

import android.content.Intent
import io.gopiper.piper.PromptActivity
import io.gopiper.piper.engine.Piper
import io.gopiper.piper.util.promptDialog
import kotlinx.coroutines.delay
import me.gumify.hiper.util.onUiThread
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executePrompt(statement: JSONObject, onError: (e: Exception) -> Unit): String? {
    try {
        val pointer = statement.getJSONObject("pointer")
        val defaultValue = executeStatement(pointer["value"].toString()).toString()
        val message = executeStatement(pointer["message"].toString()).toString()
        var result: String? = null

//        if (!isHeadless) {
//            onUiThread {
//                context.promptDialog(
//                    defaultValue = defaultValue,
//                    message = message,
//                    onConfirm = { result = it },
//                    onCancel = { result = "" }
//                )
//            }
//            while (result == null) delay(100)
//        } else {
//
//        }
        wee.remove("prompt_$scriptId")
        val intent = Intent(context, PromptActivity::class.java)
        intent.putExtra("message", message)
        intent.putExtra("defaultValue", defaultValue)
        intent.putExtra("key", "prompt_$scriptId")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
        while (result == null) {
            wee.get("prompt_$scriptId")?.let { result = it }
            delay(100)
        }
        return result
    } catch (e: Exception) {
        onError(e)
        return null
    }
}