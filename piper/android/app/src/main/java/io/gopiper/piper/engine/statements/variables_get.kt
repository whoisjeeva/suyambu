package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import io.gopiper.piper.engine.model.HTMLElement
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeVariablesGet(statement: JSONObject, onError: (e: Exception) -> Unit): Any? {
    try {
        val pointer = statement["pointer"].toString()


        if (VARIABLE_STACK.containsKey(pointer)) {
//            if (VARIABLE_STACK[pointer] is HTMLElement) {
//                val arr = ArrayList<Any?>()
//                arr.add(VARIABLE_STACK[pointer])
//                VARIABLE_STACK[pointer] = arr
//            }
            return VARIABLE_STACK[pointer]
        }
        return null
    } catch (e: Exception) {
        onError(e)
        return null
    }
}