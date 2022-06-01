package io.gopiper.piper.screen.setting.components

import android.webkit.CookieManager
import android.webkit.WebStorage
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import me.gumify.hiper.util.toast

@Composable
fun ResetBrowserDialog(onDismissRequest: () -> Unit) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = onDismissRequest,
        backgroundColor = MaterialTheme.colors.primaryVariant,
        title = { Text(text = "Are you sure?") },
        text = { Text(text = "Do you really want to clear cache and cookies?") },
        confirmButton = {
            TextButton(onClick = {
                onDismissRequest()
                WebStorage.getInstance().deleteAllData()
                CookieManager.getInstance().removeAllCookies(null)
                CookieManager.getInstance().flush()
                context.toast("Cache & cookies are cleared")
            }) {
                Text(text = "Reset browser", color = MaterialTheme.colors.secondary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = "Cancel", color = MaterialTheme.colors.secondary)
            }
        }
    )
}