package io.gopiper.piper.screen.home.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import io.gopiper.piper.ui.theme.red

@Composable
fun ScriptDeleteDialog(onDismissRequest: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = "Are you sure?") },
        text = { Text(text = "This process can not be undone, do you really want to delete?") },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = "Dismiss", color = MaterialTheme.colors.secondary)
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onConfirm()
                onDismissRequest()
            }) {
                Text(text = "Delete", color = MaterialTheme.colors.red)
            }
        },
        backgroundColor = MaterialTheme.colors.primaryVariant
    )
}