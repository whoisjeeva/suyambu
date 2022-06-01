package io.gopiper.piper.screen.setting.components

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun Toolbar(onBackPressed: () -> Unit, title: String) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back arrow")
            }
        },
        backgroundColor = MaterialTheme.colors.primaryVariant,
        elevation = 0.dp
    )
}