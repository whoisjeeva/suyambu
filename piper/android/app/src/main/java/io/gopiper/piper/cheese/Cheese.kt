package io.gopiper.piper.cheese

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.ShortText
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import io.gopiper.piper.R
import io.gopiper.piper.cheese.components.*
import io.gopiper.piper.engine.Injector
import io.gopiper.piper.engine.NativeInterface
import io.gopiper.piper.ui.theme.red
import kotlinx.coroutines.delay

@Composable
fun Cheese(
    backgroundColor: Color = MaterialTheme.colors.primaryVariant,
    contentColor: Color = MaterialTheme.colors.onPrimary,
    homePage: String = "bing",
    tabBackgroundColor: Color = MaterialTheme.colors.primaryVariant,
    tabActiveColor: Color = MaterialTheme.colors.secondary,
    browserClient: BrowserClient? = null,
    onExit: () -> Unit = {},
    newTabUrl: String? = null,
    onNearbyElement: () -> Unit = {},
    onParentElement: () -> Unit = {},
    onSimilarElement: () -> Unit = {},
    onConfirm: () -> Unit = {},
    onStartRecording: () -> Unit = {},
    isFabVisible: MutableState<Boolean>,
    onStop: () -> Unit,
    onShowLogs: () -> Unit,
    onLoaded: (Browser) -> Unit,
) {
    val vm: CheeseViewModel = hiltViewModel()
    var isEditMode by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    var showOptionMenu by remember { mutableStateOf(false) }
    var isTabDialogVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var showElementSelectorMenu by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = homePage) {
        val searchEngine = when (homePage) {
            "google" -> "https://www.google.com"
            "duckduckgo" -> "https://www.duckduckgo.com"
            "bing" -> "https://www.bing.com"
            else -> "https://www.google.com"
        }
        vm.setSearchEngine(searchEngine)
    }

    LaunchedEffect(key1 = true) {
        vm.setBrowserClient(browserClient)
        if (newTabUrl != null) {
            vm.newTab(newTabUrl)
        }
    }

    BackHandler(enabled = vm.canGoBack || isTabDialogVisible) {
        if (!isTabDialogVisible) {
            vm.currentTab?.goBack()
        } else {
            isTabDialogVisible = false
        }
    }

    DisposableEffect(key1 = true) {
        vm.setUiContextToBrowser(context)
        onDispose {
            vm.removeUiContextFromBrowser()
        }
    }



    if (isTabDialogVisible) {
        TabDialog(
            currentTab = vm.currentTab!!,
            tabs = vm.tabs,
            onTabClick = { tab ->
                isTabDialogVisible = false
                vm.switchTab(vm.tabs.indexOf(tab))
            },
            onTabClose = { tab ->
                vm.removeTab(tab)
            },
            onNewTab = {
                isTabDialogVisible = false
                vm.newTab()
            },
            tabBackgroundColor = tabBackgroundColor,
            tabActiveColor = tabActiveColor,
            onDismissRequest = {
                isTabDialogVisible = false
            },
            backgroundColor = backgroundColor,
            iconTint = contentColor
        )
    }


    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    backgroundColor = backgroundColor,
                    contentColor = contentColor,
                    elevation = 0.dp
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        if (isEditMode) {
                            UrlField(
                                contentColor = contentColor,
                                initialUrl = TextFieldValue(vm.currentTab!!.url).copy(selection = TextRange(0, vm.currentTab!!.url.length)),
                                modifier = Modifier.focusRequester(focusRequester),
                                switchEditMode = { isEditMode = it }
                            ) { query ->
                                vm.loadUrl(query)
                            }
                            LaunchedEffect(Unit) {
                                focusRequester.requestFocus()
                            }
                        } else {

                            if (Injector.IS_RUN || Injector.IS_DEBUG) {
                                IconButton(onClick = onShowLogs) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_terminal),
                                        contentDescription = "Logs",
                                        tint = contentColor,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            } else {
                                IconButton(onClick = { isTabDialogVisible = true }) {
                                    TabCount(vm.tabs.size, color = contentColor)
                                }
                            }

                            TitleBar(
                                text = vm.currentTab?.title ?: vm.currentTab!!.url.let {
                                    var url: String
                                    url = it.split("://").last()
                                    if (url.startsWith("www.")) {
                                        url = url.replace("www.", "")
                                    }
                                    url
                                },
                                contentColor = contentColor,
                                onClick = { isEditMode = true }
                            )

                            if (Injector.IS_RUN || Injector.IS_DEBUG) {
                                IconButton(onClick = onStop) {
                                    Icon(
                                        imageVector = Icons.Default.Stop,
                                        contentDescription = "Stop",
                                        tint = MaterialTheme.colors.red
                                    )
                                }
                            } else if (isFabVisible.value) {
                                Box {
                                    IconButton(onClick = { showElementSelectorMenu = true }) {
                                        Surface(
                                            color = MaterialTheme.colors.secondary,
                                            shape = CircleShape
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Done,
                                                contentDescription = "Done",
                                                modifier = Modifier
                                                    .size(28.dp)
                                                    .padding(5.dp)
                                            )
                                        }
                                    }
                                    ElementSectorMenu(
                                        expanded = showElementSelectorMenu,
                                        onDismissRequest = { showElementSelectorMenu = false },
                                        onNearbyElement = onNearbyElement,
                                        onParentElement = onParentElement,
                                        onSimilarElement = onSimilarElement,
                                        onConfirm = onConfirm
                                    )
                                }
                            } else {
                                if (Injector.IS_CAPTURE) {
                                    IconButton(onClick = {
                                        onStartRecording()
                                        isFabVisible.value = true
                                        vm.currentTab?.webView?.js(Injector.ELEMENT_FINDER_SCRIPT)
                                        vm.currentTab?.webView?.js("window.elementFinder.init();")
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.RadioButtonChecked,
                                            contentDescription = "Capture",
                                            tint = MaterialTheme.colors.red
                                        )
                                    }
                                }

                                Box {
                                    OptionMenu(
                                        expanded = showOptionMenu,
                                        onDismissRequest = { showOptionMenu = false },
                                        tab = vm.currentTab!!,
                                        onNewTab = { vm.newTab() },
                                        onExit = onExit,
                                    )
                                    IconButton(onClick = { showOptionMenu = true }) {
                                        Icon(
                                            imageVector = Icons.Default.MoreVert,
                                            contentDescription = "Option menu",
                                            tint = contentColor
                                        )
                                    }
                                }
                            }

                        }



                    }

                }

                if (vm.currentTab?.isLoading == true) {
                    if (vm.currentTab?.progress == -1) {
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colors.secondary)
                    } else {
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), progress = vm.currentTab!!.progress / 100f, color = MaterialTheme.colors.secondary)
                    }
                }
            }
        }
    ) {
        AndroidView(
            factory = {
                vm.webViewHash = null
                FrameLayout(it)
            },
            update = { tabContainer ->
                if (vm.webViewHash != vm.currentTab!!.tabId) {
                    tabContainer.removeAllViews()
                    (vm.currentTab!!.webView.parent as? ViewGroup)?.removeView(vm.currentTab!!.webView)
                    vm.currentTab?.webView?.addJavascriptInterface(NativeInterface(context), "Native")
                    tabContainer.addView(vm.currentTab!!.webView)
                    vm.webViewHash = vm.currentTab!!.tabId
                }
                vm.currentTab!!.load()
            },
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
        )

    }


    LaunchedEffect(key1 = true) {
        onLoaded(vm.getBrowser())
    }
}