package com.v380.client.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkScheme = darkColorScheme(
    primary = V380Colors.accent,
    onPrimary = V380Colors.bg,
    secondary = V380Colors.surface,
    tertiary = V380Colors.surfaceAlt,
    background = V380Colors.bg,
    surface = V380Colors.surface,
    surfaceVariant = V380Colors.surfaceAlt,
    onBackground = V380Colors.text,
    onSurface = V380Colors.text,
    onSurfaceVariant = V380Colors.textMuted,
    outline = V380Colors.border,
    outlineVariant = V380Colors.borderLight,
    error = V380Colors.danger,
)

@Composable
fun V380Theme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkScheme,
        typography = V380Typography,
        content = content,
    )
}