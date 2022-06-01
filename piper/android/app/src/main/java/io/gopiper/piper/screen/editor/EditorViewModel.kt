package io.gopiper.piper.screen.editor

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.gopiper.piper.data.repo.PiperRepo
import io.gopiper.piper.engine.EditorHolder
import io.gopiper.piper.model.Pipe
import kotlinx.coroutines.launch
import me.gumify.hiper.util.WeeDB
import javax.inject.Inject

@HiltViewModel
class EditorViewModel @Inject constructor(
    val editorHolder: EditorHolder,
    private val wee: WeeDB
): ViewModel() {
    var openBrowser by mutableStateOf(false)
    var isHtmlElementSelection by mutableStateOf(false)

    init {
        editorHolder.wee = wee
        editorHolder.webView.loadUrl("file:///android_asset/editor/index.html")
        editorHolder.setListener(object : EditorHolder.Listener {
            override fun onOpenBrowser() {
                openBrowser = true
            }
        })
    }

    fun getLastHTMLElements(): String? {
        val els = wee.getString("HTMLElements")
        wee.put("HTMLElements", "")
        return els
    }
}