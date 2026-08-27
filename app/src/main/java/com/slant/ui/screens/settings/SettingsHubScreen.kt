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
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material.icons.rounded.WifiTethering
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.slant.ui.theme.SlantDimText
import com.slant.ui.theme.SlantGlassBase
import com.slant.ui.theme.SlantOledBlack
import com.slant.ui.theme.SlantPureWhite
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

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SlantOledBlack)
            .testTag("settings_hub_screen")
    ) {
        LivingNeuralCanvas(
            state = NeuralState.IDLE,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.15f)
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
                        backgroundColor = SlantGlassBase,
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
                        contentDescription = "Back",
                        tint = SlantPureWhite
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "ПАРАМЕТРЫ СИСТЕМЫ",
                    color = SlantPureWhite,
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
                // Раздел: Сеть и «Белые списки»
                item {
                    SectionHeader("СЕТЬ И УСТОЙЧИВОСТЬ К БЛОКИРОВКАМ")
                }

                item {
                    GlassSettingTile(
                        title = "Автономная Mesh-сеть",
                        subtitle = "Bluetooth Low Energy & Wi-Fi Direct без интернета",
                        icon = Icons.Rounded.Hub,
                        isChecked = meshEnabled,
                        onCheckedChange = { meshEnabled = it }
                    )
                }

                item {
                    GlassSettingTile(
                        title = "RU-Шлюз sl-me.ru",
                        subtitle = "Маскировка сигнального трафика под доверенный сегмент",
                        icon = Icons.Rounded.WifiTethering,
                        isChecked = dohRuDomain,
                        onCheckedChange = { dohRuDomain = it }
                    )
                }

                // Раздел: Безопасность и хранилище
                item {
                    SectionHeader("БЕЗОПАСНОСТЬ И ХРАНИЛИЩЕ")
                }

                item {
                    GlassSettingTile(
                        title = "Anti-Forensics & Smart Cache",
                        subtitle = "Защита ОЗУ, SQLCipher базы и Panic PIN",
                        icon = Icons.Rounded.Security,
                        onClick = onNavigateToSecurity
                    )
                }

                item {
                    GlassSettingTile(
                        title = "Экспорт сессионных ключей",
                        subtitle = "Резервная зашифрованная копия ключей узла",
                        icon = Icons.Rounded.FolderZip,
                        onClick = {}
                    )
                }

                // Раздел: Графический движок
                item {
                    SectionHeader("ГРАФИЧЕСКИЙ ДВИЖОК NEURAL FLUID")
                }

                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .liquidGlass(RoundedCornerShape(20.dp), backgroundColor = SlantGlassBase, alpha = 0.55f)
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Плотность частиц на фоне", color = SlantPureWhite, fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold)
                            Text("${(particleDensity * 100).toInt()}%", color = SlantDimText, fontSize = 12.sp)
                        }
                        Slider(
                            value = particleDensity,
                            onValueChange = { particleDensity = it },
                            colors = SliderDefaults.colors(
                                thumbColor = SlantPureWhite,
                                activeTrackColor = SlantPureWhite,
                                inactiveTrackColor = Color(0x33FFFFFF)
                            ),
                            modifier = Modifier.testTag("particle_density_slider")
                        )
                    }
                }

                // Раздел: О проекте
                item {
                    SectionHeader("О СИСТЕМЕ SLANT")
                }

                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .liquidGlass(RoundedCornerShape(24.dp), backgroundColor = SlantGlassBase, alpha = 0.65f)
                            .padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "SLANT PROTOCOL V2.0",
                            color = SlantPureWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 2.sp
                        )
                        Text(
                            text = "Архитектура нулевого доверия (Zero-Knowledge)",
                            color = SlantDimText,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x22FFFFFF))
                                .clickable {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://blog.sl-me.online"))
                                    context.startActivity(intent)
                                }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                                .testTag("slant_tech_hub_link")
                        ) {
                            Text(
                                text = "SlantTech Hub: blog.sl-me.online",
                                color = SlantPureWhite,
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
private fun SectionHeader(title: String) {
    Text(
        text = title,
        color = SlantDimText,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.5.sp,
        modifier = Modifier.padding(start = 6.dp, top = 8.dp)
    )
}
