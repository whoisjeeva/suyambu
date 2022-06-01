package io.gopiper.piper.cheese.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun RowScope.TitleBar(
    text: String,
    onClick: () -> Unit = {},
    contentColor: Color
) {
    Surface(
        modifier = Modifier.weight(1f)
            .fillMaxHeight().padding(vertical = 10.dp),
        color = Color.White.copy(0.1f),
        shape = RoundedCornerShape(40.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable(onClick = onClick)
        ) {
            Text(
                text = text,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth().padding(horizontal = 10.dp),
                color = contentColor,
                textAlign = TextAlign.Center
            )
        }
    }
}