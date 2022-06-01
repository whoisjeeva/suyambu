package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception
import kotlin.math.pow
import kotlin.math.sqrt

suspend fun Piper.executeMathOnList(statement: JSONObject, onError: (e: Exception) -> Unit): Any? {
    try {
        Log.d("hello", "execute_math_on_list")
        val pointer = statement.getJSONObject("pointer")
        val op = pointer["op"].toString()
        val values = executeStatement(pointer["list"].toString()) as List<Any?>

        val numberList = ArrayList<Number>()

        for (v in values) {
            when (v) {
                is Double -> numberList.add(v)
                is Int -> numberList.add(v)
                else -> return null
            }
        }

        val out = when (op) {
            "SUM" -> numberList.sumOf { it.toDouble() }
            "MIN" -> numberList.minOf { it.toDouble() }
            "MAX" -> numberList.maxOf { it.toDouble() }
            "AVERAGE" -> numberList.sumOf { it.toDouble() } / numberList.size
            "MEDIAN" -> {
                val sortedList = numberList.sortedBy { it.toDouble() }
                if (sortedList.size % 2 == 0) {
                    return (sortedList[sortedList.size / 2].toDouble() + sortedList[sortedList.size / 2 - 1].toDouble()) / 2
                } else {
                    return sortedList[sortedList.size / 2]
                }
            }
            "MODE" -> {
                val sortedList = numberList.sortedBy { it.toDouble() }
                var count = 1
                var mode = sortedList[0]
                for (i in 1 until sortedList.size) {
                    if (sortedList[i] == sortedList[i - 1]) {
                        count++
                    } else {
                        count = 1
                    }

                    if (count > 1) {
                        mode = sortedList[i]
                        break
                    }
                }
                return mode
            }
            "STD_DEV" -> {
                val mean = numberList.sumOf { it.toDouble() } / numberList.size
                val meanList = ArrayList<Double>()
                for (x in numberList) {
                    meanList.add((x.toDouble() - mean).pow(2))
                }
                return sqrt(meanList.sum() / numberList.size)
            }
            "RANDOM" -> numberList.random()
            else -> null
        } ?: return null

        return if (out.toString().endsWith(".0")) {
            out.toInt()
        } else {
            out.toDouble()
        }
    } catch (e: Exception) {
        onError(e)
        return null
    }
}