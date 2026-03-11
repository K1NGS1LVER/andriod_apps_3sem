package com.danielpaul.financetracker.theme

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = AccentTeal,
    onPrimary = PrimaryNavy,
    primaryContainer = PrimaryNavyLight,
    onPrimaryContainer = AccentTealLight,
    secondary = AccentTealDark,
    onSecondary = NeutralWhite,
    background = PrimaryNavy,
    onBackground = NeutralWhite,
    surface = SurfaceDark,
    onSurface = NeutralWhite,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = TextSecondary,
    outline = CardVariantDark,
    error = ExpenseRed,
    onError = NeutralWhite
)

private val LightColorScheme = lightColorScheme(
    primary = AccentTealDark,
    onPrimary = NeutralWhite,
    primaryContainer = IncomeGreenLight,
    onPrimaryContainer = AccentTealDark,
    secondary = AccentTeal,
    onSecondary = NeutralWhite,
    background = SurfaceLight,
    onBackground = TextPrimary,
    surface = CardLight,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = TextSecondary,
    outline = TextHint,
    error = ExpenseRed,
    onError = NeutralWhite
)

@Composable
fun FinanceTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val activity = view.context.findActivity()
            activity?.window?.let { window ->
                window.statusBarColor = Color.Transparent.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}

private tailrec fun Context.findActivity(): Activity? =
    when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }
