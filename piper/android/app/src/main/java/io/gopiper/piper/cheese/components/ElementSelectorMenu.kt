package io.gopiper.piper.cheese.components

import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun ElementSectorMenu(
    expanded: Boolean = false,
    onDismissRequest: () -> Unit = {},
    onNearbyElement: () -> Unit,
    onSimilarElement: () -> Unit,
    onParentElement: () -> Unit,
    onConfirm: () -> Unit
) {
    DropdownMenu(expanded = expanded, onDismissRequest = onDismissRequest) {
        DropdownMenuItem(onClick = {
            onNearbyElement()
            onDismissRequest()
        }) {
            Text(text = "Switch to nearby element")
        }
        DropdownMenuItem(onClick = {
            onParentElement()
            onDismissRequest()
        }) {
            Text(text = "Switch to parent element")
        }
        DropdownMenuItem(onClick = {
            onSimilarElement()
            onDismissRequest()
        }) {
            Text(text = "Add similar elements")
        }
        DropdownMenuItem(onClick = {
            onConfirm()
            onDismissRequest()
        }) {
            Text(text = "Confirm")
        }
    }
}