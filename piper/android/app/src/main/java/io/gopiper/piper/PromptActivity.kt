package io.gopiper.piper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import io.gopiper.piper.ui.theme.PiperTheme
import io.gopiper.piper.ui.theme.ThemeContent
import io.gopiper.piper.util.toSlug
import me.gumify.hiper.util.Tart
import me.gumify.hiper.util.WeeDB
import me.gumify.hiper.util.toast
import javax.inject.Inject

@AndroidEntryPoint
class PromptActivity : ComponentActivity() {
    @Inject lateinit var wee: WeeDB
    @Inject lateinit var tart: Tart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val message = intent?.getStringExtra("message")
        val defaultValue = intent?.getStringExtra("defaultValue")
        val key = intent?.getStringExtra("key").toString()

        wee.remove(key)

        setContent {
            ThemeContent {
                val theme = tart.getInt("theme").collectAsState(initial = 3)

                PiperTheme(theme = theme) {
                    var value by remember { mutableStateOf(defaultValue.toString()) }

                    Surface(modifier = Modifier.fillMaxSize(), color = Color.Transparent) {

                        AlertDialog(
                            onDismissRequest = {
                                wee.put(key, defaultValue ?: "")
                                finish()
                            },
                            title = {
                                // Text(text = message.toString())
                            },
                            text = {
                                OutlinedTextField(
                                    value = value,
                                    onValueChange = {
                                        value = it
                                    },
                                    label = { Text(text = message.toString()) },
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
                            },
                            confirmButton = {
                                TextButton(onClick = {
                                    wee.put(key, value)
                                    finish()
                                }) {
                                    Text(text = "OK", color = MaterialTheme.colors.secondary)
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = {
                                    wee.put(key, defaultValue ?: "")
                                    finish()
                                }) {
                                    Text(text = "Dismiss", color = MaterialTheme.colors.secondary)
                                }
                            },
                            backgroundColor = MaterialTheme.colors.primaryVariant,
                            shape = RoundedCornerShape(10.dp)
                        )


                    }
                }


            }

        }
    }
}