package io.gopiper.piper.scheduler

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmScheduler: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("hello", "Called...")
    }
}