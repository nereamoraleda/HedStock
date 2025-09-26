package es.cursos.android.ejercicios.stocksnma.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import es.cursos.android.ejercicios.stocksnma.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.ci_gamedev)),
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),

    // Texto normal o descripciones
    bodyMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.ci_gamedev)),
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal
    ),


    // Barra de búsqueda (Placeholder y texto ingresado)
    bodySmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.ci_gamedev)),
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal
    ),


    // Títulos importantes (como pantallas)
    titleLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.ci_gamedev)),
        fontSize = 22.sp,
        fontWeight = FontWeight.Medium
    ),


    // Subtítulos o encabezados dentro de cards o listas
    titleMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.ci_gamedev)),
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium
    ),


    // Encabezado menor, botones grandes
    titleSmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.ci_gamedev)),
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium
    ),


    // Títulos grandes (Ej. Pantallas principales)
    displayLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.ci_gamedev)),
        fontSize = 32.sp,
        //fontWeight = FontWeight.Bold
    ),


    // Títulos de secciones (Ej. Secciones dentro de una pantalla)
    displayMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.ci_gamedev)),
        fontSize = 24.sp,
        //fontWeight = FontWeight.SemiBold
    ),


    // Botones, etiquetas llamativas
    labelLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.ci_gamedev)),
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium
    ),

    // Etiquetas menores, chips
    labelMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.ci_gamedev)),
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium
    ),

    // Etiquetas muy pequeñas
    labelSmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.ci_gamedev)),
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium
    )
)