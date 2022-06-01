package io.gopiper.piper.engine.statements

import android.content.Intent
import io.gopiper.piper.ListsChooserActivity
import io.gopiper.piper.PromptActivity
import io.gopiper.piper.engine.Piper
import kotlinx.coroutines.delay
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeListsChooser(statement: JSONObject, onError: (e: Exception) -> Unit): Int {
    try {
        val list = executeStatement(statement["pointer"].toString()) as List<Any>
        val jsonArray = JSONArray()
        list.forEach { jsonArray.put(it) }
        var result = -1

        wee.remove("chooser_$scriptId")
        val intent = Intent(context, ListsChooserActivity::class.java)
        intent.putExtra("jsonArray", jsonArray.toString())
        intent.putExtra("key", "chooser_$scriptId")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
        while (result == -1) {
            wee.getInt("chooser_$scriptId", defaultValue = -1).let { result = it }
            delay(100)
        }
        return result + 1
    } catch (e: Exception) {
        onError(e)
        return -1
    }
}