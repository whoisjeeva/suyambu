package io.gopiper.piper.screen.setting

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.gopiper.piper.util.C
import kotlinx.coroutines.launch
import me.gumify.hiper.util.Tart
import me.gumify.hiper.util.WeeDB
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val wee: WeeDB,
    private val tart: Tart
): ViewModel() {
    var isClearBrowserBeforeExecution by mutableStateOf(false)
        private set
    var isDoNotWaitForLoading by mutableStateOf(false)
        private set
    var isSkipError by mutableStateOf(false)
        private set
    var isToolbarToggleHandleVisible by mutableStateOf(false)
        private set
    var theme = tart.getInt("theme")

    init {
        viewModelScope.launch {
            isClearBrowserBeforeExecution = wee.getBoolean(C.SETTING_CLEAR_BROWSER_BEFORE_EX)
            isDoNotWaitForLoading = wee.getBoolean(C.SETTING_DO_NOT_WAIT_FOR_LOADING)
            isSkipError = wee.getBoolean(C.SETTING_SKIP_ERROR)
            isToolbarToggleHandleVisible = wee.getBoolean(C.SETTING_SHOW_EDITOR_HANDLE)
        }
    }

    fun updateClearBrowserBeforeEx(value: Boolean) {
        viewModelScope.launch {
            wee.put(C.SETTING_CLEAR_BROWSER_BEFORE_EX, value)
        }
        isClearBrowserBeforeExecution = value
    }

    fun updateDoNotWaitForLoading(value: Boolean) {
        viewModelScope.launch {
            wee.put(C.SETTING_DO_NOT_WAIT_FOR_LOADING, value)
        }
        isDoNotWaitForLoading = value
    }

    fun updateSkipError(value: Boolean) {
        viewModelScope.launch {
            wee.put(C.SETTING_SKIP_ERROR, value)
        }
        isSkipError = value
    }

    fun updateTheme(theme: Int) {
        viewModelScope.launch {
            tart.put("theme", theme)
        }
    }

    fun updateToolbarToggleHandleVisible(value: Boolean) {
        viewModelScope.launch {
            wee.put(C.SETTING_SHOW_EDITOR_HANDLE, value)
        }
        isToolbarToggleHandleVisible = value
    }
}