package io.gopiper.piper.screen.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.DriveFileMove
import androidx.compose.material.icons.filled.ImportExport
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.gopiper.piper.R

@Composable
fun DrawerContent(
    onBugReportClick: () -> Unit,
    onImportScriptClick: () -> Unit
) {
    LazyColumn {
        item { DrawerHeader() }
        item {
            Divider()
            Spacer(modifier = Modifier.height(10.dp))
        }
        item {
            Text(
                text = "General",
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.onPrimary.copy(0.5f),
                modifier = Modifier.padding(top = 20.dp, start = 20.dp, end = 20.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
        item {
            NavigationItem(
                icon = Icons.Default.BugReport,
                text = "Bug report",
                onClick = onBugReportClick,
                description = "Report any issues you find on this app"
            )
        }
        item {
            NavigationItem(
                icon = Icons.Default.DriveFileMove,
                text = "Import script",
                onClick = onImportScriptClick,
                description = "Import a piper script file"
            )
        }
    }
}


@Composable
fun NavigationItem(icon: ImageVector, text: String, onClick: () -> Unit, description: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = text)
            Spacer(modifier = Modifier.width(20.dp))
            Column {
                Text(text = text)
                Text(
                    text = description,
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onPrimary.copy(0.5f)
                )
            }
        }
    }
}


@Composable
fun DrawerHeader() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.size(50.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(text = "Piper", fontWeight = FontWeight.Bold)
            Text(
                text = "Web Automation Toolkit",
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.onPrimary.copy(0.5f)
            )
        }
    }
}
