package io.gopiper.piper.engine.statements

import android.util.Log
import io.gopiper.piper.engine.Piper
import org.json.JSONObject
import java.lang.Exception

suspend fun Piper.executeLogicCompare(statement: JSONObject, onError: (e: Exception) -> Unit): Boolean {
    try {
        Log.d("hello", "execute_logic_compare")
        val pointer = statement.getJSONObject("pointer")
        val op = pointer.getString("op")
        val value1 = executeStatement(pointer["value1"].toString()) ?: "null"
        val value2 = executeStatement(pointer["value2"].toString()) ?: "null"

        return when (op) {
            "==" -> value1 == value2
            "!=" -> value1 != value2
            ">" -> {
                if (value1::class.java.toString() != value2::class.java.toString()) {
                    if (value1 is Int && value2 is Double) {
                        return value1 > value2
                    } else if (value1 is Double && value2 is Int) {
                        return value1 > value2
                    }
                    return value1.toString() > value2.toString()
                }
                when (value1) {
                    is Double -> value1 > value2 as Double
                    is Int -> value1 > value2 as Int
                    else -> value1.toString() > value2.toString()
                }
            }
            "<" -> {
                if (value1::class.java.toString() != value2::class.java.toString()) {
                    if (value1 is Int && value2 is Double) {
                        return value1 < value2
                    } else if (value1 is Double && value2 is Int) {
                        return value1 < value2
                    }
                    return value1.toString() < value2.toString()
                }
                when (value1) {
                    is Double -> value1 < value2 as Double
                    is Int -> value1 < value2 as Int
                    else -> value1.toString() < value2.toString()
                }
            }
            ">=" -> {
                if (value1::class.java.toString() != value2::class.java.toString()) {
                    if (value1 is Int && value2 is Double) {
                        return value1 >= value2
                    } else if (value1 is Double && value2 is Int) {
                        return value1 >= value2
                    }
                    return value1.toString() >= value2.toString()
                }
                when (value1) {
                    is Double -> value1 >= value2 as Double
                    is Int -> value1 >= value2 as Int
                    else -> value1.toString() >= value2.toString()
                }
            }
            "<=" -> {
                if (value1::class.java.toString() != value2::class.java.toString()) {
                    if (value1 is Int && value2 is Double) {
                        return value1 <= value2
                    } else if (value1 is Double && value2 is Int) {
                        return value1 <= value2
                    }
                    return value1.toString() <= value2.toString()
                }
                when (value1) {
                    is Double -> value1 <= value2 as Double
                    is Int -> value1 <= value2 as Int
                    else -> value1.toString() <= value2.toString()
                }
            }
            else -> false
        }
    } catch (e: Exception) {
        onError(e)
        return false
    }
}