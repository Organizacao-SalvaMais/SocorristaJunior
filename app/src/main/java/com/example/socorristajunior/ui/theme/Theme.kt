package com.example.socorristajunior.ui.theme

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

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFF37272), //cor de fundo do rótulo de passo
    secondary = Color(0xFF111822),//Color.White,//Color(0xFFEB0029),//cor do texto de passo
    tertiary = Color.White,// cor do texto
    background = Color(0xFF0E141B),// cor de fundo
    onBackground = Color(0xFF151D28), //fundo do espaço reservado para imagem
    onPrimary = Color(0xFF111822) // cor do card
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFF23D3D),// cor passo
    secondary =Color.Black, //Color.Black,// cor do texto de passo
    tertiary = Color.Black,//cor do texto
    background = Color(0xFFF7F9FB), //cor de fundo
    onBackground = Color(0xFFF1F7FE) //fundo do espaço reservado para imagem


    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun SocorristaJuniorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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