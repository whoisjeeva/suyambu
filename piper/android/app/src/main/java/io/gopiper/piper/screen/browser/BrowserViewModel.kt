package io.gopiper.piper.screen.browser

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import me.gumify.hiper.util.WeeDB
import javax.inject.Inject

@HiltViewModel
class BrowserViewModel @Inject constructor(private val wee: WeeDB): ViewModel() {
    var isRecordHintShowed: Boolean
        get() = wee.getBoolean("isRecordHintShowed")
        set(value) = wee.put("isRecordHintShowed", value)
}