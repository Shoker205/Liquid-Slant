package com.slant.ui.screens.settings

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.FolderZip
import androidx.compose.material.icons.rounded.Hub
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material.icons.rounded.WifiTethering
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slant.ui.components.LivingNeuralCanvas
import com.slant.ui.components.NeuralState
import com.slant.ui.screens.profile.GlassSettingTile
import com.slant.ui.theme.SlantAppStateManager
import com.slant.ui.theme.SlantDimText
import com.slant.ui.theme.SlantLanguage
import com.slant.ui.theme.SlantOledBlack
import com.slant.ui.theme.SlantPureWhite
import com.slant.ui.theme.SlantStrings
import com.slant.ui.theme.SlantThemeMode
import com.slant.ui.theme.SlantThemePalette
import com.slant.ui.theme.liquidGlass

@Composable
fun SettingsHubScreen(
    onBackClick: () -> Unit,
    onNavigateToSecurity: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var meshEnabled by remember { mutableStateOf(true) }
    var dohRuDomain by remember { mutableStateOf(true) }
    var particleDensity by remember { mutableFloatStateOf(0.7f) }

    val isDark = SlantAppStateManager.isDark
    val currentLang = SlantAppStateManager.language.value
    val currentMode = SlantAppStateManager.themeMode.value
    val currentPalette = SlantAppStateManager.themePalette.value

    val textColor = if (isDark) SlantPureWhite else MaterialTheme.colorScheme.onBackground
    val dimColor = if (isDark) SlantDimText else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(if (isDark) SlantOledBlack else MaterialTheme.colorScheme.background)
            .testTag("settings_hub_screen")
    ) {
        LivingNeuralCanvas(
            state = NeuralState.IDLE,
            modifier = Modifier
                .fillMaxSize()
                .alpha(if (isDark) 0.15f else 0.06f)
                .align(Alignment.Center)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // Верхняя планка
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .liquidGlass(
                        shape = RoundedCornerShape(24.dp),
                        alpha = 0.65f,
                        borderWidth = 1.dp
                    )
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.testTag("settings_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = SlantStrings.back,
                        tint = textColor
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = SlantStrings.settings.uppercase(),
                    color = textColor,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Раздел: Тема и Язык
                item {
                    SectionHeader(SlantStrings.appearanceTitle, dimColor)
                }

                item {
                    GlassSettingTile(
                        title = if (currentMode == SlantThemeMode.DARK) SlantStrings.themeNightTitle else SlantStrings.themeDayTitle,
                        subtitle = "${SlantStrings.themeLabel}: ${if (currentMode == SlantThemeMode.DARK) SlantStrings.themeNight else SlantStrings.themeDay}",
                        icon = Icons.Rounded.Palette,
                        onClick = { SlantAppStateManager.toggleThemeMode() }
                    )
                }

                item {
                    GlassSettingTile(
                        title = if (currentPalette == SlantThemePalette.MONOCHROME) SlantStrings.paletteMonoTitle else SlantStrings.paletteMonetTitle,
                        subtitle = "${SlantStrings.paletteLabel}: ${if (currentPalette == SlantThemePalette.MONOCHROME) SlantStrings.paletteMono else SlantStrings.paletteMonet}",
                        icon = Icons.Rounded.Palette,
                        onClick = { SlantAppStateManager.toggleThemePalette() }
                    )
                }

                item {
                    GlassSettingTile(
                        title = SlantStrings.languageTitle,
                        subtitle = "${SlantStrings.languageLabel}: ${currentLang.nativeName}",
                        icon = Icons.Rounded.Language,
                        onClick = { SlantAppStateManager.toggleLanguage() }
                    )
                }

                // Раздел: Сеть и «Белые списки»
                item {
                    SectionHeader(if (currentLang == SlantLanguage.RU) "СЕТЬ И УСТОЙЧИВОСТЬ К БЛОКИРОВКАМ" else "NETWORK & ANTI-CENSORSHIP", dimColor)
                }

                item {
                    GlassSettingTile(
                        title = if (currentLang == SlantLanguage.RU) "Автономная Mesh-сеть" else "Autonomous Mesh Network",
                        subtitle = if (currentLang == SlantLanguage.RU) "BLE & Wi-Fi Direct без интернета" else "BLE & Wi-Fi Direct without internet",
                        icon = Icons.Rounded.Hub,
                        isChecked = meshEnabled,
                        onCheckedChange = { meshEnabled = it }
                    )
                }

                item {
                    GlassSettingTile(
                        title = if (currentLang == SlantLanguage.RU) "RU-Шлюз sl-me.ru" else "RU Relay sl-me.ru",
                        subtitle = if (currentLang == SlantLanguage.RU) "Маскировка трафика под доверенный сегмент" else "Masking traffic as trusted segment",
                        icon = Icons.Rounded.WifiTethering,
                        isChecked = dohRuDomain,
                        onCheckedChange = { dohRuDomain = it }
                    )
                }

                // Раздел: Безопасность и хранилище
                item {
                    SectionHeader(if (currentLang == SlantLanguage.RU) "БЕЗОПАСНОСТЬ И ХРАНИЛИЩЕ" else "SECURITY & STORAGE", dimColor)
                }

                item {
                    GlassSettingTile(
                        title = "Anti-Forensics & Smart Cache",
                        subtitle = if (currentLang == SlantLanguage.RU) "Защита ОЗУ, SQLCipher базы и Panic PIN" else "RAM protection, SQLCipher & Panic PIN",
                        icon = Icons.Rounded.Security,
                        onClick = onNavigateToSecurity
                    )
                }

                item {
                    GlassSettingTile(
                        title = if (currentLang == SlantLanguage.RU) "Экспорт сессионных ключей" else "Export Session Keys",
                        subtitle = if (currentLang == SlantLanguage.RU) "Резервная зашифрованная копия ключей узла" else "Encrypted backup of node keys",
                        icon = Icons.Rounded.FolderZip,
                        onClick = {}
                    )
                }

                // Раздел: Графический движок
                item {
                    SectionHeader(if (currentLang == SlantLanguage.RU) "ГРАФИЧЕСКИЙ ДВИЖОК NEURAL FLUID" else "NEURAL FLUID GRAPHICS ENGINE", dimColor)
                }

                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .liquidGlass(RoundedCornerShape(20.dp), alpha = 0.55f)
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (currentLang == SlantLanguage.RU) "Плотность частиц на фоне" else "Background particle density",
                                color = textColor,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text("${(particleDensity * 100).toInt()}%", color = dimColor, fontSize = 12.sp)
                        }
                        Slider(
                            value = particleDensity,
                            onValueChange = { particleDensity = it },
                            colors = SliderDefaults.colors(
                                thumbColor = if (isDark) SlantPureWhite else MaterialTheme.colorScheme.primary,
                                activeTrackColor = if (isDark) SlantPureWhite else MaterialTheme.colorScheme.primary,
                                inactiveTrackColor = if (isDark) Color(0x33FFFFFF) else Color(0x22000000)
                            ),
                            modifier = Modifier.testTag("particle_density_slider")
                        )
                    }
                }

                // Раздел: О проекте
                item {
                    SectionHeader(if (currentLang == SlantLanguage.RU) "О СИСТЕМЕ SLANT" else "ABOUT SLANT SYSTEM", dimColor)
                }

                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .liquidGlass(RoundedCornerShape(24.dp), alpha = 0.65f)
                            .padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "SLANT PROTOCOL V2.0",
                            color = textColor,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 2.sp
                        )
                        Text(
                            text = if (currentLang == SlantLanguage.RU) "Архитектура нулевого доверия (Zero-Knowledge)" else "Zero-Knowledge Architecture",
                            color = dimColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isDark) Color(0x22FFFFFF) else MaterialTheme.colorScheme.primaryContainer)
                                .clickable {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://blog.sl-me.online"))
                                    context.startActivity(intent)
                                }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                                .testTag("slant_tech_hub_link")
                        ) {
                            Text(
                                text = "SlantTech Hub: blog.sl-me.online",
                                color = textColor,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(32.dp)) }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String, color: Color) {
    Text(
        text = title,
        color = color,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.5.sp,
        modifier = Modifier.padding(start = 6.dp, top = 8.dp)
    )
}
