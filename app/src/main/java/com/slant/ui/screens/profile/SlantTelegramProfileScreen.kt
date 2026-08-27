package com.slant.ui.screens.profile

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.HelpOutline
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.BatteryChargingFull
import androidx.compose.material.icons.rounded.ChatBubbleOutline
import androidx.compose.material.icons.rounded.Devices
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material.icons.rounded.Hub
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PieChart
import androidx.compose.material.icons.rounded.Policy
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material.icons.rounded.VpnKey
import androidx.compose.material.icons.rounded.WifiTethering
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.slant.ui.models.ProfileMenuItem
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
fun SlantTelegramProfileScreen(
    displayName: String = "dmiTry",
    username: String = "shoker0215",
    cryptoId: String = "slant_ed25519_99a8b7c6...",
    onBackClick: () -> Unit = {},
    onNavigateToSlantTech: () -> Unit = {},
    onNavigateToCustomProfile: () -> Unit = {},
    onNavigateToAppearance: () -> Unit = {},
    onNavigateToNodes: () -> Unit = {},
    onNavigateToAccount: () -> Unit = {},
    onNavigateToChatSettings: () -> Unit = {},
    onNavigateToSecurity: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToStorage: () -> Unit = {},
    onNavigateToFolders: () -> Unit = {},
    onNavigateToDevices: () -> Unit = {},
    onNavigateToPowerSaving: () -> Unit = {},
    onNavigateToLanguage: () -> Unit = {},
    onNavigateToFaq: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isDark = SlantAppStateManager.isDark
    val currentLang = SlantAppStateManager.language.value
    val currentThemeMode = SlantAppStateManager.themeMode.value
    val currentPalette = SlantAppStateManager.themePalette.value

    val textColor = if (isDark) SlantPureWhite else MaterialTheme.colorScheme.onBackground
    val dimColor = if (isDark) SlantDimText else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)

    val currentThemeDisplay = "${if (currentThemeMode == SlantThemeMode.DARK) SlantStrings.themeNight else SlantStrings.themeDay} • ${if (currentPalette == SlantThemePalette.MONOCHROME) SlantStrings.paletteMono else SlantStrings.paletteMonet}"

    val mainSettings = listOf(
        ProfileMenuItem("slanttech", SlantStrings.menuSlantTech, "Параметры протокола VLESS/XTLS", null, Icons.Rounded.Tune) { onNavigateToSlantTech() },
        ProfileMenuItem("appearance", SlantStrings.menuAppearance, currentThemeDisplay, null, Icons.Rounded.Palette) { onNavigateToAppearance() },
        ProfileMenuItem("custom_profile", SlantStrings.menuCustomProfile, "Liquid Glass кастомизация", null, Icons.Rounded.Person) { onNavigateToCustomProfile() },
        ProfileMenuItem("nodes", SlantStrings.menuNodes, "sl-me.ru • sl-me.online", null, Icons.Rounded.WifiTethering) { onNavigateToNodes() },
        ProfileMenuItem("account", SlantStrings.menuAccount, "Криптографические ключи", null, Icons.Rounded.VpnKey) { onNavigateToAccount() },
        ProfileMenuItem("chat_settings", SlantStrings.menuChatSettings, null, null, Icons.Rounded.ChatBubbleOutline) { onNavigateToChatSettings() },
        ProfileMenuItem("privacy", SlantStrings.menuPrivacy, "Anti-Forensics & E2EE", null, Icons.Rounded.Security) { onNavigateToSecurity() },
        ProfileMenuItem("folders", SlantStrings.menuFolders, null, null, Icons.Rounded.Folder) { onNavigateToFolders() },
        ProfileMenuItem("devices", SlantStrings.menuDevices, null, "3", Icons.Rounded.Devices) { onNavigateToDevices() },
        ProfileMenuItem("power", SlantStrings.menuPower, null, null, Icons.Rounded.BatteryChargingFull) { onNavigateToPowerSaving() },
        ProfileMenuItem("language", SlantStrings.menuLanguage, null, currentLang.nativeName, Icons.Rounded.Language) { onNavigateToLanguage() }
    )

    val supportSettings = listOf(
        ProfileMenuItem("ask", SlantStrings.menuSupport, null, null, Icons.AutoMirrored.Rounded.HelpOutline) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://blog.sl-me.online"))
            context.startActivity(intent)
        },
        ProfileMenuItem("faq", SlantStrings.menuFaq, null, null, Icons.Rounded.Hub) { onNavigateToFaq() },
        ProfileMenuItem("privacy_policy", SlantStrings.menuPrivacyPolicy, null, null, Icons.Rounded.Policy) { onNavigateToFaq() }
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(if (isDark) SlantOledBlack else MaterialTheme.colorScheme.background)
    ) {
        // Фоновый живой маскот
        LivingNeuralCanvas(
            state = NeuralState.IDLE,
            modifier = Modifier
                .fillMaxSize()
                .alpha(if (isDark) 0.12f else 0.05f)
                .align(Alignment.Center)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Верхний бар с кнопкой назад и заголовком
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 6.dp)
                    .liquidGlass(
                        shape = RoundedCornerShape(22.dp),
                        alpha = 0.65f,
                        borderWidth = 1.dp
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = SlantStrings.back,
                            tint = textColor
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = SlantStrings.profileTitle,
                        color = textColor,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Быстрый переключатель языка прямо в профиле
                    Text(
                        text = if (currentLang == SlantLanguage.RU) "RU" else "EN",
                        color = textColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isDark) Color(0x33FFFFFF) else MaterialTheme.colorScheme.primaryContainer)
                            .clickable { SlantAppStateManager.toggleLanguage() }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .testTag("profile_quick_lang_toggle")
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    // Быстрый переключатель темы (День/Ночь) прямо в профиле
                    Text(
                        text = if (isDark) "🌙" else "☀️",
                        fontSize = 13.sp,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isDark) Color(0x33FFFFFF) else MaterialTheme.colorScheme.primaryContainer)
                            .clickable { SlantAppStateManager.toggleThemeMode() }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .testTag("profile_quick_theme_toggle")
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Шапка профиля
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .liquidGlass(
                                shape = RoundedCornerShape(26.dp),
                                alpha = 0.70f,
                                borderWidth = 1.dp
                            )
                            .padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier.size(76.dp),
                            contentAlignment = Alignment.BottomEnd
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .background(if (isDark) Color(0x22FFFFFF) else MaterialTheme.colorScheme.primaryContainer)
                                    .border(1.dp, if (isDark) SlantPureWhite.copy(alpha = 0.3f) else MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = displayName.take(2).uppercase(),
                                    color = textColor,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                            // Точка онлайн-статуса
                            Box(
                                modifier = Modifier
                                    .size(15.dp)
                                    .clip(CircleShape)
                                    .background(if (isDark) SlantOledBlack else SlantPureWhite)
                                    .padding(2.dp)
                                    .clip(CircleShape)
                                    .background(if (isDark) SlantPureWhite else MaterialTheme.colorScheme.primary)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = displayName,
                            color = textColor,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = "@$username • $cryptoId",
                            color = dimColor,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Секция основных настроек
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .liquidGlass(
                                shape = RoundedCornerShape(22.dp),
                                alpha = 0.60f,
                                borderWidth = 1.dp
                            )
                            .padding(vertical = 4.dp)
                    ) {
                        mainSettings.forEachIndexed { index, item ->
                            ProfileMenuRow(item = item, textColor = textColor, dimColor = dimColor)
                            if (index < mainSettings.size - 1) {
                                HorizontalDivider(
                                    color = if (isDark) Color(0x1AFFFFFF) else Color(0x1A000000),
                                    thickness = 0.5.dp,
                                    modifier = Modifier.padding(start = 54.dp)
                                )
                            }
                        }
                    }
                }

                // Секция помощи
                item {
                    Text(
                        text = if (currentLang == SlantLanguage.RU) "ПОМОЩЬ И ПРОТОКОЛ" else "HELP & PROTOCOL",
                        color = dimColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        modifier = Modifier.padding(start = 6.dp, top = 2.dp)
                    )
                }

                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .liquidGlass(
                                shape = RoundedCornerShape(22.dp),
                                alpha = 0.60f,
                                borderWidth = 1.dp
                            )
                            .padding(vertical = 4.dp)
                    ) {
                        supportSettings.forEachIndexed { index, item ->
                            ProfileMenuRow(item = item, textColor = textColor, dimColor = dimColor)
                            if (index < supportSettings.size - 1) {
                                HorizontalDivider(
                                    color = if (isDark) Color(0x1AFFFFFF) else Color(0x1A000000),
                                    thickness = 0.5.dp,
                                    modifier = Modifier.padding(start = 54.dp)
                                )
                            }
                        }
                    }
                }

                // Футер с версией протокола и ссылкой на хаб
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "SlantTech 20260329",
                            color = dimColor.copy(alpha = 0.6f),
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "blog.sl-me.online",
                            color = textColor.copy(alpha = 0.6f),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 0.5.sp,
                            modifier = Modifier.clickable {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://blog.sl-me.online"))
                                context.startActivity(intent)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileMenuRow(
    item: ProfileMenuItem,
    textColor: Color,
    dimColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { item.onClick() }
            .padding(horizontal = 14.dp, vertical = 10.dp)
            .testTag("profile_item_${item.id}"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                tint = textColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = item.title,
                    color = textColor,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Medium
                )
                if (item.subtitle != null) {
                    Text(
                        text = item.subtitle,
                        color = dimColor,
                        fontSize = 10.5.sp
                    )
                }
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (item.badge != null) {
                Text(
                    text = item.badge,
                    color = dimColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(end = 4.dp)
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = null,
                tint = dimColor.copy(alpha = 0.5f),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
