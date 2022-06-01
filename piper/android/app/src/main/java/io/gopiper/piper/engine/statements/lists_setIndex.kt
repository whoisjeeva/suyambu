package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception
import java.util.ArrayList

suspend fun Piper.executeListsSetIndex(statement: JSONObject, onError: (e: Exception) -> Unit) {
    try {
        Log.d("hello", "execute_lists_setIndex")
        val pointer = statement.getJSONObject("pointer")
        val list = executeStatement(pointer["list"].toString()) as ArrayList<Any?>
        val index = executeStatement(pointer["index"].toString()).toString().toInt()
        val value = executeStatement(pointer["value"].toString())
        val mode = pointer["mode"].toString()

        when (pointer["where"].toString()) {
            "FROM_START" -> when (mode) {
                "SET" -> list[index - 1] = value
                "INSERT" -> list.add(index - 1, value)
            }
            "FROM_END" -> when (mode) {
                "SET" -> list[list.size - index] = value
                "INSERT" -> list.add(list.size - index, value)
            }
            "FIRST" -> when (mode) {
                "SET" -> list[0] = value
                "INSERT" -> list.add(0, value)
            }
            "LAST" -> when (mode) {
                "SET" -> list[list.lastIndex] = value
                "INSERT" -> {
                    list.add(list.size, value)
                }
            }
            "RANDOM" -> when (mode) {
                "SET" -> list[(0 until list.size).random()] = value
                "INSERT" -> list.add((0 until list.size).random(), value)
            }
        }
    } catch (e: Exception) {
        onError(e)
    }
}