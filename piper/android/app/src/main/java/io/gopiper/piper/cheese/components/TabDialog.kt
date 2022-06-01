package io.gopiper.piper.cheese.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.gopiper.piper.cheese.model.Tab

@Composable
fun TabDialog(
    currentTab: Tab,
    tabs: List<Tab>,
    backgroundColor: Color = MaterialTheme.colors.surface,
    tabActiveColor: Color = Color.Green,
    tabBackgroundColor: Color = Color.LightGray,
    iconTint: Color = Color.White,
    onTabClick: (Tab) -> Unit = {},
    onTabClose: (Tab) -> Unit = {},
    onNewTab: () -> Unit = {},
    onDismissRequest: () -> Unit = {}
) {
    val config = LocalConfiguration.current

    Dialog(
        onDismissRequest =  onDismissRequest
    ) {
        Surface(
            color = backgroundColor,
            modifier = Modifier.requiredHeight(config.screenHeightDp.dp * 0.9f)
                .fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 5.dp)) {
                TopAppBar(backgroundColor = backgroundColor, elevation = 0.dp) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(onClick = onDismissRequest) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = iconTint
                            )
                        }

                        TextButton(onClick = { onNewTab() }, modifier = Modifier.padding(end = 10.dp)) {
                            Icon(imageVector = Icons.Default.Add,
                                contentDescription = "New tab", tint = iconTint)
                            Text(text = "New tab", color = iconTint)
                        }
                    }
                }

                LazyColumn(
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    items(tabs) { tab ->
                        if (tab.tabId == currentTab.tabId) {
                            TabItem(
                                tab = tab,
                                isActive = true,
                                activeColor = tabActiveColor,
                                backgroundColor = tabBackgroundColor,
                                onTabClick = onTabClick,
                                onTabClose = onTabClose
                            )
                        } else {
                            TabItem(
                                tab = tab,
                                activeColor = tabActiveColor,
                                backgroundColor = tabBackgroundColor,
                                onTabClick = onTabClick,
                                onTabClose = onTabClose
                            )
                        }
                    }
                }
            }
        }
    }

}

