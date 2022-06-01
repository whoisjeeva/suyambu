package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import io.gopiper.piper.engine.model.Func
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeProceduresDefreturn(statement: JSONObject, onError: (e: Exception) -> Unit) {
    try {
        Log.d("hello", "execute_procedures_defreturn")
        val pointer = statement.getJSONObject("pointer")
        val name = pointer["name"].toString()
        val args = pointer.getJSONArray("args")
        val functionBlock = pointer["functionBlock"].toString()
        val ret = pointer["ret"].toString()

        for (i in 0 until args.length()) {
            VARIABLE_STACK[args[i].toString()] = null
        }

        FUNCTION_STACK.add(Func(
            name = name,
            functionBlock = functionBlock,
            ret = ret
        ))
    } catch (e: Exception) {
        onError(e)
    }
}