package com.example.trendalert.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = TrendAlertTheme.trendAlertBlue,
    secondary = TrendAlertTheme.trendAlertLightBlue,
    tertiary = TrendAlertTheme.trendAlertDarkBlue,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = TrendAlertTheme.trendAlertBlue,
    secondary = TrendAlertTheme.trendAlertLightBlue,
    tertiary = TrendAlertTheme.trendAlertDarkBlue,
    background = TrendAlertTheme.lightBackground,
    surface = TrendAlertTheme.lightSurface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black
)

@Composable
fun TrendAlertTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

object TrendAlertTheme {
    val trendAlertBlue = Color(0xFF0088CC)
    val trendAlertDarkBlue = Color(0xFF006699)
    val trendAlertLightBlue = Color(0xFF00AAFF)

    // Light Theme Colors
    val lightBackground = Color(0xFFE3F2FD)
    val lightSurface = Color.White
    val lightText = Color.Black.copy(alpha = 0.87f)
    val lightTextSecondary = Color.Gray.copy(alpha = 0.7f)
    val lightDivider = Color.Gray.copy(alpha = 0.5f)

    // Dark Theme Colors
    val darkBackground = Color(0xFF121212)
    val darkSurface = Color(0xFF242424)
    val darkText = Color.White.copy(alpha = 0.87f)
    val darkTextSecondary = Color.White.copy(alpha = 0.6f)
    val darkDivider = Color.White.copy(alpha = 0.2f)

    @Composable
    fun getBackgroundColor() = if (isSystemInDarkTheme()) darkBackground else lightBackground

    @Composable
    fun getSurfaceColor() = if (isSystemInDarkTheme()) darkSurface else lightSurface

    @Composable
    fun getTextColor() = if (isSystemInDarkTheme()) darkText else lightText

    @Composable
    fun getTextSecondaryColor() = if (isSystemInDarkTheme()) darkTextSecondary else lightTextSecondary

    @Composable
    fun getDividerColor() = if (isSystemInDarkTheme()) darkDivider else lightDivider
}