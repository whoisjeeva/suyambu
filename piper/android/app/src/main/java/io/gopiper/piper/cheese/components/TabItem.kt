package io.gopiper.piper.cheese.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Public
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import io.gopiper.piper.cheese.model.Tab

@Composable
fun TabItem(
    tab: Tab,
    isActive: Boolean = false,
    activeColor: Color = Color.Green,
    onTabClick: (Tab) -> Unit,
    onTabClose: (Tab) -> Unit,
    backgroundColor: Color = Color.LightGray
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(2.dp, if (isActive) activeColor else Color.Transparent)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(if (isActive) activeColor.copy(0.3f) else backgroundColor)
                .clickable { onTabClick(tab) }
        ) {
            if (tab.icon == null) {
                Icon(
                    imageVector = Icons.Default.Public,
                    contentDescription = "Favicon",
                    modifier = Modifier.padding(10.dp).size(40.dp)
                )
            } else {
                Image(
                    painter = rememberAsyncImagePainter(model = tab.icon),
                    contentDescription = "Tab favicon",
                    modifier = Modifier.padding(10.dp).size(40.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tab.title ?: tab.url,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = tab.url,
                    style = MaterialTheme.typography.caption,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            IconButton(onClick = { onTabClose(tab) }) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
            }
        }
    }
}