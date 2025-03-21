package com.akari.ulpack.repoapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val darkColorScheme = darkColorScheme()
private val lightColorScheme = lightColorScheme()

@Composable
fun RepoTheme(
    darkMode: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val color = if (darkMode) darkColorScheme else lightColorScheme
    MaterialTheme(
        colorScheme = color,
        content = content
    )
}