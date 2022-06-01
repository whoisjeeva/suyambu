package io.gopiper.piper.engine.statements

import android.util.Log
import android.view.KeyEvent
import io.gopiper.piper.engine.Piper
import io.gopiper.piper.engine.pressKey
import org.json.JSONObject

suspend fun Piper.executePressKey(statement: JSONObject, onError: (e: Exception) -> Unit) {
    try {
        Log.d("hello", "execute_press_key")
        when (statement.getString("pointer")) {
            "ENTER" -> pressKey(this.browser, KeyEvent.KEYCODE_ENTER, onError)
            "SHIFT" -> pressKey(this.browser, KeyEvent.KEYCODE_SHIFT_LEFT, onError)
            "CTRL" -> pressKey(this.browser, KeyEvent.KEYCODE_CTRL_LEFT, onError)
            "CAPS_LOCK" -> pressKey(this.browser, KeyEvent.KEYCODE_CAPS_LOCK, onError)
            "TAB" -> pressKey(this.browser, KeyEvent.KEYCODE_TAB, onError)
            "ESC" -> pressKey(this.browser, KeyEvent.KEYCODE_ESCAPE, onError)
        }
    } catch (e: Exception) {
        onError(e)
    }
}