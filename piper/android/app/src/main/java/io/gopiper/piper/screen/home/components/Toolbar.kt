package io.gopiper.piper.screen.home.components

import android.view.KeyEvent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.gopiper.piper.cheese.components.keyboardAsState
import io.gopiper.piper.util.titlecase

@Composable
fun Toolbar(
    onSearchClick: () -> Unit,
    isSearch: Boolean = false,
    onSearch: (query: String) -> Unit,
    title: String = "",
    onSettingClick: () -> Unit,
    onNavigationClick: () -> Unit
) {
    var query by remember { mutableStateOf(TextFieldValue("")) }
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    var isMenuOpen by remember { mutableStateOf(false) }

    if (isSearch) {
        TopAppBar(elevation = 0.dp, backgroundColor = MaterialTheme.colors.primaryVariant) {
            TextField(
                value = query,
                onValueChange = { query = it.copy(text = it.text.trim()) },
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester)
                    .onKeyEvent {
                        if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                            focusManager.clearFocus()
                            onSearch(query.text.trim())
                            return@onKeyEvent true
                        }
                        false
                    }
                    .fillMaxSize(),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = MaterialTheme.colors.onPrimary,
                    disabledTextColor = Color.Transparent,
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    cursorColor = MaterialTheme.colors.onPrimary
                ),
                placeholder = {
                    Text(
                        text = "Search scripts",
                        color = MaterialTheme.colors.onPrimary.copy(0.3f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                maxLines = 1,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search,
                    keyboardType = KeyboardType.Uri
                ),
                keyboardActions = KeyboardActions(onSearch = {
                    focusManager.clearFocus()
                    onSearch(query.text.trim())
                }, onDone = {
                    focusManager.clearFocus()
                    onSearch(query.text.trim())
                }),
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
            )

            LaunchedEffect(key1 = true) {
                focusRequester.requestFocus()
                val text = query.text.trim()
                query = query.copy(selection = TextRange(0, text.length))
            }
        }
    } else {
        TopAppBar(
            backgroundColor = MaterialTheme.colors.primaryVariant,
            elevation = 0.dp,
            title = { Text(if (title == "") "Piper" else title.titlecase(), color = MaterialTheme.colors.onPrimary) },
            navigationIcon = {
                IconButton(onClick = onNavigationClick) {
                    Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu", tint = MaterialTheme.colors.onPrimary)
                }
            },
            actions = {
                IconButton(onClick = onSearchClick) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search for script", tint = MaterialTheme.colors.onPrimary)
                }
                IconButton(onClick = { isMenuOpen = true }) {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Option menu", tint = MaterialTheme.colors.onPrimary)
                    DropdownMenu(expanded = isMenuOpen, onDismissRequest = { isMenuOpen = false }) {
                        DropdownMenuItem(onClick = {
                            isMenuOpen = false
                            onSettingClick()
                        }) {
                            Text(text = "Settings")
                        }
                    }
                }
            }
        )
    }
}