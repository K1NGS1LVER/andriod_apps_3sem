package com.example.recipebook.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val SpaceDarkColorScheme = darkColorScheme(
    primary          = NebulaBlue,
    onPrimary        = SpaceBlack,
    secondary        = NebulaPurple,
    onSecondary      = StarWhite,
    background       = SpaceBlack,
    onBackground     = OnSpaceBlack,
    surface          = SpaceNavy,
    onSurface        = StarWhite,
    surfaceVariant   = SpaceNavy,
    onSurfaceVariant = CosmicGrey,
    error            = ErrorRed,
    onError          = StarWhite
)

@Composable
fun RecipeBookTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = SpaceDarkColorScheme,
        typography  = Typography,
        content     = content
    )
}