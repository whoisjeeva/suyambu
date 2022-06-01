package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeListsGetSublist(statement: JSONObject, onError: (e: Exception) -> Unit): List<Any?>? {
    try {
        Log.d("hello", "execute_lists_getSublist")
        val pointer = statement.getJSONObject("pointer")
        val list = executeStatement(pointer["list"].toString()) as? List<Any?> ?: return null
        val where1 = pointer["where1"].toString()
        val where2 = pointer["where2"].toString()
        val at1 = executeStatement(pointer["at1"].toString()).toString().toInt()
        val at2 = executeStatement(pointer["at2"].toString()).toString().toInt()

        return when (where1) {
            "FROM_START" -> when (where2) {
                "FROM_START" -> list.subList(at1 - 1, at2).toMutableList()
                "FROM_END" -> list.subList(at1 - 1, list.size - (at2-1)).toMutableList()
                "LAST" -> list.subList(at1 - 1, list.size).toMutableList()
                else -> null
            }
            "FROM_END" -> when (where2) {
                "FROM_START" -> list.subList(list.size - at1, at2).toMutableList()
                "FROM_END" -> list.subList(list.size - at1, list.size - (at2-1)).toMutableList()
                "LAST" -> list.subList(list.size - at2, list.size).toMutableList()
                else -> null
            }
            "FIRST" -> when (where2) {
                "FROM_START" -> list.subList(0, at2).toMutableList()
                "FROM_END" -> list.subList(0, list.size - (at2-1)).toMutableList()
                "LAST" -> list.subList(0, list.size).toMutableList()
                else -> null
            }
            else -> null
        }
    } catch (e: Exception) {
        onError(e)
        return null
    }
}
