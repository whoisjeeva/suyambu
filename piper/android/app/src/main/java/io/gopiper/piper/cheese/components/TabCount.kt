package io.gopiper.piper.cheese.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TabCount(count: Int, color: Color = MaterialTheme.colors.onSurface) {
    Column(
        modifier = Modifier
            .border(
                border = BorderStroke(2.dp, color),
                shape = MaterialTheme.shapes.medium
            )
            .size(20.dp)
            .padding(bottom = 1.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (count < 10) {
            Text(text = "$count", color = color, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        } else {
            Text(text = "9+", color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}