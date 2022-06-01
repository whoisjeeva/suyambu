package io.gopiper.piper

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import io.gopiper.piper.core.CoreViewModel
import io.gopiper.piper.core.Navigation
import io.gopiper.piper.engine.Injector
import io.gopiper.piper.scheduler.AlarmScheduler
import io.gopiper.piper.ui.theme.PiperTheme
import io.gopiper.piper.ui.theme.ThemeContent
import io.gopiper.piper.util.C
import io.gopiper.piper.util.IO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.gumify.hiper.util.Tart
import me.gumify.hiper.util.WeeDB
import me.gumify.hiper.util.toast
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
@ExperimentalFoundationApi
class MainActivity : ComponentActivity() {
    @Inject lateinit var wee: WeeDB
    @Inject lateinit var tart: Tart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Config.PACKAGE_NAME = applicationContext.packageName
        Ads.initInterstitial()
        Ads.loadInterstitial(this)

//        val calendar = Calendar.getInstance()
//        calendar.set(
//            calendar.get(Calendar.YEAR),
//            calendar.get(Calendar.MONTH),
//            calendar.get(Calendar.DAY_OF_MONTH),
//            1,
//            0,
//            0
//        )
//        val millis = calendar.timeInMillis
//
//        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val intent = Intent(this, AlarmScheduler::class.java)
//        // use unique requestCode to schedule different scripts
//        val pendingIntent = PendingIntent.getBroadcast(
//            this,
//            0,
//            intent,
//            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
//        )
////        1000L * 60 * 2
//        alarmManager.setInexactRepeating(AlarmManager.RTC, millis, 1000L * 60 * 5, pendingIntent)
//
////        alarmManager.set(AlarmManager.RTC, millis, pendingIntent)
//        toast("Scheduled")

        CoroutineScope(Dispatchers.IO).launch {
            Injector.ELEMENT_FINDER_SCRIPT = IO.readFromAsset(this@MainActivity, "element_finder.js") ?: ""
            if (!wee.contains(C.SETTING_CLEAR_BROWSER_BEFORE_EX)) {
                wee.put(C.SETTING_CLEAR_BROWSER_BEFORE_EX, true)
//                wee.put(C.SETTING_DO_NOT_WAIT_FOR_LOADING, true)
//                wee.put(C.SETTING_SHOW_EDITOR_HANDLE, true)
            }
            if (!wee.contains("UA")) {
                launch(Dispatchers.Main) {
                    val webView = WebView(this@MainActivity)
                    val UA = webView.settings.userAgentString
                    wee.put("UA", UA)
                }
            }
        }

        setContent {
            ThemeContent {
                val theme = tart.getInt("theme").collectAsState(initial = 3)
                PiperTheme(theme = theme) {
                    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.primaryVariant) {
                        App()
                    }
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun App() {
    val navController = rememberNavController()
    val coreVm: CoreViewModel = viewModel()
    Navigation(navController = navController, coreVm = coreVm)
}
