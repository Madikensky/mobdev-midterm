package kz.qarjy.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF00695C),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFCDEAE4),
    onPrimaryContainer = Color(0xFF00201B),
    secondary = Color(0xFF4A635E),
    background = Color(0xFFF6FAF9),
    surface = Color(0xFFF6FAF9),
    surfaceVariant = Color(0xFFE3EDEA)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF7FD6C6),
    onPrimary = Color(0xFF00201B),
    primaryContainer = Color(0xFF004D43),
    onPrimaryContainer = Color(0xFFCDEAE4),
    secondary = Color(0xFFB0CCC6),
    background = Color(0xFF101514),
    surface = Color(0xFF101514),
    surfaceVariant = Color(0xFF1F2927)
)

@Composable
fun QarjyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content
    )
}
