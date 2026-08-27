package com.slant.ui.theme

import android.content.Context
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Фирменные цвета SlantTech
val SlantOledBlack = Color(0xFF000000)
val SlantGlassBase = Color(0xFF0E0E0E)
val SlantGlassBorder = Color(0xFF262626)
val SlantPureWhite = Color(0xFFFFFFFF)
val SlantDimText = Color(0xFF8E8E93)
val SlantErrorRed = Color(0xFFFF3B30)

// Светлая тема SlantTech
val SlantLightBackground = Color(0xFFF2F2F7)
val SlantLightGlassBase = Color(0xFFFFFFFF)
val SlantLightGlassBorder = Color(0xFFD1D1D6)
val SlantLightOnBackground = Color(0xFF000000)
val SlantLightDimText = Color(0xFF6C6C70)

// Монохромная тёмная схема
private val MonochromeDarkScheme = darkColorScheme(
    primary = SlantPureWhite,
    onPrimary = SlantOledBlack,
    primaryContainer = Color(0xFF2C2C2E),
    onPrimaryContainer = SlantPureWhite,
    secondary = Color(0xFFE5E5EA),
    onSecondary = SlantOledBlack,
    background = SlantOledBlack,
    onBackground = SlantPureWhite,
    surface = SlantGlassBase,
    onSurface = SlantPureWhite,
    surfaceVariant = Color(0xFF1C1C1E),
    onSurfaceVariant = Color(0xFFD1D1D6),
    outline = SlantGlassBorder,
    error = SlantErrorRed
)

// Монохромная светлая схема
private val MonochromeLightScheme = lightColorScheme(
    primary = SlantOledBlack,
    onPrimary = SlantPureWhite,
    primaryContainer = Color(0xFFE5E5EA),
    onPrimaryContainer = SlantOledBlack,
    secondary = Color(0xFF3A3A3C),
    onSecondary = SlantPureWhite,
    background = SlantLightBackground,
    onBackground = SlantLightOnBackground,
    surface = SlantLightGlassBase,
    onSurface = SlantLightOnBackground,
    surfaceVariant = Color(0xFFE5E5EA),
    onSurfaceVariant = SlantLightDimText,
    outline = SlantLightGlassBorder,
    error = SlantErrorRed
)

// Fallback Material You (Monet) схемы для Android < 12
private val MonetDarkSchemeFallback = darkColorScheme(
    primary = Color(0xFFADC6FF),
    onPrimary = Color(0xFF002E69),
    primaryContainer = Color(0xFF1B3B6F),
    onPrimaryContainer = Color(0xFFD8E2FF),
    secondary = Color(0xFFBBC7DB),
    onSecondary = Color(0xFF263140),
    background = Color(0xFF0F141C),
    onBackground = Color(0xFFE1E2E8),
    surface = Color(0xFF141A24),
    onSurface = Color(0xFFE1E2E8),
    surfaceVariant = Color(0xFF202632),
    onSurfaceVariant = Color(0xFFC4C7D0),
    outline = Color(0xFF394354),
    error = SlantErrorRed
)

private val MonetLightSchemeFallback = lightColorScheme(
    primary = Color(0xFF005AC1),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFD8E2FF),
    onPrimaryContainer = Color(0xFF001A41),
    secondary = Color(0xFF535F70),
    onSecondary = Color(0xFFFFFFFF),
    background = Color(0xFFF7F9FF),
    onBackground = Color(0xFF181C20),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF181C20),
    surfaceVariant = Color(0xFFDFE2EB),
    onSurfaceVariant = Color(0xFF43474E),
    outline = Color(0xFFC4C6CF),
    error = SlantErrorRed
)

val SlantShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(26.dp),
    extraLarge = RoundedCornerShape(32.dp)
)

/**
 * Адаптивный модификатор Liquid Glass:
 * Поддерживает как глубокий OLED Dark Glass, так и кристально чистый Light Glass,
 * а также акценты системной темы Material You (Monet).
 */
fun Modifier.liquidGlass(
    shape: Shape = RoundedCornerShape(22.dp),
    backgroundColor: Color? = null,
    alpha: Float = 0.70f,
    borderWidth: Dp = 1.dp
): Modifier = this
    .clip(shape)
    .background(
        brush = Brush.verticalGradient(
            colors = run {
                val isDark = SlantAppStateManager.isDark
                val isMonet = SlantAppStateManager.isMonet

                val base = backgroundColor ?: when {
                    !isDark -> SlantLightGlassBase
                    isMonet -> Color(0xFF141A24)
                    else -> SlantGlassBase
                }

                val topAlpha = if (isDark) alpha else (alpha * 0.85f).coerceAtMost(0.95f)
                val bottomAlpha = if (isDark) (alpha + 0.15f).coerceAtMost(0.98f) else (alpha + 0.10f).coerceAtMost(0.98f)

                listOf(
                    base.copy(alpha = topAlpha),
                    base.copy(alpha = bottomAlpha)
                )
            }
        ),
        shape = shape
    )
    .border(
        width = borderWidth,
        brush = Brush.verticalGradient(
            colors = if (SlantAppStateManager.isDark) {
                if (SlantAppStateManager.isMonet) {
                    listOf(
                        Color(0xFFADC6FF).copy(alpha = 0.35f),
                        Color(0xFFADC6FF).copy(alpha = 0.08f)
                    )
                } else {
                    listOf(
                        Color.White.copy(alpha = 0.28f),
                        Color.White.copy(alpha = 0.04f)
                    )
                }
            } else {
                if (SlantAppStateManager.isMonet) {
                    listOf(
                        Color(0xFF005AC1).copy(alpha = 0.30f),
                        Color(0xFF005AC1).copy(alpha = 0.08f)
                    )
                } else {
                    listOf(
                        Color.Black.copy(alpha = 0.18f),
                        Color.Black.copy(alpha = 0.05f)
                    )
                }
            }
        ),
        shape = shape
    )

@Composable
fun SlantTheme(
    darkTheme: Boolean = SlantAppStateManager.isDark,
    palette: SlantThemePalette = SlantAppStateManager.themePalette.value,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val colorScheme = when {
        palette == SlantThemePalette.MATERIAL_YOU -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            } else {
                if (darkTheme) MonetDarkSchemeFallback else MonetLightSchemeFallback
            }
        }
        darkTheme -> MonochromeDarkScheme
        else -> MonochromeLightScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = SlantShapes,
        content = content
    )
}
