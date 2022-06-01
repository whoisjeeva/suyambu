package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import io.gopiper.piper.engine.model.HTMLElement
import io.gopiper.piper.engine.waitForElementUnlimited
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeWaitForElements(statement: JSONObject, onError: (e: Exception) -> Unit) {
    try {
        Log.d("hello", "execute_wait_for_elements")
        val elsObj = executeStatement(statement["pointer"].toString())
        val elements = if (elsObj is List<*>) {
            elsObj as List<HTMLElement>
        } else {
            listOf(elsObj as HTMLElement)
        }
        for (el in elements) {
            waitForElementUnlimited(browser, el)
        }
    } catch (e: Exception) {
        onError(e)
    }
}