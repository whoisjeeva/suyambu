package io.gopiper.piper.screen.setting.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingCategory(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.caption,
        color = MaterialTheme.colors.onPrimary.copy(0.5f),
        modifier = Modifier.padding(top = 20.dp, start = 20.dp, end = 20.dp)
    )
}