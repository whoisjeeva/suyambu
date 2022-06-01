package io.gopiper.piper.screen.setting.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingItem(content: @Composable () -> Unit) {
    Box(Modifier.padding(horizontal = 18.dp).fillMaxWidth()) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colors.primary,
            modifier = Modifier.fillMaxWidth(),
            content = content
        )
    }
}