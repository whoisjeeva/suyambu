package io.gopiper.piper.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import io.gopiper.piper.util.C

val Colors.red: Color
    @Composable
    get() = Color(0xffd32f2f)

val Colors.green: Color
    @Composable
    get() = Color(0xff388e3c)

private val DarkColorPalette = darkColors(
    primary = Primary,
    primaryVariant = PrimaryVariant,
    secondary = Accent,
    onPrimary = Color.White,
)

private val LightColorPalette = lightColors(
    primary = Color.White,
    primaryVariant = LightGray,
    secondary = Accent,
    onPrimary = Primary
)

private object JetNewsRippleTheme : RippleTheme {
    // Here you should return the ripple color you want
    // and not use the defaultRippleColor extension on RippleTheme.
    // Using that will override the ripple color set in DarkMode
    // or when you set light parameter to false
    @Composable
    override fun defaultColor(): Color = MaterialTheme.colors.onPrimary

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleTheme.defaultRippleAlpha(
        Color.Black,
        lightTheme = !isSystemInDarkTheme()
    )
}

@Composable
fun PiperTheme(theme: State<Int>? = null, content: @Composable () -> Unit) {
    val systemUiController = rememberSystemUiController()

    val darkTheme = when (theme?.value) {
        C.THEME_LIGHT -> false
        C.THEME_DARK -> true
        else -> isSystemInDarkTheme()
    }

    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    systemUiController.setSystemBarsColor(colors.primaryVariant)

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes
    ) {
        val customTextSelectionColors = TextSelectionColors(
            handleColor = MaterialTheme.colors.secondary,
            backgroundColor = MaterialTheme.colors.secondary.copy(alpha = 0.4f)
        )
        CompositionLocalProvider(
            LocalTextSelectionColors provides customTextSelectionColors,
            LocalRippleTheme provides JetNewsRippleTheme,
            content = content
        )
    }
}

@Composable
fun ThemeContent(content: @Composable () -> Unit) {
    content()
}
