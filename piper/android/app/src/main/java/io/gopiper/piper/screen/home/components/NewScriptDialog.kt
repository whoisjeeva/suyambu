package io.gopiper.piper.screen.home.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.gopiper.piper.util.toSlug
import me.gumify.hiper.util.toast

@Composable
fun NewScriptDialog(
    onDismissRequest: () -> Unit,
    onCreate: (String, String) -> Unit,
    isRename: Boolean = false,
    titleDefault: String = "",
    fileNameDefault: String = ""
) {
    var title by remember { mutableStateOf(titleDefault) }
    var fileName by remember { mutableStateOf(fileNameDefault) }
    val context = LocalContext.current

    Dialog(onDismissRequest = onDismissRequest) {
        Card(backgroundColor = MaterialTheme.colors.primaryVariant, shape = RoundedCornerShape(10.dp)) {
            Column(
                modifier = Modifier
                    .padding(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 10.dp)
            ) {
                if (isRename) {
                    Text(text = "Rename script", style = MaterialTheme.typography.h6)
                } else {
                    Text(text = "Create a script", style = MaterialTheme.typography.h6)
                }

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        if ("[a-zA-Z0-9 ]+".toRegex().matches(it)) {
                            title = it
                            fileName = it.toSlug()
                        } else {
                            context.toast("Only letters and numbers are allowed")
                        }
                    },
                    label = { Text(text = "Script title") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = MaterialTheme.colors.onPrimary,
                        focusedIndicatorColor = MaterialTheme.colors.onPrimary,
                        focusedLabelColor = MaterialTheme.colors.onPrimary,
                        backgroundColor = Color.Transparent,
                        cursorColor = MaterialTheme.colors.onPrimary,
                    ),
                    textStyle = TextStyle(fontFamily = FontFamily.Monospace)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = "$fileName.piper",
                    onValueChange = {
                        if ("[a-zA-Z0-9\\-.]+".toRegex().matches(it)) {
                            fileName = it.replace(".piper", "")
                        } else {
                            context.toast("Only letters, numbers and hyphens are allowed")
                        }
                    },
                    label = { Text(text = "Filename") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = MaterialTheme.colors.onPrimary,
                        focusedIndicatorColor = MaterialTheme.colors.onPrimary,
                        focusedLabelColor = MaterialTheme.colors.onPrimary,
                        backgroundColor = Color.Transparent,
                        cursorColor = MaterialTheme.colors.onPrimary,

                        ),
                    textStyle = TextStyle(fontFamily = FontFamily.Monospace)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismissRequest) {
                        Text(text = "Cancel", color = MaterialTheme.colors.secondary)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    TextButton(onClick = {
                        onCreate(title, fileName)
                        onDismissRequest()
                    }) {
                        if (isRename) {
                            Text(text = "Rename", color = MaterialTheme.colors.secondary)
                        } else {
                            Text(text = "Create", color = MaterialTheme.colors.secondary)
                        }
                    }
                }
            }
        }
    }
}