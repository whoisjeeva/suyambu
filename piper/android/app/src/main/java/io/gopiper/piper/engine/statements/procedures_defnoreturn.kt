package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import io.gopiper.piper.engine.model.Func
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeProceduresDefnoreturn(statement: JSONObject, onError: (e: Exception) -> Unit) {
    try {
        Log.d("hello", "execute_procedures_defnoreturn")
        val pointer = statement.getJSONObject("pointer")
        val name = pointer["name"].toString()
        val args = pointer.getJSONArray("args")
        val functionBlock = pointer["functionBlock"].toString()

        for (i in 0 until args.length()) {
            val arg = args.getString(i)
            VARIABLE_STACK[arg] = null
        }

        FUNCTION_STACK.add(Func(
            name = name,
            functionBlock = functionBlock
        ))
    } catch (e: Exception) {
        onError(e)
    }
}
