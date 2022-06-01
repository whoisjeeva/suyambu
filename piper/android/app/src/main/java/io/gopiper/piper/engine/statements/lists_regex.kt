package io.gopiper.piper.engine.statements

import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeListsRegex(statement: JSONObject, onError: (e: Exception) -> Unit): ArrayList<String>? {
    try {
        val pointer = statement.getJSONObject("pointer")
        val text = executeStatement(pointer["text"].toString()).toString()
        var r = executeStatement(pointer["regex"].toString()).toString()
        if (!r.endsWith(")")) {
            r = "$r)"
        }
        if (!r.startsWith("(")) {
            r = "($r"
        }
        val regex = Regex(r)
        val matches = regex.findAll(text)
        val foundList = ArrayList<String>()
        for (m in matches) {
            foundList.add(m.groupValues[1])
        }
        return foundList
    } catch (e: Exception) {
        onError(e)
        return null
    }
}