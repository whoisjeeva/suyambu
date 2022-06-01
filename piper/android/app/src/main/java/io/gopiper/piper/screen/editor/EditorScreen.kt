package io.gopiper.piper.screen.editor

import android.annotation.SuppressLint
import android.util.Base64
import android.util.Log
import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import io.gopiper.piper.cheese.js
import io.gopiper.piper.core.CoreViewModel
import io.gopiper.piper.core.Screen
import io.gopiper.piper.core.components.LogDialog
import io.gopiper.piper.engine.Injector
import io.gopiper.piper.model.Pipe
import io.gopiper.piper.screen.editor.components.Toolbar
import io.gopiper.piper.util.C
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.gumify.hiper.util.onUiThread
import me.gumify.hiper.util.toast
import org.json.JSONObject

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun EditorScreen(navController: NavController, scriptId: String, coreVm: CoreViewModel) {
    val context = LocalContext.current
    val vm: EditorViewModel = hiltViewModel()
    val isDarkTheme = isSystemInDarkTheme()
    var isLoadingVisible by remember { mutableStateOf(true) }
    var showExitDialog by remember { mutableStateOf(false) }
    var pipe: Pipe? by remember { mutableStateOf(null) }
    val theme by coreVm.theme.collectAsState(initial = 3)
    var showLogDialog by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = vm.openBrowser) {
        if (vm.openBrowser) {
            vm.openBrowser = false
            navController.navigate("${Screen.Browser.route}?op=capture")
        }
    }

    LaunchedEffect(key1 = theme) {
        val darkTheme = when (theme) {
            C.THEME_DARK -> true
            C.THEME_LIGHT -> false
            else -> isDarkTheme
        }
        vm.editorHolder.webView.js("""
            localStorage.setItem("isDark", $darkTheme);
        """.trimIndent())
    }

    LaunchedEffect(key1 = true) {
        pipe = coreVm.getPipe(scriptId)
        if (pipe == null) {
            navController.popBackStack()
        } else {
            vm.editorHolder.webView.js("""
                localStorage.setItem("xml", decodeURIComponent(escape(window.atob("${Base64.encodeToString(pipe!!.workspace.toByteArray(), Base64.NO_WRAP)}"))) )
                localStorage.setItem("variableStack", decodeURIComponent(escape(window.atob("${Base64.encodeToString(pipe!!.variableStack.toByteArray(), Base64.NO_WRAP)}"))) )
                localStorage.setItem("isToolbarToggleVisible", ${coreVm.isToolbarToggleVisible()})
            """.trimIndent())
        }
        delay(1500)
        Injector.IS_EDITOR_START = false
        isLoadingVisible = false
    }

    DisposableEffect(key1 = true) {
        vm.editorHolder.setUiContext(context)
        onDispose { vm.editorHolder.destroy() }
    }

    BackHandler {
        showExitDialog = true
    }

    if (showExitDialog) {
        AlertDialog(
            backgroundColor = MaterialTheme.colors.primaryVariant,
            shape = RoundedCornerShape(10.dp),
            onDismissRequest = { showExitDialog = false },
            title = { Text(text = "Alert") },
            text = { Text(text = "Would you like to save the script before you exit the editor?") },
            confirmButton = {
                TextButton(onClick = {
                    vm.editorHolder.webView.saveScript(scriptId, coreVm)
                    navController.popBackStack()
                }) {
                    Text(text = "Save & Exit", color = MaterialTheme.colors.secondary)
                }
            },
            dismissButton = {
                TextButton(onClick = { navController.popBackStack() }) {
                    Text(text = "Exit", color = MaterialTheme.colors.secondary)
                }
            }
        )
    }

    if (showLogDialog && pipe != null) {
        LogDialog(coreVm = coreVm, scriptId = pipe!!.scriptId) {
            showLogDialog = false
        }
    }

    Scaffold(
        topBar = {
            Toolbar(
                title = pipe?.fileName.toString(),
                onRun = {
                    vm.editorHolder.webView.js("return Gumify.compile();") { code ->
                        coreVm.storeDebugCode(code)
                        coreVm.storeScriptId(pipe?.scriptId.toString())
                        navController.navigate("${Screen.Browser.route}?op=debug")
                    }
                },
                onUndo = {
                    vm.editorHolder.webView.js("Blockly.mainWorkspace.undo(false);")
                },
                onRedo = {
                    vm.editorHolder.webView.js("Blockly.mainWorkspace.undo(true);")
                },
                onBackPressed = {
                    showExitDialog = true
                },
                onSave = {
                    vm.editorHolder.webView.saveScript(scriptId, coreVm)
                },
                onOpenBrowser = {
                    navController.navigate(Screen.Browser.route)
                },
                onLogs = {
                    showLogDialog = true
                }
            )
        }
    ) {
        Box(Modifier.fillMaxSize()) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = {
                    vm.editorHolder.webView
                },
                update = {
                    if (Injector.IS_HTML_SELECTION) {
                        var els = vm.getLastHTMLElements()
                        if (els != "" && els != null) {
                            els = Base64.encodeToString(els.toByteArray(), Base64.NO_WRAP)
                        } else {
                            context.toast("No element selected")
                        }
                        vm.editorHolder.webView.js("""
                            Gumify.updateLastBlock("$els");
                        """.trimIndent())
                        Injector.IS_HTML_SELECTION = false
                    }
                }
            )
            if (isLoadingVisible && Injector.IS_EDITOR_START) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.primaryVariant),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colors.secondary)
                }
            }
        }
    }
}


fun WebView.saveScript(scriptId: String, coreVm: CoreViewModel) {
    js("return Gumify.generate();") {
        try {
            val json = JSONObject(it)
            val code = json.getJSONArray("code")
            val workspace = json.getString("xml")
            val variableStack = json.getJSONObject("stack")
            coreVm.saveScript(
                scriptId = scriptId,
                code = code.toString(),
                workspace = workspace,
                variableStack = variableStack.toString(),
                onSuccess = {
                    onUiThread { context.toast("Script saved") }
                },
                onFailure = {
                    onUiThread { context.toast("Unable to save the script") }
                }
            )
        } catch (e: Exception) {
            context.toast("SyntaxError: $e")
        }
    }
}