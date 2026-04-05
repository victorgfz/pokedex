package com.pokedex.app.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary          = Color(0xFFE63946),
    onPrimary        = Color.White,
    secondary        = Color(0xFF457B9D),
    background       = Color(0xFF1A1A2E),
    surface          = Color(0xFF16213E),
    onBackground     = Color.White,
    onSurface        = Color.White,
    surfaceVariant   = Color(0xFF0F3460),
    onSurfaceVariant = Color(0xFFB0B3B8)
)

private val LightColorScheme = lightColorScheme(
    primary          = Color(0xFFE63946),
    onPrimary        = Color.White,
    secondary        = Color(0xFF457B9D),
    background       = Color(0xFFF8F9FA),
    surface          = Color.White,
    onBackground     = Color(0xFF1A1A2E),
    onSurface        = Color(0xFF1A1A2E),
    surfaceVariant   = Color(0xFFEEF0F2),
    onSurfaceVariant = Color(0xFF546E7A)
)

@Composable
fun PokeDexTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        content     = content
    )
}