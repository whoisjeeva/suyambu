package io.gopiper.piper.services

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.view.WindowManager
import androidx.core.app.NotificationCompat
import dagger.hilt.android.AndroidEntryPoint
import io.gopiper.piper.PiperApp
import io.gopiper.piper.R
import io.gopiper.piper.cheese.Browser
import io.gopiper.piper.data.repo.PiperRepo
import io.gopiper.piper.engine.Piper
import io.gopiper.piper.model.HeadlessProcess
import io.gopiper.piper.model.PiperLog
import io.gopiper.piper.util.C
import io.gopiper.piper.util.vibrate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HeadlessService: Service() {
    @Inject
    lateinit var piperRepo: PiperRepo
    private lateinit var notification: NotificationCompat.Builder
    private lateinit var notificationManager: NotificationManager
    private val processes = ArrayList<HeadlessProcess>()

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notification = NotificationCompat.Builder(this, PiperApp.CHANNEL_ID)
            .setContentTitle("Headless Browser")
            .setContentText("Total of ${processes.size} script(s) running")
            .setSmallIcon(R.drawable.ic_notification)
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val scriptId = intent?.getStringExtra("scriptId")
        val op = intent?.getStringExtra("op")

        if (scriptId != null && op == "run") {
            CoroutineScope(Dispatchers.Main).launch {
                piperRepo.getPipe(scriptId)?.also { pipe ->
                    val browser = Browser(this@HeadlessService, isHeadless = true)
                    val piper = Piper(
                        context = this@HeadlessService,
                        browser = browser,
                        onScriptComplete = {
                            Log.d("HeadlessService", "Terminated")
                            stopProcess(scriptId)
                        },
                        onRegisterLog = { id, log ->
                            CoroutineScope(Dispatchers.Main).launch {
                                piperRepo.insertLog(PiperLog(id, log))
                            }
                        },
                        scriptId = scriptId,
                        isHeadless = true
                    )
                    val process = HeadlessProcess(
                        scriptId = scriptId,
                        browser = browser,
                        pipe = pipe,
                        piper = piper
                    )
                    processes.add(process)
                    startProcess(process)
                }
                if (processes.isEmpty()) {
                    stopSelf()
                }
            }
        } else if (scriptId != null && op == "stop") {
            stopProcess(scriptId)
        }
        startForeground(C.SCRIPT_RUNNER_NOTIFICATION_ID, notification.build())
        return START_NOT_STICKY
    }

    fun startProcess(process: HeadlessProcess) {
        vibrate()
        notification.setContentText("Total of ${processes.size} script(s) running")
        notificationManager.notify(C.SCRIPT_RUNNER_NOTIFICATION_ID, notification.build())

        CoroutineScope(Dispatchers.IO).launch {
            piperRepo.deleteLogs(process.pipe.scriptId)
            process.pipe.isRunning = true
            piperRepo.updatePipe(process.pipe)
            process.piper.init(process.pipe.code)
        }
    }

    fun stopProcess(scriptId: String) {
        val process = processes.find { it.scriptId == scriptId }
        CoroutineScope(Dispatchers.Main).launch {
            if (process != null) {
                process.browser.currentTab.value?.webView?.stopLoading()
                process.piper.isTerminated = true
                process.pipe.isRunning = false
                piperRepo.updatePipe(process.pipe)
                processes.remove(process)
            } else {
                piperRepo.getPipe(scriptId)?.also {
                    it.isRunning = false
                    piperRepo.updatePipe(it)
                }
            }

            if (processes.isEmpty()) {
                stopSelf()
            }
        }
    }
}