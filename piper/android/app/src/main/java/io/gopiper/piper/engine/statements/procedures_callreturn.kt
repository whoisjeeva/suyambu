package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeProceduresCallreturn(statement: JSONObject, onError: (e: Exception) -> Unit): Any? {
    Log.d("hello", "execute_procedures_callreturn")
    return executeProceduresCallnoreturn(statement, onError)
}