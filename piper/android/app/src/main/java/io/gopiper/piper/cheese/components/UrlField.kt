package io.gopiper.piper.cheese.components

import android.graphics.Rect
import android.view.KeyEvent
import android.view.ViewTreeObserver
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun RowScope.UrlField(
    modifier: Modifier = Modifier,
    initialUrl: TextFieldValue,
    contentColor: Color,
    switchEditMode: (Boolean) -> Unit = {},
    onSearch: (String) -> Unit = {}
) {
    var query by remember { mutableStateOf(initialUrl) }
    val focusManager = LocalFocusManager.current
    val keyboardState by keyboardAsState()
    var isKeyboardOpen by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = keyboardState) {
        if (isKeyboardOpen && keyboardState == Keyboard.Closed) {
            switchEditMode(false)
            isKeyboardOpen = false
        }
        if (keyboardState == Keyboard.Opened) {
            isKeyboardOpen = true
        }
    }


    TextField(
        value = query,
        onValueChange = { query = it },
        modifier = modifier.weight(1f)
            .onKeyEvent {
                if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER){
                    focusManager.clearFocus()
                    switchEditMode(false)
                    if (query.text.isNotBlank()) {
                        onSearch(query.text.trim())
                    }
                    return@onKeyEvent true
                }
                false
            }
            .onFocusChanged {
                switchEditMode(it.isFocused)
            }
            .fillMaxSize(),
        colors = TextFieldDefaults.textFieldColors(
            textColor = contentColor,
            disabledTextColor = Color.Transparent,
            backgroundColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            cursorColor = contentColor
        ),
        placeholder = {
            Text(
                text = "Search or type a URL",
                color = contentColor.copy(0.3f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search, keyboardType = KeyboardType.Uri),
        keyboardActions = KeyboardActions(onSearch = {
            focusManager.clearFocus()
            switchEditMode(false)
            if (query.text.isNotBlank()) {
                onSearch(query.text.trim())
            }
        }, onDone = {
            focusManager.clearFocus()
            switchEditMode(false)
            if (query.text.isNotBlank()) {
                onSearch(query.text.trim())
            }
        }),
        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
    )

}


enum class Keyboard {
    Opened, Closed
}



@Composable
fun keyboardAsState(): State<Keyboard> {
    val keyboardState = remember { mutableStateOf(Keyboard.Closed) }
    val view = LocalView.current
    DisposableEffect(view) {
        val onGlobalListener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = Rect()
            view.getWindowVisibleDisplayFrame(rect)
            val screenHeight = view.rootView.height
            val keypadHeight = screenHeight - rect.bottom
            keyboardState.value = if (keypadHeight > screenHeight * 0.15) {
                Keyboard.Opened
            } else {
                Keyboard.Closed
            }
        }
        view.viewTreeObserver.addOnGlobalLayoutListener(onGlobalListener)

        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalListener)
        }
    }

    return keyboardState
}
