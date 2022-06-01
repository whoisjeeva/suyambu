package io.gopiper.piper.data.util

import androidx.room.TypeConverter
import org.json.JSONArray

class Converters {
    @TypeConverter
    fun fromString(value: String): List<String> {
        val json = JSONArray(value)
        val array = ArrayList<String>()
        for (i in 0 until json.length()) {
            array.add(json.getString(i))
        }
        return array
    }

    @TypeConverter
    fun fromArrayList(list: List<String>): String {
        val json = JSONArray()
        for (item in list) json.put(item)
        return json.toString()
    }
}