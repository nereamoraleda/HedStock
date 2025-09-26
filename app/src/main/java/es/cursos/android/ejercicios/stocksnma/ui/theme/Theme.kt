package es.cursos.android.ejercicios.stocksnma.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF8D6E63),   // Marrón suave
    secondary = Color(0xFFA1887F), // Marrón tostado
    background = Color(0xFFFFF3E0), // Beige claro
    surface = Color(0xFFFFE0B2),    // Tono crema
    onPrimary = Color.White,        // Texto sobre botones marrones
    onSecondary = Color.Black,      // Texto sobre elementos secundarios
    onBackground = Color(0xFF5D4037), // Marrón oscuro para contraste
    onSurface = Color(0xFF4E342E)  // Marrón más oscuro
)


private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF6D4C41),    // Marrón oscuro
    secondary = Color(0xFF5D4037),  // Marrón chocolate
    background = Color(0xFF3E2723), // Marrón casi negro
    surface = Color(0xFF4E342E),    // Marrón intenso
    onPrimary = Color(0xFFFFCCBC),  // Beige suave para contraste
    onSecondary = Color(0xFFD7CCC8), // Grisáceo claro para texto secundario
    onBackground = Color(0xFFFFE0B2), // Beige crema para mejor visibilidad
    onSurface = Color(0xFFFFCCBC)  // Tono claro sobre marrón oscuro
)


@Composable
fun StocksNMATheme(
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}