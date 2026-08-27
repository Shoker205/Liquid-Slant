package com.slant.ui.screens.settings

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.ColorLens
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.FormatPaint
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slant.ui.components.LivingNeuralCanvas
import com.slant.ui.components.NeuralState
import com.slant.ui.theme.SlantAppStateManager
import com.slant.ui.theme.SlantDimText
import com.slant.ui.theme.SlantOledBlack
import com.slant.ui.theme.SlantPureWhite
import com.slant.ui.theme.SlantStrings
import com.slant.ui.theme.SlantThemeMode
import com.slant.ui.theme.SlantThemePalette
import com.slant.ui.theme.liquidGlass

@Composable
fun AppearanceSettingsScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isDark = SlantAppStateManager.isDark
    val currentMode = SlantAppStateManager.themeMode.value
    val currentPalette = SlantAppStateManager.themePalette.value

    val textColor = if (isDark) SlantPureWhite else MaterialTheme.colorScheme.onBackground
    val dimColor = if (isDark) SlantDimText else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(if (isDark) SlantOledBlack else MaterialTheme.colorScheme.background)
    ) {
        LivingNeuralCanvas(
            state = NeuralState.IDLE,
            modifier = Modifier.fillMaxSize().alpha(if (isDark) 0.12f else 0.05f).align(Alignment.Center)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // Заголовок TopBar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .liquidGlass(
                        shape = RoundedCornerShape(24.dp),
                        alpha = 0.65f
                    )
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = SlantStrings.back,
                        tint = textColor
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = SlantStrings.appearanceTitle,
                    color = textColor,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Секция режима темы (День / Ночь)
                item {
                    Text(
                        text = SlantStrings.themeModeSection,
                        color = dimColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(start = 6.dp, top = 6.dp)
                    )
                }

                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .liquidGlass(shape = RoundedCornerShape(22.dp), alpha = 0.60f)
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Ночная (Тёмная)
                        ThemeSelectionRow(
                            title = SlantStrings.themeNightTitle,
                            subtitle = SlantStrings.themeNightDesc,
                            icon = Icons.Rounded.DarkMode,
                            isSelected = currentMode == SlantThemeMode.DARK,
                            textColor = textColor,
                            dimColor = dimColor,
                            onClick = {
                                SlantAppStateManager.setThemeMode(SlantThemeMode.DARK)
                            }
                        )

                        // Дневная (Светлая)
                        ThemeSelectionRow(
                            title = SlantStrings.themeDayTitle,
                            subtitle = SlantStrings.themeDayDesc,
                            icon = Icons.Rounded.LightMode,
                            isSelected = currentMode == SlantThemeMode.LIGHT,
                            textColor = textColor,
                            dimColor = dimColor,
                            onClick = {
                                SlantAppStateManager.setThemeMode(SlantThemeMode.LIGHT)
                            }
                        )
                    }
                }

                // Секция палитры (Стандарт Монохром ч/б vs Material You Monet)
                item {
                    Text(
                        text = SlantStrings.themePaletteSection,
                        color = dimColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(start = 6.dp, top = 8.dp)
                    )
                }

                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .liquidGlass(shape = RoundedCornerShape(22.dp), alpha = 0.60f)
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Стандарт (Монохром)
                        ThemeSelectionRow(
                            title = SlantStrings.paletteMonoTitle,
                            subtitle = SlantStrings.paletteMonoDesc,
                            icon = Icons.Rounded.FormatPaint,
                            isSelected = currentPalette == SlantThemePalette.MONOCHROME,
                            textColor = textColor,
                            dimColor = dimColor,
                            onClick = {
                                SlantAppStateManager.setThemePalette(SlantThemePalette.MONOCHROME)
                            }
                        )

                        // Системная (Material You)
                        ThemeSelectionRow(
                            title = SlantStrings.paletteMonetTitle,
                            subtitle = SlantStrings.paletteMonetDesc,
                            icon = Icons.Rounded.ColorLens,
                            isSelected = currentPalette == SlantThemePalette.MATERIAL_YOU,
                            textColor = textColor,
                            dimColor = dimColor,
                            onClick = {
                                SlantAppStateManager.setThemePalette(SlantThemePalette.MATERIAL_YOU)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ThemeSelectionRow(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    textColor: Color,
    dimColor: Color,
    onClick: () -> Unit
) {
    val isDark = SlantAppStateManager.isDark

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .liquidGlass(
                shape = RoundedCornerShape(16.dp),
                backgroundColor = if (isSelected) {
                    if (isDark) Color(0x33FFFFFF) else MaterialTheme.colorScheme.primaryContainer
                } else null,
                alpha = 0.50f
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) textColor else dimColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    color = textColor,
                    fontSize = 13.5.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
                Text(
                    text = subtitle,
                    color = dimColor,
                    fontSize = 10.5.sp
                )
            }
        }

        if (isSelected) {
            Icon(
                imageVector = Icons.Rounded.Check,
                contentDescription = "Selected",
                tint = if (isDark) SlantPureWhite else MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
