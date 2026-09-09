package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val NovaDarkColorScheme = darkColorScheme(
    primary = NovaVioletPrimary,
    onPrimary = NovaBackground,
    primaryContainer = NovaVioletSubtle,
    onPrimaryContainer = NovaVioletLight,
    secondary = NovaVioletLight,
    onSecondary = NovaBackground,
    secondaryContainer = NovaSurfaceSubtle,
    onSecondaryContainer = NovaTextPrimary,
    tertiary = NovaVioletNeon,
    onTertiary = NovaBackground,
    background = NovaBackground,
    onBackground = NovaTextPrimary,
    surface = NovaSurface,
    onSurface = NovaTextPrimary,
    surfaceVariant = NovaSurfaceElevated,
    onSurfaceVariant = NovaTextSecondary,
    outline = NovaGlassBorder,
    outlineVariant = NovaDivider
)

@Composable
fun NovaTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = NovaDarkColorScheme,
        typography = Typography,
        content = content
    )
}
