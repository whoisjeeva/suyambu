package io.gopiper.piper.screen.home.components

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@SuppressLint("InlinedApi")
@Composable
fun OverlayPermissionDialog(onDismissRequest: () -> Unit) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

    AlertDialog(
        backgroundColor = MaterialTheme.colors.primaryVariant,
        onDismissRequest = onDismissRequest,
        title = { Text(text = "Require Permission") },
        text = { Text(text = "The app requires draw over other app permission to run in the background") },
        confirmButton = {
            TextButton(onClick = {
                onDismissRequest()
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:${context.packageName}")
                )
                launcher.launch(intent)
            }) {
                Text(text = "Grant", color = MaterialTheme.colors.secondary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = "Cancel", color = MaterialTheme.colors.secondary)
            }
        }
    )
}