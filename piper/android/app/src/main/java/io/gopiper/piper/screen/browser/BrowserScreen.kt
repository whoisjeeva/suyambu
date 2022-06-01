package io.gopiper.piper.screen.browser

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.gopiper.piper.cheese.Browser
import io.gopiper.piper.cheese.Cheese
import io.gopiper.piper.cheese.js
import io.gopiper.piper.core.CoreViewModel
import io.gopiper.piper.core.components.LogDialog
import io.gopiper.piper.engine.Injector
import io.gopiper.piper.engine.Piper
import io.gopiper.piper.screen.browser.components.NearbyPrompt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.gumify.hiper.util.toast

@Composable
fun BrowserScreen(navController: NavController, coreVm: CoreViewModel) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val vm: BrowserViewModel = hiltViewModel()
    var isScriptCompleted by remember { mutableStateOf(false) }
    var browserState: Browser? by remember { mutableStateOf(null) }
    var isFindNearbyElementDialogVisible by remember { mutableStateOf(false) }
    var showRecordHint by remember { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }
    val isFabVisible = remember { mutableStateOf(false) }
    var piper: Piper? by remember { mutableStateOf(null) }
    var showLogDialog by remember { mutableStateOf(false) }
    var scriptId: String? by remember { mutableStateOf(null) }

    LaunchedEffect(key1 = true) {
        scriptId = coreVm.getStoredScriptId()
        if (Injector.IS_CAPTURE) {
            Injector.IS_HTML_SELECTION = true
        }
    }

    if (showLogDialog) {
        LogDialog(coreVm = coreVm, scriptId = scriptId.toString()) {
            showLogDialog = false
        }
    }

//    if (isScriptCompleted) {
//        AlertDialog(
//            onDismissRequest = {},
//            shape = RoundedCornerShape(10.dp),
//            title = { Text("Alert", color = MaterialTheme.colors.onPrimary) },
//            text = { Text("The script execution is completed.", color = MaterialTheme.colors.onPrimary) },
//            backgroundColor = MaterialTheme.colors.primaryVariant,
//            confirmButton = {
//                TextButton(onClick = {
//                    isScriptCompleteDialogVisible = false
//                    navController.popBackStack()
//                }) {
//                    Text("Exit", color = MaterialTheme.colors.secondary)
//                }
//            }
//        )
//    }

    if (showRecordHint) {
        AlertDialog(
            shape = RoundedCornerShape(10.dp),
            onDismissRequest = { showRecordHint = false },
            title = { Text("Hint", color = MaterialTheme.colors.onPrimary) },
            text = { Text("Long press on elements to select", color = MaterialTheme.colors.onPrimary) },
            backgroundColor = MaterialTheme.colors.primaryVariant,
            confirmButton = { TextButton(onClick = { showRecordHint = false }) { Text("OK", color = MaterialTheme.colors.secondary) } }
        )
    }

    if (showExitDialog) {
        AlertDialog(
            shape = RoundedCornerShape(10.dp),
            onDismissRequest = {},
            title = { Text("Alert", color = MaterialTheme.colors.onPrimary) },
            text = {
                if (isScriptCompleted) {
                    Text("The script execution is completed.", color = MaterialTheme.colors.onPrimary)
                } else {
                    Text("Would you like to exist the browser now?", color = MaterialTheme.colors.onPrimary)
                }
            },
            backgroundColor = MaterialTheme.colors.primaryVariant,
            confirmButton = {
                TextButton(onClick = {
                    showExitDialog = false
                    navController.popBackStack()
                }) {
                    Text("Exit", color = MaterialTheme.colors.secondary)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    isFabVisible.value = false
                    showExitDialog = false

                    if (!isScriptCompleted) {
                        browserState?.currentTab?.value?.webView?.reload()
                    }
                }) {
                    Text("Stay", color = MaterialTheme.colors.secondary)
                }
            }
        )
    }

    if (isFindNearbyElementDialogVisible) {
        NearbyPrompt(browserState = browserState) {
            isFindNearbyElementDialogVisible = false
        }
    }

    Scaffold(modifier = Modifier.background(Color.White)) {
        Cheese(
            homePage = "google",
            backgroundColor = MaterialTheme.colors.primaryVariant,
            tabBackgroundColor = MaterialTheme.colors.primary,
            tabActiveColor = MaterialTheme.colors.secondary,
            onExit = {
                navController.popBackStack()
            },
            newTabUrl = null,
            isFabVisible = isFabVisible,
            onLoaded = { browser ->
                browserState = browser
                if (Injector.IS_CAPTURE) {
                    browser.currentTab.value?.reload()
                } else if (Injector.IS_RUN || Injector.IS_DEBUG) {
                    browser.tabs.forEach { browser.removeTab(it) }
                    val code = coreVm.getDebugCode()

                    piper = Piper(
                        context = context,
                        browser = browser,
                        onScriptComplete = {
                            Injector.IS_RUN = false
                            Injector.IS_DEBUG = false
                            isScriptCompleted = true
                            showExitDialog = true
                        },
                        onRegisterLog = { id, log ->
                            coreVm.updateLog(id, log)
                        },
                        scriptId = scriptId!!
                    )
                    scope.launch(Dispatchers.IO) {
                        coreVm.deleteLogs(scriptId!!)
                        coreVm.getPipe(scriptId!!)?.also {
                            if (Injector.IS_DEBUG) piper?.init(code) else piper?.init(it.code)
                        }
                    }
                }
            },
            onNearbyElement = {
                isFindNearbyElementDialogVisible = true
            },
            onParentElement = {
                browserState?.currentTab?.value?.webView?.js("""
                    return window.elementFinder.switchParenElement()
                """.trimIndent()) {
                    if (it == "true") {
                        context.toast("Parent elements added")
                    } else {
                        context.toast("We couldn't find any parent elements")
                    }
                }
            },
            onSimilarElement = {
                browserState?.currentTab?.value?.webView?.js("""
                            return window.elementFinder.addSimilarElements()
                        """.trimIndent()) {
                    if (it == "true") {
                        context.toast("Similar elements added")
                    } else {
                        context.toast("We couldn't find any similar elements")
                    }
                }
            },
            onConfirm = {
                browserState?.currentTab?.value?.webView?.js("""
                            return window.elementFinder.confirmElements()
                        """.trimIndent())
                context.toast("Confirmed, you can close the browser now.")
                scope.launch {
                    delay(300)
                    showExitDialog = true
                }
            },
            onStartRecording = {
                if (!vm.isRecordHintShowed) {
                    showRecordHint = true
                    scope.launch { vm.isRecordHintShowed = true }
                }
            },
            onStop = {
                browserState?.currentTab?.value?.webView?.stopLoading()
                piper?.isTerminated = true
                Injector.IS_RUN = false
                Injector.IS_DEBUG = false
                isScriptCompleted = true
                showExitDialog = true
                piper = null
            },
            onShowLogs = {
                showLogDialog = true
            }
        )
    }
}