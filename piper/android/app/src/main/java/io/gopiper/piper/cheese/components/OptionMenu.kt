package io.gopiper.piper.cheese.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import io.gopiper.piper.cheese.model.Tab
import me.gumify.hiper.util.toast


@Composable
fun OptionMenu(
    expanded: Boolean = false,
    onDismissRequest: () -> Unit = {},
    tint: Color = MaterialTheme.colors.onSurface,
    tab: Tab,
    onNewTab: () -> Unit,
    onExit: () -> Unit,
) {
    val context = LocalContext.current
    var canGoBack by remember { mutableStateOf(tab.canGoBack) }
    var canGoForward by remember { mutableStateOf(tab.canGoForward) }

    LaunchedEffect(key1 = tab.canGoBack ) { canGoBack = tab.canGoBack }
    LaunchedEffect(key1 = tab.canGoForward ) { canGoForward = tab.canGoForward }

    DropdownMenu(
        modifier = Modifier.width(200.dp),
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        DropdownMenuItem(onClick = {}, enabled = false) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Go back",
                    tint = if (canGoBack) tint else tint.copy(0.3f),
                    modifier = Modifier.clickable {
                        if (canGoBack) {
                            tab.goBack()
                            onDismissRequest()
                        }
                    }
                )
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Go forward",
                    tint = if (canGoForward) tint else tint.copy(0.3f),
                    modifier = Modifier.clickable {
                        if (canGoForward) {
                            tab.goForward()
                            onDismissRequest()
                        }
                    }
                )
                Icon(
                    imageVector = if (tab.isLoading) Icons.Default.Close else Icons.Default.Refresh,
                    contentDescription = "Refresh",
                    tint = tint,
                    modifier = Modifier.clickable {
                        if (tab.isLoading) {
                            tab.stopLoading()
                        } else {
                            tab.reload()
                        }
                        onDismissRequest()
                    }
                )
            }
        }

        Divider()

        DropdownMenuItem(onClick = {
            onNewTab()
            onDismissRequest()
        }) {
            Icon(imageVector = Icons.Outlined.AddBox, contentDescription = "New tab")
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "New tab")
        }

        DropdownMenuItem(onClick = {
            val i = Intent(Intent.ACTION_SEND)
            i.type = "text/plain"
            i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL")
            i.putExtra(Intent.EXTRA_TEXT, tab.url)
            context.startActivity(Intent.createChooser(i, "Share URL"))
            onDismissRequest()
        }) {
            Icon(imageVector = Icons.Default.Share, contentDescription = "Share")
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "Share...")
        }

        DropdownMenuItem(onClick = {
            try {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(tab.url)
                context.startActivity(i)
            } catch (e: Exception) {
                context.toast("Unable to use open with")
            }
            onDismissRequest()
        }) {
            Icon(imageVector = Icons.Default.OpenInBrowser, contentDescription = "Open with")
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "Open with...")
        }

        DropdownMenuItem(onClick = onExit) {
            Icon(imageVector = Icons.Default.Logout, contentDescription = "Exit")
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "Exit")
        }
    }
}