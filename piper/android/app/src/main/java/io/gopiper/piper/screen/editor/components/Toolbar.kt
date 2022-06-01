package io.gopiper.piper.screen.editor.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.gopiper.piper.ui.theme.green

@Composable
fun Toolbar(
    title: String,
    onRun: () -> Unit,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    onBackPressed: () -> Unit,
    onSave: () -> Unit,
    onOpenBrowser: () -> Unit,
    onLogs: () -> Unit
) {
    var dropDownMenuExpanded by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(MaterialTheme.colors.primaryVariant)
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        IconButton(onClick = onBackPressed) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Go back",
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            modifier = Modifier.weight(1f),
            text = "$title.piper",
            fontFamily = FontFamily.Monospace,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.width(10.dp))
        IconButton(onClick = onRun) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Run",
                tint = MaterialTheme.colors.green
            )
        }
        IconButton(onClick = onSave) {
            Icon(
                imageVector = Icons.Default.Save,
                contentDescription = "Save",
            )
        }
        Box {
            IconButton(onClick = { dropDownMenuExpanded = true }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Option menu",
                )
            }

            DropdownMenu(expanded = dropDownMenuExpanded, onDismissRequest = { dropDownMenuExpanded = false }) {
                DropdownMenuItem(onClick = {
                    dropDownMenuExpanded = false
                    onUndo()
                }) {
                    Text(text = "Undo")
                }
                DropdownMenuItem(onClick = {
                    dropDownMenuExpanded = false
                    onRedo()
                }) {
                    Text(text = "Redo")
                }
                DropdownMenuItem(onClick = {
                    dropDownMenuExpanded = false
                    onLogs()
                }) {
                    Text(text = "Logs")
                }
                DropdownMenuItem(onClick = {
                    dropDownMenuExpanded = false
                    onOpenBrowser()
                }) {
                    Text(text = "Open browser")
                }
            }
        }
    }
}