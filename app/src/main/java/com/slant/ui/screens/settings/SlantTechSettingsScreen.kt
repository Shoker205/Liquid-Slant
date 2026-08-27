package com.slant.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Cable
import androidx.compose.material.icons.rounded.Memory
import androidx.compose.material.icons.rounded.NetworkPing
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slant.ui.components.LivingNeuralCanvas
import com.slant.ui.components.NeuralState
import com.slant.ui.screens.profile.GlassSettingTile
import com.slant.ui.theme.SlantDimText
import com.slant.ui.theme.SlantGlassBase
import com.slant.ui.theme.SlantOledBlack
import com.slant.ui.theme.SlantPureWhite
import com.slant.ui.theme.liquidGlass

@Composable
fun SlantTechSettingsScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var vlessDirectBypass by remember { mutableStateOf(true) }
    var xtlsRealitySni by remember { mutableStateOf(true) }
    var zeroRamLogPurge by remember { mutableStateOf(true) }
    var rawByteStrictVerification by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SlantOledBlack)
    ) {
        LivingNeuralCanvas(
            state = NeuralState.IDLE,
            modifier = Modifier.fillMaxSize().alpha(0.12f).align(Alignment.Center)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .liquidGlass(RoundedCornerShape(24.dp), backgroundColor = SlantGlassBase, alpha = 0.65f)
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Back", tint = SlantPureWhite)
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "НАСТРОЙКИ SLANTTECH",
                    color = SlantPureWhite,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Text(
                        text = "НИЗКОУРОВНЕВЫЕ ТРАНСПОРТЫ И DPI ОБХОД",
                        color = SlantDimText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        modifier = Modifier.padding(start = 6.dp, top = 6.dp)
                    )
                }

                item {
                    GlassSettingTile(
                        title = "VLESS + XTLS Reality SNI",
                        subtitle = "Мимикрия TLS-рукопожатий под доверенные RU-сервисы",
                        icon = Icons.Rounded.Cable,
                        isChecked = xtlsRealitySni,
                        onCheckedChange = { xtlsRealitySni = it }
                    )
                }

                item {
                    GlassSettingTile(
                        title = "Прямой TCP/UDP Bypass",
                        subtitle = "Автоматический выбор fallback-сокета при фильтрации",
                        icon = Icons.Rounded.NetworkPing,
                        isChecked = vlessDirectBypass,
                        onCheckedChange = { vlessDirectBypass = it }
                    )
                }

                item {
                    Text(
                        text = "ОТЛАДКА И БЕЗОПАСНОСТЬ ЯДРА",
                        color = SlantDimText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        modifier = Modifier.padding(start = 6.dp, top = 6.dp)
                    )
                }

                item {
                    GlassSettingTile(
                        title = "Zero-RAM Log Purge",
                        subtitle = "Мгновенная очистка системных логов из буфера ОЗУ",
                        icon = Icons.Rounded.Memory,
                        isChecked = zeroRamLogPurge,
                        onCheckedChange = { zeroRamLogPurge = it }
                    )
                }

                item {
                    GlassSettingTile(
                        title = "Строгая проверка RAW-хеша",
                        subtitle = "Контроль целостности SHA-256 перед открытием вложений",
                        icon = Icons.Rounded.Shield,
                        isChecked = rawByteStrictVerification,
                        onCheckedChange = { rawByteStrictVerification = it }
                    )
                }
            }
        }
    }
}
