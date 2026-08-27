package com.slant.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Монохромная OLED палитра SlantTech
val SlantOledBlack = Color(0xFF000000)
val SlantGlassBase = Color(0xFF0E0E0E)
val SlantGlassBorder = Color(0xFF262626)
val SlantPureWhite = Color(0xFFFFFFFF)
val SlantDimText = Color(0xFF757575)
val SlantErrorRed = Color(0xFFFF3B30)

private val DarkScheme = darkColorScheme(
    primary = SlantPureWhite,
    onPrimary = SlantOledBlack,
    background = SlantOledBlack,
    surface = SlantGlassBase,
    onSurface = SlantPureWhite,
    outline = SlantGlassBorder,
    error = SlantErrorRed
)

private val LightScheme = lightColorScheme(
    primary = SlantOledBlack,
    onPrimary = SlantPureWhite,
    background = Color(0xFFF5F5F7),
    surface = Color(0xFFFFFFFF),
    onSurface = SlantOledBlack,
    outline = Color(0xFFD2D2D7),
    error = SlantErrorRed
)

val SlantShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(36.dp)
)

// Модификатор Material 3 + Liquid Glass
fun Modifier.liquidGlass(
    shape: Shape = RoundedCornerShape(24.dp),
    backgroundColor: Color = SlantGlassBase,
    alpha: Float = 0.65f,
    borderWidth: Dp = 1.dp
): Modifier = this
    .clip(shape)
    .background(
        brush = Brush.verticalGradient(
            colors = listOf(
                backgroundColor.copy(alpha = alpha),
                backgroundColor.copy(alpha = (alpha + 0.2f).coerceAtMost(0.95f))
            )
        ),
        shape = shape
    )
    .border(
        width = borderWidth,
        brush = Brush.verticalGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.25f),
                Color.White.copy(alpha = 0.04f)
            )
        ),
        shape = shape
    )

@Composable
fun SlantTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkScheme else LightScheme,
        shapes = SlantShapes,
        content = content
    )
}
