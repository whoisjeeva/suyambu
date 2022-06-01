package io.gopiper.piper.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import io.gopiper.piper.core.CoreViewModel
import io.gopiper.piper.model.Pipe

@Composable
fun LogDialog(coreVm: CoreViewModel, scriptId: String, onDismissRequest: () -> Unit) {
    val logs by coreVm.allLogs(scriptId).collectAsState(initial = listOf())

    Dialog(onDismissRequest = onDismissRequest) {

        Surface(shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxSize()) {
            Column(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.primaryVariant)
                    .padding(top = 20.dp, end = 20.dp, start = 20.dp, bottom = 5.dp)) {
                Text(text = "Logs", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(20.dp))
                SelectionContainer(modifier = Modifier.weight(1f)) {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(logs) { log ->
                            Text(text = log.value, modifier = Modifier.padding(vertical = 10.dp))
                            Divider()
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismissRequest) {
                        Text(text = "Dismiss", color = MaterialTheme.colors.secondary)
                    }
                }
            }

        }

    }
}