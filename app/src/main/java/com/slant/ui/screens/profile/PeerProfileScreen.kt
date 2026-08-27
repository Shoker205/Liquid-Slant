package com.slant.ui.screens.profile

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Block
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.QrCode
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slant.ui.components.LivingNeuralCanvas
import com.slant.ui.components.NeuralState
import com.slant.ui.theme.SlantDimText
import com.slant.ui.theme.SlantGlassBase
import com.slant.ui.theme.SlantOledBlack
import com.slant.ui.theme.SlantPureWhite
import com.slant.ui.theme.liquidGlass

@Composable
fun PeerProfileScreen(
    peerName: String,
    username: String,
    publicKey: String,
    bio: String = "Шифрование узла активно. Доступен в локальном Mesh-сегменте.",
    onBackClick: () -> Unit,
    onAudioCallClick: () -> Unit,
    onVideoCallClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isMuted by remember { mutableStateOf(false) }
    var selfDestructTimer by remember { mutableStateOf("Отключен") }
    var selectedMediaTab by remember { mutableIntStateOf(0) }
    val mediaTabs = listOf("Медиа", "Файлы", "Голосовые", "Ссылки")

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SlantOledBlack)
            .testTag("peer_profile_screen")
    ) {
        LivingNeuralCanvas(
            state = NeuralState.IDLE,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.18f)
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
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.testTag("peer_profile_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back",
                        tint = SlantPureWhite
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Карточка профиля
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .liquidGlass(
                                shape = RoundedCornerShape(32.dp),
                                backgroundColor = SlantGlassBase,
                                alpha = 0.75f,
                                borderWidth = 1.dp
                            )
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(84.dp)
                                .clip(CircleShape)
                                .background(Color(0x33FFFFFF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = peerName.take(2).uppercase(),
                                color = SlantPureWhite,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Black
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = peerName,
                            color = SlantPureWhite,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "@$username",
                            color = SlantDimText,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = bio,
                            color = SlantPureWhite.copy(alpha = 0.8f),
                            fontSize = 12.5.sp,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        // Кнопки быстрых действий (Call / Video)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            PeerActionButton(
                                icon = Icons.Rounded.Call,
                                title = "Аудио",
                                testTag = "peer_audio_call_button",
                                onClick = onAudioCallClick
                            )
                            PeerActionButton(
                                icon = Icons.Rounded.Videocam,
                                title = "Видео",
                                testTag = "peer_video_call_button",
                                onClick = onVideoCallClick
                            )
                        }
                    }
                }

                // Карточка открытого ключа
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .liquidGlass(RoundedCornerShape(22.dp), backgroundColor = SlantGlassBase, alpha = 0.65f)
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Rounded.Lock, null, tint = SlantPureWhite, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("ПУБЛИЧНЫЙ КЛЮЧ УЗЛА", color = SlantDimText, fontSize = 9.5.sp, fontWeight = FontWeight.Bold)
                                Text(
                                    text = publicKey.take(24) + "...",
                                    color = SlantPureWhite,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                        IconButton(onClick = {}) {
                            Icon(Icons.Rounded.QrCode, "QR", tint = SlantPureWhite)
                        }
                    }
                }

                // Параметры приватности диалога
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .liquidGlass(RoundedCornerShape(24.dp), backgroundColor = SlantGlassBase, alpha = 0.65f)
                            .padding(vertical = 6.dp)
                    ) {
                        GlassSettingTile(
                            title = "Уведомления",
                            subtitle = if (isMuted) "Отключены" else "Включены",
                            icon = Icons.Rounded.Notifications,
                            isChecked = !isMuted,
                            onCheckedChange = { isMuted = !it }
                        )
                        GlassSettingTile(
                            title = "Автоудаление сообщений",
                            subtitle = selfDestructTimer,
                            icon = Icons.Rounded.Schedule,
                            onClick = {
                                selfDestructTimer = if (selfDestructTimer == "Отключен") "24 часа" else "Отключен"
                            }
                        )
                        GlassSettingTile(
                            title = "Заблокировать контакт",
                            subtitle = "Сброс сессионных Double Ratchet ключей",
                            icon = Icons.Rounded.Block,
                            isDanger = true,
                            onClick = {}
                        )
                    }
                }

                // Общие вложения
                item {
                    Text(
                        text = "ОБЩИЕ МАТЕРИАЛЫ",
                        color = SlantDimText,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        modifier = Modifier.padding(start = 6.dp, top = 6.dp)
                    )
                }

                item {
                    ScrollableTabRow(
                        selectedTabIndex = selectedMediaTab,
                        containerColor = Color.Transparent,
                        contentColor = SlantPureWhite,
                        edgePadding = 0.dp,
                        divider = {},
                        indicator = {}
                    ) {
                        mediaTabs.forEachIndexed { index, title ->
                            val isSelected = selectedMediaTab == index
                            Tab(
                                selected = isSelected,
                                onClick = { selectedMediaTab = index },
                                modifier = Modifier
                                    .padding(vertical = 4.dp, horizontal = 4.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .then(
                                        if (isSelected) Modifier.liquidGlass(RoundedCornerShape(14.dp), backgroundColor = Color(0x66FFFFFF), alpha = 0.25f)
                                        else Modifier
                                    )
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = title.uppercase(),
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) SlantPureWhite else SlantDimText
                                )
                            }
                        }
                    }
                }

                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .liquidGlass(RoundedCornerShape(20.dp), backgroundColor = SlantGlassBase, alpha = 0.45f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Нет общих файлов в данном хранилище", color = SlantDimText, fontSize = 12.sp)
                    }
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }
            }
        }
    }
}

@Composable
private fun PeerActionButton(
    icon: ImageVector,
    title: String,
    testTag: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0x22FFFFFF)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = SlantPureWhite, modifier = Modifier.size(22.dp))
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(title, color = SlantDimText, fontSize = 11.sp, fontWeight = FontWeight.Medium)
    }
}
