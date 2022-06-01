package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeProceduresCallnoreturn(statement: JSONObject, onError: (e: Exception) -> Unit): Any? {
    try {
        Log.d("hello", "execute_procedures_callnoreturn")
        val pointer = statement.getJSONObject("pointer")
        val name = pointer["name"].toString()
        val args = pointer.getJSONObject("args")

        currentFunctionName = name
        val f = findFunction(name)
        var toRet: Any? = null


        if (f != null) {
            val iter = args.keys()
            while(iter.hasNext()) {
                val key = iter.next()
                var keySlug = key
                val c = keySlug[0]
                if (!(c in 'a'..'z' || c in 'A'..'Z') && c != '_') {
                    keySlug.drop(1)
                    keySlug = "_$keySlug"
                }
                keySlug = keySlug.replace("[^A-Za-z0-9]".toRegex(), "_")
                VARIABLE_STACK[keySlug] = executeStatement(args[key].toString())
            }
            execute(f.functionBlock)

            if (f.ret != null) {
                toRet = executeStatement(f.ret.toString())
            }
        }

        if (functionReturnValue != null) {
            toRet = functionReturnValue
        }

        currentFunctionName = null
        isFunctionReturn = null

        return toRet
    } catch (e: Exception) {
        onError(e)
        return null
    }
}