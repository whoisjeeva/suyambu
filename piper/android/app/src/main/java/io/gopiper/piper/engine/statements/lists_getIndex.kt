package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeListsGetIndex(statement: JSONObject, onError: (e: Exception) -> Unit): Any? {
    try {
        Log.d("hello", "execute_lists_getIndex")
        val pointer = statement.getJSONObject("pointer")
        val list = executeStatement(pointer["list"].toString()) as ArrayList<Any?>
        val index = executeStatement(pointer["index"].toString()).toString().toInt()
        val mode = pointer["mode"].toString()

        return when (pointer["where"].toString()) {
            "FROM_START" -> when (mode) {
                "GET" -> list[index - 1]
                "REMOVE", "GET_REMOVE" -> list.removeAt(index - 1)
                else -> null
            }
            "FROM_END" -> when (mode) {
                "GET" -> list[list.size - index]
                "REMOVE", "GET_REMOVE" -> list.removeAt(list.size - index)
                else -> null
            }
            "FIRST" -> when (mode) {
                "GET" -> list[0]
                "REMOVE", "GET_REMOVE" -> list.removeAt(0)
                else -> null
            }
            "LAST" -> when (mode) {
                "GET" -> list.last()
                "REMOVE", "GET_REMOVE" -> list.removeAt(list.lastIndex)
                else -> null
            }
            "RANDOM" -> when (mode) {
                "GET" -> list.random()
                "REMOVE", "GET_REMOVE" -> list.removeAt((0 until list.size).random())
                else -> null
            }
            else -> null
        }
    } catch (e: Exception) {
        onError(e)
        return null
    }
}