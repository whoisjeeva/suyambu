package io.gopiper.piper

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.AndroidEntryPoint
import io.gopiper.piper.core.CoreViewModel
import io.gopiper.piper.services.HeadlessService

@AndroidEntryPoint
class ShortcutActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val coreVm: CoreViewModel by viewModels()
        val scriptId = intent?.getStringExtra("scriptId")
        val isHeadless = intent?.getBooleanExtra("isHeadless", false) ?: false


        if (isHeadless) {
            val intent = Intent(this, HeadlessService::class.java)
            intent.putExtra("scriptId", scriptId)
            intent.putExtra("op", "run")
            startService(intent)
        } else {
//            coreVm.storeScriptId(scriptId.toString())
//            val intent = Intent(this, MainActivity::class.java)
//            intent.putExtra("shortcutRun", true)
//            intent.putExtra("scriptId", scriptId)
//            startActivity(intent)
        }
        finish()
    }

}