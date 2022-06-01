package io.gopiper.piper.screen.setting

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.gopiper.piper.screen.setting.components.ResetBrowserDialog
import io.gopiper.piper.screen.setting.components.SettingCategory
import io.gopiper.piper.screen.setting.components.SettingItem
import io.gopiper.piper.screen.setting.components.Toolbar
import io.gopiper.piper.util.C

@Composable
fun SettingScreen(navController: NavController) {
    val vm: SettingViewModel = hiltViewModel()

    Scaffold(
        topBar = {
            Toolbar(
                onBackPressed = { navController.popBackStack() },
                title = "Settings"
            )
        }
    ) {
        Surface(color = MaterialTheme.colors.primaryVariant, modifier = Modifier.fillMaxSize()) {
            SettingScreenContent(vm = vm)
        }
    }
}


@Composable
fun SettingScreenContent(vm: SettingViewModel) {
    var showResetBrowserDialog by remember { mutableStateOf(false) }
    var isThemeOptionExpanded by remember { mutableStateOf(false) }
    val theme by vm.theme.collectAsState(initial = 3)

    if (showResetBrowserDialog) {
        ResetBrowserDialog {
            showResetBrowserDialog = false
        }
    }

    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item { SettingCategory(text = "Browser") }
        item {
            SettingItem {
                Text(
                    text = "Reset browser",
                    modifier = Modifier
                        .clickable { showResetBrowserDialog = true }
                        .padding(vertical = 20.dp, horizontal = 20.dp)
                        .fillMaxWidth()
                )
            }
        }
        item {
            SettingItem {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { vm.updateClearBrowserBeforeEx(!vm.isClearBrowserBeforeExecution) }
                ) {
                    Text(
                        text = "Reset browser before script execution",
                        modifier = Modifier
                            .padding(vertical = 20.dp, horizontal = 20.dp)
                            .weight(1f)
                    )
                    Checkbox(
                        checked = vm.isClearBrowserBeforeExecution,
                        onCheckedChange = {
                            vm.updateClearBrowserBeforeEx(it)
                        }
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }
        }

        item {
            SettingItem {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { vm.updateDoNotWaitForLoading(!vm.isDoNotWaitForLoading) }
                ) {
                    Text(
                        text = "Do not wait for website loading",
                        modifier = Modifier
                            .padding(vertical = 20.dp, horizontal = 20.dp)
                            .weight(1f)
                    )
                    Checkbox(
                        checked = vm.isDoNotWaitForLoading,
                        onCheckedChange = {
                            vm.updateDoNotWaitForLoading(it)
                        }
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }
        }

        item {
            SettingItem {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { vm.updateSkipError(!vm.isSkipError) }
                ) {
                    Column(
                        Modifier
                            .weight(1f)
                            .padding(vertical = 20.dp, horizontal = 20.dp)) {
                        Text(text = "Skip Errors", modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = "Ignore any errors and keep running the script", modifier = Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.caption, color = MaterialTheme.colors.onPrimary.copy(0.5f))
                    }
                    Checkbox(
                        checked = vm.isSkipError,
                        onCheckedChange = {
                            vm.updateSkipError(it)
                        }
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }
        }


        item { SettingCategory(text = "Personalization") }

        item {
            SettingItem {
                Column(Modifier.fillMaxWidth()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { isThemeOptionExpanded = !isThemeOptionExpanded }
                            .padding(vertical = 20.dp, horizontal = 20.dp)
                    ) {
                        Text(
                            text = "Theme",
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = when (theme) {
                                C.THEME_DARK -> "Dark theme"
                                C.THEME_LIGHT -> "Light theme"
                                else -> "Use device theme"
                            },
                            style = MaterialTheme.typography.caption,
                            color = MaterialTheme.colors.onPrimary.copy(0.5f)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Icon(imageVector = if (isThemeOptionExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, contentDescription = "Expand more")
                    }

                    AnimatedVisibility(visible = isThemeOptionExpanded) {
                        Column {
                            Divider()
                            Text(
                                text = "Use device theme",
                                modifier = Modifier
                                    .clickable {
                                        isThemeOptionExpanded = false
                                        vm.updateTheme(C.THEME_SYSTEM)
                                    }
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp, horizontal = 10.dp)
                            )

                            Divider()
                            Text(
                                text = "Dark theme",
                                modifier = Modifier
                                    .clickable {
                                        isThemeOptionExpanded = false
                                        vm.updateTheme(C.THEME_DARK)
                                    }
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp, horizontal = 10.dp)
                            )

                            Divider()
                            Text(
                                text = "Light theme",
                                modifier = Modifier
                                    .clickable {
                                        isThemeOptionExpanded = false
                                        vm.updateTheme(C.THEME_LIGHT)
                                    }
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp, horizontal = 10.dp)
                            )
                        }
                    }

                }
            }
        }


        item {
            SettingItem {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { vm.updateToolbarToggleHandleVisible(!vm.isToolbarToggleHandleVisible) }
                ) {
                    Text(
                        text = "Enable editor toolbar toggle",
                        modifier = Modifier
                            .padding(vertical = 20.dp, horizontal = 20.dp)
                            .weight(1f)
                    )
                    Checkbox(
                        checked = vm.isToolbarToggleHandleVisible,
                        onCheckedChange = {
                            vm.updateToolbarToggleHandleVisible(it)
                        }
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }
        }


        item { SettingCategory(text = "Others") }

        item {
            SettingItem {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 20.dp, horizontal = 20.dp)) {
                    Text(
                        text = "Piper engine",
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "v${C.PIPER_ENGINE_VERSION}", color = MaterialTheme.colors.onPrimary.copy(0.5f))
                }
            }
        }

        item {
            SettingItem {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 20.dp, horizontal = 20.dp)) {
                    Text(
                        text = "Build number",
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = C.getBuildNumber(), color = MaterialTheme.colors.onPrimary.copy(0.5f))
                }
            }
        }

    }
}

