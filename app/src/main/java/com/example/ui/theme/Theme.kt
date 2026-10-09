package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Navy900,
    onPrimary = Color.White,
    primaryContainer = Navy800,
    onPrimaryContainer = Color.White,
    secondary = Emerald600,
    onSecondary = Color.White,
    secondaryContainer = Emerald100,
    onSecondaryContainer = Navy900,
    tertiary = Amber600,
    onTertiary = Color.White,
    tertiaryContainer = Amber100,
    onTertiaryContainer = Navy900,
    error = Rose600,
    onError = Color.White,
    errorContainer = Rose100,
    onErrorContainer = Rose600,
    background = Color(0xFFFFFFFF),
    onBackground = Navy900,
    surface = Color(0xFFFFFFFF),
    onSurface = Navy900,
    surfaceVariant = Slate100,
    onSurfaceVariant = Navy700,
    outline = Slate200,
    outlineVariant = Slate400
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF38BDF8),
    onPrimary = Navy950,
    primaryContainer = Navy800,
    onPrimaryContainer = Color.White,
    secondary = Emerald500,
    onSecondary = Navy950,
    secondaryContainer = Color(0xFF064E3B),
    onSecondaryContainer = Emerald100,
    tertiary = Amber500,
    onTertiary = Navy950,
    background = Navy950,
    onBackground = Color.White,
    surface = Navy900,
    onSurface = Color.White,
    surfaceVariant = Navy800,
    onSurfaceVariant = Slate200,
    outline = Navy700,
    outlineVariant = Slate400
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

