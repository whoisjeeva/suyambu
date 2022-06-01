package io.gopiper.piper.core

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.accompanist.insets.ProvideWindowInsets
import io.gopiper.piper.engine.Injector
import io.gopiper.piper.screen.browser.BrowserScreen
import io.gopiper.piper.screen.editor.EditorScreen
import io.gopiper.piper.screen.home.HomeScreen
import io.gopiper.piper.screen.import.ImportScreen
import io.gopiper.piper.screen.setting.SettingScreen

sealed class Screen(
    val title: String,
    val route: String
) {
    object Home: Screen("Home", "home")
    object Editor: Screen("Editor", "editor")
    object Browser: Screen("Browser", "browser")
    object Setting: Screen("Setting", "setting")
    object Import: Screen("Import", "import")
}

@ExperimentalFoundationApi
@Composable
fun Navigation(navController: NavHostController, coreVm: CoreViewModel) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route) {
            ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
                HomeScreen(navController = navController, coreVm = coreVm)
            }
        }

        composable(
            route = "${Screen.Editor.route}?scriptId={scriptId}",
            arguments = listOf(
                navArgument("scriptId") {
                    defaultValue = ""
                    type = NavType.StringType
                }
            )
        ) {
            val scriptId = it.arguments?.getString("scriptId") ?: ""
            ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
                EditorScreen(navController = navController, scriptId = scriptId, coreVm = coreVm)
            }
        }

        composable(
            route = "${Screen.Browser.route}?op={op}",
            arguments = listOf(
                navArgument("op") {
                    defaultValue = "open"
                    type = NavType.StringType
                }
            )
        ) {
            val op = it.arguments?.getString("op") ?: "open"
            Injector.IS_CAPTURE = op == "capture"
            Injector.IS_RUN = op == "run"
            Injector.IS_DEBUG = op == "debug"
            ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
                BrowserScreen(navController = navController, coreVm = coreVm)
            }
        }

        composable(route = Screen.Setting.route) {
            ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
                SettingScreen(navController = navController)
            }
        }

        composable(route = Screen.Import.route) {
            ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
                ImportScreen(navController = navController)
            }
        }
    }
}
