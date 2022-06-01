package io.gopiper.piper.util

import io.gopiper.piper.BuildConfig
import io.gopiper.piper.Config

object C {
    const val SCRIPT_RUNNER_NOTIFICATION_ID = 1
    const val PIPER_ENGINE_VERSION = 1
    fun getBuildNumber(): String {
        return "${if (Config.DEBUG_MODE) "dev" else "release"}${PIPER_ENGINE_VERSION}-${BuildConfig.VERSION_NAME}${BuildConfig.VERSION_CODE}"
    }

    const val SETTING_CLEAR_BROWSER_BEFORE_EX = "io.gopiper.piper.SETTING_CLEAR_BROWSER_BEFORE_EX"
    const val SETTING_DO_NOT_WAIT_FOR_LOADING = "io.gopiper.piper.SETTING_DO_NOT_WAIT_FOR_LOADING"
    const val SETTING_SKIP_ERROR = "io.gopiper.piper.SETTING_SKIP_ERROR"
    const val SETTING_SHOW_EDITOR_HANDLE = "io.gopiper.piper.SETTING_SHOW_EDITOR_HANDLE"
    const val THEME_LIGHT = 1
    const val THEME_DARK = 2
    const val THEME_SYSTEM = 3
}