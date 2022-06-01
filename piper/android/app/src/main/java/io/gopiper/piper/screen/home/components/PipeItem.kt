package io.gopiper.piper.screen.home.components

import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.gopiper.piper.R
import io.gopiper.piper.model.Pipe
import io.gopiper.piper.ui.theme.green
import io.gopiper.piper.ui.theme.red

@Composable
fun PipeItem(
    pipe: Pipe,
    onEdit: (Pipe) -> Unit,
    onRun: (Pipe) -> Unit,
    onRunHeadless: (Pipe) -> Unit,
    onStopHeadless: (Pipe) -> Unit,
    onShowLog: (Pipe) -> Unit,
    onRename: (Pipe) -> Unit,
    onDelete: (Pipe) -> Unit,
    onExport: (Pipe) -> Unit,
    createShortcut: (Pipe) -> Unit
) {
    var isRunMenuExpanded by remember { mutableStateOf(false) }
    var isMoreMenuExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 0.dp,
        backgroundColor = MaterialTheme.colors.primary,
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(Modifier.padding(top = 10.dp, bottom = 15.dp, start = 20.dp, end = 5.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(
                        text = pipe.title,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = pipe.fileName + ".piper",
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onPrimary.copy(0.5f),
                        fontFamily = FontFamily.Monospace
                    )
                }
                IconButton(onClick = { isMoreMenuExpanded = true }) {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More")

                    DropdownMenu(expanded = isMoreMenuExpanded, onDismissRequest = { isMoreMenuExpanded = false }) {
                        DropdownMenuItem(onClick = {
                            isMoreMenuExpanded = false
                            onExport(pipe)
                        }) {
                            Text(text = "Export")
                        }
                        DropdownMenuItem(onClick = {
                            isMoreMenuExpanded = false
                            onRename(pipe)
                        }) {
                            Text(text = "Rename")
                        }
                        DropdownMenuItem(onClick = {
                            isMoreMenuExpanded = false
                            onShowLog(pipe)
                        }) {
                            Text(text = "Logs")
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            DropdownMenuItem(onClick = {
                                isMoreMenuExpanded = false
                                createShortcut(pipe)
                            }) {
                                Text(text = "Create shortcut")
                            }
                        }
                        Divider()
                        DropdownMenuItem(onClick = {
                            isMoreMenuExpanded = false
                            onDelete(pipe)
                        }) {
                            Text(text = "Delete", color = MaterialTheme.colors.red)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                Surface(color = MaterialTheme.colors.primaryVariant, shape = CircleShape) {
                    Box(Modifier.clickable(onClick = {
                        if (pipe.isRunning) {
                            onStopHeadless(pipe)
                        } else {
                            isRunMenuExpanded = true
                        }
                    })) {
                        if (pipe.isRunning) {
                            Icon(imageVector = Icons.Default.Stop, contentDescription = "Stop",
                                modifier = Modifier.padding(5.dp),
                                tint = MaterialTheme.colors.red
                            )
                        } else {
                            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Run",
                                modifier = Modifier.padding(5.dp), tint = MaterialTheme.colors.green
                            )
                        }

                        DropdownMenu(expanded = isRunMenuExpanded, onDismissRequest = { isRunMenuExpanded = false }) {
                            DropdownMenuItem(onClick = {
                                isRunMenuExpanded = false
                                onRun(pipe)
                            }) {
                                Text(text = "Run")
                            }
                            DropdownMenuItem(onClick = {
                                isRunMenuExpanded = false
                                onRunHeadless(pipe)
                            }) {
                                Text(text = "Run headless")
                            }
                        }

                    }
                }
                Surface(color = MaterialTheme.colors.primaryVariant, shape = CircleShape) {
                    Box(Modifier.clickable {
                        if (pipe.isRunning) {
                            onShowLog(pipe)
                        } else {
                            onEdit(pipe)
                        }
                    }) {
                        if (pipe.isRunning) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_terminal),
                                contentDescription = "Logs",
                                modifier = Modifier.padding(7.dp).size(20.dp)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Rounded.EditNote,
                                contentDescription = "Edit",
                                modifier = Modifier.padding(5.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}