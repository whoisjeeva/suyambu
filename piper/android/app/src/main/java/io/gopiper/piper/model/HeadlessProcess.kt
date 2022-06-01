package io.gopiper.piper.model

import io.gopiper.piper.cheese.Browser
import io.gopiper.piper.engine.Piper

data class HeadlessProcess(
    val scriptId: String,
    val browser: Browser,
    val pipe: Pipe,
    val piper: Piper
)
