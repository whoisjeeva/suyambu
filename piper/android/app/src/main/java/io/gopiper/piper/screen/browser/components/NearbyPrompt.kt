package io.gopiper.piper.screen.browser.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.gopiper.piper.cheese.Browser
import io.gopiper.piper.cheese.js

@Composable
fun NearbyPrompt(browserState: Browser?, onDismissRequest: () -> Unit) {
    var tagName by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismissRequest) {
        Column(modifier = Modifier.background(MaterialTheme.colors.primaryVariant)) {
            Column(modifier = Modifier.padding(top = 20.dp, start = 20.dp, end = 20.dp)) {
                Text(text = "Prompt", color = Color.White, style = MaterialTheme.typography.h6)
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = tagName,
                    onValueChange = { tagName = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Tag name", color = Color.White) },
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = MaterialTheme.colors.onPrimary,
                        focusedIndicatorColor = MaterialTheme.colors.onPrimary,
                        focusedLabelColor = MaterialTheme.colors.onPrimary,
                        backgroundColor = Color.Transparent,
                        cursorColor = MaterialTheme.colors.onPrimary,
                    ),
                )
            }
            Row(Modifier.padding(10.dp).fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = {
                    onDismissRequest()
                    tagName = ""
                }) {
                    Text("Cancel", color = Color.White)
                }
                Spacer(modifier = Modifier.width(10.dp))
                TextButton(onClick = {
                    browserState?.currentTab?.value?.webView?.js("""
                            return window.elementFinder.findNearestElement("$tagName");
                        """.trimIndent()) {
                        onDismissRequest()
                        tagName = ""
                    }
                }) {
                    Text("Find", color = Color.White)
                }
            }
        }
    }
}