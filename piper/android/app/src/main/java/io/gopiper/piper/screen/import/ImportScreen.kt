package io.gopiper.piper.screen.import

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material.icons.filled.NoteAdd
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.gopiper.piper.core.CoreViewModel
import io.gopiper.piper.core.decode
import io.gopiper.piper.model.Pipe
import io.gopiper.piper.util.IO
import io.pipend.open.hiper.Hiper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.gumify.hiper.util.toast
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.*

@Composable
fun ImportScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Import Script")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back arrow")
                    }
                },
                backgroundColor = MaterialTheme.colors.primaryVariant,
                elevation = 0.dp
            )
        }
    ) {
        Surface(color = MaterialTheme.colors.primaryVariant) {
            ImportScreenContent()
        }
    }
}


@Composable
fun ImportScreenContent() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val coreVm: CoreViewModel = hiltViewModel()
    var showLoadingDialog by remember { mutableStateOf(false) }
    var url by remember { mutableStateOf("") }

    val scriptPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { scriptUri ->
        if (scriptUri != null) {
            showLoadingDialog = true
            scope.launch(Dispatchers.IO) {
                val script = IO.readUri(context, scriptUri)
                if (script == null) {
                    launch(Dispatchers.Main) { context.toast("Unable to read the script") }
                } else {
                    try {
                        val pipeString = script.toString().decode()
                        val json = JSONObject(pipeString)
                        val piperVersion = json.getInt("piperVersion")
                        val pipe = Pipe(
                            scriptId = UUID.randomUUID().toString(),
                            title = json.getString("title"),
                            fileName = json.getString("fileName"),
                            workspace = json.getString("workspace"),
                            code = json.getString("code"),
                            variableStack = json.getString("variableStack"),
                            UA = json.getString("UA") ?: ""
                        )
                        coreVm.insertPipe(pipe)
                        launch(Dispatchers.Main) { context.toast("Imported") }
                    } catch (e: Exception) {
                        launch(Dispatchers.Main) { context.toast("Invalid script") }
                    }
                }
                showLoadingDialog = false
            }
        }
    }

    if (showLoadingDialog) {
        Dialog(onDismissRequest = {}) {
            Surface(
                color = MaterialTheme.colors.primaryVariant,
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(20.dp)) {
                    CircularProgressIndicator(color = MaterialTheme.colors.secondary)
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(text = "Loading...")
                }
            }
        }
    }

    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Box(Modifier.padding(20.dp)) {
            Surface(color = MaterialTheme.colors.primary, shape = RoundedCornerShape(10.dp)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(20.dp, 40.dp)) {
                    Button(
                        onClick = { scriptPicker.launch("application/octet-stream") },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colors.secondary)
                    ) {
                        Text(text = "Choose a file")
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Click the Choose button to import a piper script from your device",
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onPrimary.copy(0.5f),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(text = "OR", color = MaterialTheme.colors.onPrimary.copy(0.5f))

                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedTextField(
                        value = url,
                        onValueChange = { url = it },
                        placeholder = { Text(text = "Enter a script URL") },
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = MaterialTheme.colors.onPrimary,
                            focusedIndicatorColor = MaterialTheme.colors.onPrimary,
                            focusedLabelColor = MaterialTheme.colors.onPrimary,
                            backgroundColor = Color.Transparent,
                            cursorColor = MaterialTheme.colors.onPrimary,
                        ),
                        modifier = Modifier.widthIn(100.dp, 400.dp),
                        singleLine = true,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            showLoadingDialog = true
                            val hiper = Hiper.getInstance().async()
                            try {
                                hiper.get(url) {
                                    if (it.isSuccessful) {
                                        try {
                                            val pipeString = it.text.toString().decode()
                                            val json = JSONObject(pipeString)
                                            val piperVersion = json.getInt("piperVersion")
                                            val pipe = Pipe(
                                                scriptId = UUID.randomUUID().toString(),
                                                title = json.getString("title"),
                                                fileName = json.getString("fileName"),
                                                workspace = json.getString("workspace"),
                                                code = json.getString("code"),
                                                variableStack = json.getString("variableStack"),
                                                UA = json.getString("UA") ?: ""
                                            )
                                            coreVm.insertPipe(pipe)
                                            scope.launch(Dispatchers.Main) { context.toast("Imported") }
                                        } catch (e: Exception) {
                                            scope.launch(Dispatchers.Main) { context.toast("Invalid script") }
                                        }
                                    } else {
                                        scope.launch(Dispatchers.Main) {
                                            context.toast("Network error occurred")
                                        }
                                    }
                                    showLoadingDialog = false
                                    url = ""
                                }
                            } catch (e: Exception) {
                                showLoadingDialog = false
                                context.toast("Invalid URL")
                            }
                        },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colors.secondary)
                    ) {
                        Text(text = "Import")
                    }
                }
            }
        }
    }
}
