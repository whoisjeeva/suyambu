package io.gopiper.piper.util

import android.app.Activity
import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.text.InputType
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.EditText
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import io.gopiper.piper.R
import io.gopiper.piper.ShortcutActivity
import io.gopiper.piper.engine.model.Attr


fun Context.promptDialog(
    defaultValue: String,
    message: String,
    onConfirm: (String) -> Unit,
    onCancel: () -> Unit
) {
    val container = FrameLayout(this)
    val params = FrameLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )

    val input = EditText(this)
    input.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
    input.isSingleLine = false
    input.setText(defaultValue)

    val size = resources.getDimensionPixelSize(R.dimen.dialog_margin)
    params.leftMargin = size
    params.rightMargin = size
    input.layoutParams = params
    container.addView(input)

    val dialog = android.app.AlertDialog.Builder(this)
        .setTitle(message)
        .setView(container)
        .setCancelable(false)
        .setPositiveButton("OK") { _, _ ->
            onConfirm(input.text.toString())
        }
        .setNegativeButton("Cancel") { _, _ ->
            onCancel()
        }
        .create()
    dialog.setOnShowListener {
        input.requestFocus()
    }
    dialog.show()
}


@RequiresApi(Build.VERSION_CODES.O)
fun Context.addShortcut(title: String, scriptId: String, isHeadless: Boolean) {
    val mShortcutInfoBuilder = ShortcutInfo.Builder(this, title)
    mShortcutInfoBuilder.setShortLabel(title)
    mShortcutInfoBuilder.setLongLabel(title)
    mShortcutInfoBuilder.setIcon(Icon.createWithBitmap(AvatarGenerator.avatarImage(this, 512, 1, title).bitmap))
    mShortcutInfoBuilder.setIntent(
        Intent(
            applicationContext,
            ShortcutActivity::class.java
        ).apply {
            action = "LOCATION_SHORTCUT"
            putExtra("scriptId", scriptId)
            putExtra("isHeadless", isHeadless)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
    )
    val mShortcutInfo = mShortcutInfoBuilder.build()
    val mShortcutManager: ShortcutManager? =
        ContextCompat.getSystemService(this, ShortcutManager::class.java)
    mShortcutManager?.requestPinShortcut(mShortcutInfo, null)
}


fun Context.getActivity(): Activity? = when (this) {
    is Activity -> this
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}

fun String.isAscii(): Boolean {
    return matches("\\A\\p{ASCII}*\\z".toRegex())
}

fun Context.vibrate() {
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager =
            getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        getSystemService(VIBRATOR_SERVICE) as Vibrator
    }
    if (Build.VERSION.SDK_INT >= 26) {
        vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        vibrator.vibrate(200)
    }
}


fun WebView.setDesktopMode(enabled: Boolean) {
    val newUserAgent = if (enabled) {
        settings.userAgentString.replace("Mobile", "eliboM").replace("Android", "diordnA")
    } else {
        settings.userAgentString.replace("eliboM", "Mobile").replace("diordnA", "Android")
    }
    settings.userAgentString = newUserAgent
    settings.useWideViewPort = enabled
    settings.loadWithOverviewMode = enabled
    settings.setSupportZoom(enabled)
    settings.builtInZoomControls = enabled
}



