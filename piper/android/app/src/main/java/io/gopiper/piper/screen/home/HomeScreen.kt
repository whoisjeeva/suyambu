package io.gopiper.piper.screen.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import com.google.android.gms.ads.AdSize
import io.gopiper.piper.Ads
import io.gopiper.piper.Config
import io.gopiper.piper.cheese.components.Keyboard
import io.gopiper.piper.cheese.components.keyboardAsState
import io.gopiper.piper.core.CoreViewModel
import io.gopiper.piper.core.Screen
import io.gopiper.piper.core.components.BannerAd
import io.gopiper.piper.core.components.LogDialog
import io.gopiper.piper.engine.Injector
import io.gopiper.piper.model.Pipe
import io.gopiper.piper.screen.home.components.*
import io.gopiper.piper.services.HeadlessService
import io.gopiper.piper.util.C
import io.gopiper.piper.util.IO
import io.gopiper.piper.util.addShortcut
import io.gopiper.piper.util.getActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.gumify.hiper.util.toast
import org.json.JSONObject


@ExperimentalFoundationApi
@Composable
fun HomeScreen(navController: NavController, coreVm: CoreViewModel) {
    var showCreateDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val activity = context.getActivity()
    var showLoading by rememberSaveable { mutableStateOf(true) }
    var isSearch by remember { mutableStateOf(false) }
    val keyboardState by keyboardAsState()
    var isKeyboardOpen by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = keyboardState) {
        if (isKeyboardOpen && keyboardState == Keyboard.Closed) {
            isSearch = false
            isKeyboardOpen = false
        }
        if (keyboardState == Keyboard.Opened) {
            isKeyboardOpen = true
        }
    }

    LaunchedEffect(key1 = true) {
        Injector.IS_EDITOR_START = true
        delay(1000)
        showLoading = false
    }

    if (showCreateDialog) {
        NewScriptDialog(
            onCreate = { title, fileName ->
                if (title.trim() == "" || fileName.trim() == ".piper") {
                    context.toast("title and filename should not be empty")
                } else {
                    coreVm.createScript(title, fileName) {
                        activity?.also { activity -> Ads.showInterstitial(activity) }
                        Ads.loadInterstitial(context)
                        navController.navigate("${Screen.Editor.route}?scriptId=$it")
                    }
                }
            },
            onDismissRequest = { showCreateDialog = false }
        )
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            Toolbar(
                onSearchClick = {
                    isSearch = true
                },
                isSearch = isSearch,
                onSearch = { query ->
                    searchQuery = query.trim()
                },
                title = searchQuery,
                onSettingClick = {
                    navController.navigate(Screen.Setting.route)
                },
                onNavigationClick = {
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                showCreateDialog = true
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "New script")
            }
        },
        drawerContent = {
            DrawerContent(
                onBugReportClick = {
                    scope.launch { scaffoldState.drawerState.close() }
                    try {
                        val intent = Intent(Intent.ACTION_SENDTO)
                        intent.data = Uri.parse("mailto:" + "tellspidy@gmail.com") // only email apps should handle this
                        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("tellspidy@gmail.com"))
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Piper Bug Report")
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        context.toast("No email app found, please mail to tellspidy@gmail.com", true)
                    }
                },
                onImportScriptClick = {
                    scope.launch { scaffoldState.drawerState.close() }
                    navController.navigate(Screen.Import.route)
                }
            )
        },
        drawerScrimColor = MaterialTheme.colors.primaryVariant.copy(0.7f),
        drawerBackgroundColor = MaterialTheme.colors.primaryVariant
    ) {
        Surface(color = MaterialTheme.colors.primaryVariant, modifier = Modifier.fillMaxSize()) {
            Box {
                HomeScreenContent(navController = navController, coreVm = coreVm, searchQuery = searchQuery)
                if (showLoading) {
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
}


@ExperimentalFoundationApi
@Composable
fun HomeScreenContent(navController: NavController, coreVm: CoreViewModel, searchQuery: String) {
    val pipes by coreVm.filterPipes(searchQuery).collectAsState(initial = listOf())
    val context = LocalContext.current
    var showLogDialog by remember { mutableStateOf(false) }
    var currentPipe: Pipe? by remember { mutableStateOf(null) }
    var showRenameDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showLoadingDialog by remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission Accepted: Do something
            context.toast("Permission granted, try to export again.")
        } else {
            // Permission Denied: Do something
        }
    }
    val scope = rememberCoroutineScope()
    val activity = context.getActivity()


//    var showShortcutDialog by remember { mutableStateOf(false) }
    
//    if (showShortcutDialog) {
//        AlertDialog(
//            backgroundColor = MaterialTheme.colors.primaryVariant,
//            onDismissRequest = {},
//            title = { Text(text = "Shortcut") },
//            text = { Text(text = "How would you like to configure the shortcut?") },
//            confirmButton = {
//                TextButton(onClick = {
//                    scope.launch {
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                            context.addShortcut(currentPipe!!.title, currentPipe!!.scriptId, true)
//                        }
//                    }
//                    showShortcutDialog = false
//                }) {
//                    Text(text = "Use headless", color = MaterialTheme.colors.secondary)
//                }
//            },
//            dismissButton = {
//                TextButton(onClick = {
//                    scope.launch {
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                            context.addShortcut(currentPipe!!.title, currentPipe!!.scriptId, false)
//                        }
//                    }
//                    showShortcutDialog = false
//                }) {
//                    Text(text = "Use browser", color = MaterialTheme.colors.secondary)
//                }
//            }
//        )
//    }
    
    if (showLogDialog && currentPipe != null) {
        LogDialog(coreVm = coreVm, scriptId = currentPipe!!.scriptId) {
            showLogDialog = false
            currentPipe = null
        }
    }

    if (showRenameDialog) {
        NewScriptDialog(
            onCreate = { title, fileName ->
                if (title.trim() == "" || fileName.trim() == ".piper") {
                    context.toast("title and filename should not be empty")
                } else {
                    coreVm.updateTitleAndFileName(currentPipe?.scriptId.toString(), title, fileName)
                }
            },
            onDismissRequest = {
                showRenameDialog = false
                currentPipe = null
            },
            isRename = true,
            titleDefault = currentPipe!!.title,
            fileNameDefault = currentPipe!!.fileName
        )
    }

    if (showDeleteDialog) {
        ScriptDeleteDialog(
            onDismissRequest = {
                showDeleteDialog = false
                currentPipe = null
            },
            onConfirm = {
                coreVm.deletePipe(currentPipe!!.scriptId)
            }
        )
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

    LazyVerticalGrid(
        cells = GridCells.Adaptive(400.dp),
        contentPadding = PaddingValues(start = 15.dp, end = 15.dp, top = 15.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        ) {
        itemsIndexed(pipes) { i, pipe ->
            Column {

                PipeItem(
                    pipe = pipe,
                    onEdit = {
                        activity?.also { activity -> Ads.showInterstitial(activity) }
                        Ads.loadInterstitial(context)
                        navController.navigate("${Screen.Editor.route}?scriptId=${it.scriptId}")
                    },
                    onRun = {
                        coreVm.storeScriptId(it.scriptId)
                        navController.navigate("${Screen.Browser.route}?op=run")
                    },
                    onRunHeadless = {
                        if (activity != null) Ads.showInterstitial(activity)
                        Ads.loadInterstitial(context)
                        val intent = Intent(context, HeadlessService::class.java)
                        intent.putExtra("scriptId", it.scriptId)
                        intent.putExtra("op", "run")
                        context.startService(intent)
                    },
                    onStopHeadless = {
                        if (activity != null) Ads.showInterstitial(activity)
                        Ads.loadInterstitial(context)
                        val intent = Intent(context, HeadlessService::class.java)
                        intent.putExtra("scriptId", it.scriptId)
                        intent.putExtra("op", "stop")
                        context.startService(intent)
                    },
                    onShowLog = {
                        currentPipe = it
                        showLogDialog = true
                    },
                    onRename = {
                        currentPipe = it
                        showRenameDialog = true
                    },
                    onDelete = {
                        currentPipe = it
                        showDeleteDialog = true
                    },
                    onExport = {
                        when (PackageManager.PERMISSION_GRANTED) {
                            ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                                showLoadingDialog = true
                                val json = JSONObject()
                                json.put("scriptId", it.scriptId)
                                json.put("title", it.title)
                                json.put("fileName", it.fileName)
                                json.put("workspace", it.workspace)
                                json.put("code", it.code)
                                json.put("variableStack", it.variableStack)
                                json.put("piperVersion", C.PIPER_ENGINE_VERSION)
                                json.put("UA", coreVm.getUA())
                                IO.writeToSdCard(
                                    context = context,
                                    fileName = it.fileName + ".piper",
                                    content = json.toString(),
                                    Environment.DIRECTORY_DOWNLOADS,
                                    mimeType = "application/octet-stream"
                                ) {
                                    showLoadingDialog = false
                                }
                            }
                            else -> launcher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        }
                    },
                    createShortcut = {
//                    currentPipe = it
//                    showShortcutDialog = true
                        scope.launch {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                context.addShortcut(it.title, it.scriptId, true)
                            }
                        }
                    }
                )


                if (i%5 == 0) {
                    if (!Config.DEBUG_MODE) {
                        Spacer(modifier = Modifier.height(15.dp))
                    }
                    BannerAd(adUnitId = Ads.bannerId, adSize = AdSize.MEDIUM_RECTANGLE)
                }

            }
        }
    }
    
    if (pipes.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = if (searchQuery == "") "Hit the + button to create a script" else "No scripts found matching '$searchQuery'",
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.onPrimary.copy(0.5f),
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(horizontal = 80.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

