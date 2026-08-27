package com.slant.ui.screens.channel

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.OpenInNew
import androidx.compose.material.icons.rounded.Chat
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.NotificationsOff
import androidx.compose.material.icons.rounded.PushPin
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slant.ui.components.LivingNeuralCanvas
import com.slant.ui.components.NeuralState
import com.slant.ui.theme.SlantDimText
import com.slant.ui.theme.SlantGlassBase
import com.slant.ui.theme.SlantOledBlack
import com.slant.ui.theme.SlantPureWhite
import com.slant.ui.theme.liquidGlass

data class ReactionState(
    val emoji: String,
    var count: Int,
    var isSelected: Boolean = false
)

data class ChannelPost(
    val id: String,
    val tag: String,
    val title: String,
    val releaseVersion: String? = null,
    val date: String,
    val views: String,
    val changelogHighlights: List<String>,
    val commitHash: String? = null,
    val externalUrl: String? = null,
    val isPinned: Boolean = false,
    val reactions: List<ReactionState>
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SlantChannelScreen(
    channelTitle: String = "SlantTech Core Releases",
    subscriberCount: String = "24,850 узлов",
    onBackClick: () -> Unit,
    onOpenCommentsClick: (postId: String) -> Unit = {},
    onChannelInfoClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var isMuted by remember { mutableStateOf(false) }
    var showPinnedBanner by remember { mutableStateOf(true) }

    val posts = remember {
        mutableStateListOf(
            ChannelPost(
                id = "p_320",
                tag = "КРУПНЫЙ РЕЛИЗ",
                title = "SLANT v3.2.0: ГИБРИДНЫЙ ДВИЖОК LIQUID GLASS И ZERO-RAM АРХИТЕКТУРА",
                releaseVersion = "v3.2.0-release",
                date = "Сегодня в 14:20",
                views = "18.4K",
                changelogHighlights = listOf(
                    "🎨 Полная интеграция Liquid Glass + Material 3: глубокий OLED (#000000), матовое стекло, зеркальные градиентные обводки и 120 FPS Living Neural Canvas.",
                    "🛡️ Anti-Forensics Zero-RAM: зануление оперативной памяти при выходе, защита от скриншотов (FLAG_SECURE) и уничтожение ключей Double Ratchet.",
                    "⚡ Четырехуровневый транспорт: прямые BLE/Wi-Fi Mesh соединения, слепые релеи sl-me.ru:8443 и VLESS Reality SNI-мимикрия.",
                    "⚙️ 10 новых модулей управления в профиле: SlantTech Engine, Кастомизация узла, Сессии, Энергосбережение и Smart Cache.",
                    "🎙️ Передача RAW несжатых голосовых и видео-сообщений без перекодирования и метаданных."
                ),
                commitHash = "e89c3f91a27d498b8e05c2d3a771b9f692d04a8e",
                externalUrl = "https://blog.sl-me.online",
                reactions = listOf(
                    ReactionState("⚡", 1420),
                    ReactionState("🔥", 980),
                    ReactionState("🛡️", 850),
                    ReactionState("🤍", 412)
                )
            ),
            ChannelPost(
                id = "p_312",
                tag = "ПАТЧ БЕЗОПАСНОСТИ",
                title = "SLANT v3.1.2: POST-QUANTUM RATCHET & KYBER-1024",
                releaseVersion = "v3.1.2-patch",
                date = "24 авг в 19:40",
                views = "21.1K",
                changelogHighlights = listOf(
                    "🔐 Внедрен гибридный квантово-устойчивый обмен ключами Kyber-1024 + X25519.",
                    "🗃️ Локальная база SQLCipher переведена на 600,000 итераций Argon2id.",
                    "🚀 Оптимизация нагрузки на CPU: снижение энергопотребления фонового BLE Mesh-сканера на 42%."
                ),
                commitHash = "4b27a90f11c8d356ea880491cb8975ef2139b882",
                externalUrl = "https://blog.sl-me.online",
                reactions = listOf(
                    ReactionState("🛡️", 940),
                    ReactionState("⚡", 610),
                    ReactionState("🔥", 320)
                )
            ),
            ChannelPost(
                id = "p_300",
                tag = "GENESIS PROTOCOL",
                title = "SLANT GENESIS v3.0: ДЕЦЕНТРАЛИЗОВАННЫЙ СУВЕРЕННЫЙ МЕССЕНДЖЕР",
                releaseVersion = "v3.0.0-genesis",
                date = "15 авг в 12:00",
                views = "34.6K",
                changelogHighlights = listOf(
                    "🌐 Отказ от централизованных серверов аутентификации и номеров телефонов.",
                    "🔑 Идентификация исключительно по Ed25519 открытым ключам и мнемонике Seed 12-24.",
                    "📡 Автономная передача сообщений по схеве Store-and-Forward в локальных Mesh кластерах."
                ),
                commitHash = "00a1f94c3d8e920b1574fa6d81239c049811ab24",
                externalUrl = "https://blog.sl-me.online",
                reactions = listOf(
                    ReactionState("🔥", 2450),
                    ReactionState("⚡", 1890),
                    ReactionState("🤍", 980)
                )
            )
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SlantOledBlack)
            .testTag("slant_channel_screen")
    ) {
        // Фоновый Neural Canvas
        LivingNeuralCanvas(
            state = NeuralState.IDLE,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.14f)
                .align(Alignment.Center)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // Шапка канала (Liquid Glass)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .liquidGlass(
                        shape = RoundedCornerShape(24.dp),
                        backgroundColor = SlantGlassBase,
                        alpha = 0.70f,
                        borderWidth = 1.dp
                    )
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onChannelInfoClick() }
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back",
                            tint = SlantPureWhite
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFF2A2A2A), Color(0xFF111111))
                                )
                            )
                            .border(1.dp, SlantPureWhite.copy(alpha = 0.4f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "ST",
                            color = SlantPureWhite,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = channelTitle,
                                color = SlantPureWhite,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Rounded.CheckCircle,
                                contentDescription = "Verified Channel",
                                tint = SlantPureWhite,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                        Text(
                            text = "$subscriberCount • Официальный канал",
                            color = SlantDimText,
                            fontSize = 10.5.sp
                        )
                    }
                }

                IconButton(
                    onClick = {
                        isMuted = !isMuted
                        Toast.makeText(
                            context,
                            if (isMuted) "Уведомления канала отключены" else "Уведомления включены",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = if (isMuted) Icons.Rounded.NotificationsOff else Icons.Rounded.Notifications,
                        contentDescription = "Mute Channel",
                        tint = if (isMuted) SlantDimText else SlantPureWhite,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }

            // Закрепленный пост (Pinned Banner)
            AnimatedVisibility(
                visible = showPinnedBanner,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 3.dp)
                        .liquidGlass(
                            shape = RoundedCornerShape(16.dp),
                            backgroundColor = Color(0x331E1E1E),
                            alpha = 0.65f,
                            borderWidth = 0.8.dp
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.PushPin,
                            contentDescription = "Pinned",
                            tint = SlantPureWhite,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "ЗАКРЕПЛЕННЫЙ РЕЛИЗ: Slant v3.2.0 Liquid Glass",
                                color = SlantPureWhite,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Официальный хаб разработчиков: blog.sl-me.online",
                                color = SlantDimText,
                                fontSize = 9.5.sp
                            )
                        }
                    }
                    IconButton(
                        onClick = { showPinnedBanner = false },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Text("✕", color = SlantDimText, fontSize = 11.sp)
                    }
                }
            }

            // Лента постов обновлений
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item { Spacer(modifier = Modifier.height(4.dp)) }

                items(posts, key = { it.id }) { post ->
                    ChannelPostCard(
                        post = post,
                        onOpenUrl = { url ->
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        },
                        onCopyHash = { hash ->
                            clipboardManager.setText(AnnotatedString(hash))
                            Toast.makeText(context, "Хеш коммита скопирован", Toast.LENGTH_SHORT).show()
                        },
                        onDiscussInGroup = {
                            onOpenCommentsClick(post.id)
                        }
                    )
                }

                item { Spacer(modifier = Modifier.height(10.dp)) }
            }

            // Нижняя панель действий канала (Mute/Unmute & Discuss)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .liquidGlass(
                        shape = RoundedCornerShape(22.dp),
                        backgroundColor = SlantGlassBase,
                        alpha = 0.85f,
                        borderWidth = 1.dp
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        isMuted = !isMuted
                        Toast.makeText(
                            context,
                            if (isMuted) "Уведомления канала выключены" else "Уведомления включены",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isMuted) Color(0x33FFFFFF) else SlantPureWhite,
                        contentColor = if (isMuted) SlantPureWhite else SlantOledBlack
                    )
                ) {
                    Icon(
                        imageVector = if (isMuted) Icons.Rounded.NotificationsOff else Icons.Rounded.Notifications,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isMuted) "ВКЛ. УВЕДОМЛЕНИЯ" else "БЕЗ ЗВУКА",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp
                    )
                }

                Button(
                    onClick = { onOpenCommentsClick("latest") },
                    modifier = Modifier
                        .weight(1.3f)
                        .height(44.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0x33FFFFFF),
                        contentColor = SlantPureWhite
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Chat,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "ОБСУДИТЬ В КЛАСТЕРЕ",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ChannelPostCard(
    post: ChannelPost,
    onOpenUrl: (String) -> Unit,
    onCopyHash: (String) -> Unit,
    onDiscussInGroup: () -> Unit
) {
    val context = LocalContext.current
    var reactions by remember { mutableStateOf(post.reactions) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .liquidGlass(
                shape = RoundedCornerShape(26.dp),
                backgroundColor = SlantGlassBase,
                alpha = 0.70f,
                borderWidth = 1.dp
            )
            .padding(18.dp)
    ) {
        // Тег и дата
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(SlantPureWhite)
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = post.tag,
                    color = SlantOledBlack,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
            }

            Text(
                text = post.date,
                color = SlantDimText,
                fontSize = 10.5.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Заголовок
        Text(
            text = post.title,
            color = SlantPureWhite,
            fontSize = 14.5.sp,
            fontWeight = FontWeight.Black,
            lineHeight = 19.sp,
            letterSpacing = 0.5.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Список изменений (Changelog)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0x22FFFFFF))
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            post.changelogHighlights.forEach { item ->
                Text(
                    text = item,
                    color = SlantPureWhite.copy(alpha = 0.9f),
                    fontSize = 11.5.sp,
                    lineHeight = 16.sp
                )
            }
        }

        // Commit Hash & Links
        if (post.commitHash != null) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x18FFFFFF))
                    .clickable { onCopyHash(post.commitHash) }
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.Shield,
                        contentDescription = null,
                        tint = SlantPureWhite,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "SHA-256: ${post.commitHash.take(16)}...",
                        color = SlantDimText,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
                Icon(
                    imageVector = Icons.Rounded.ContentCopy,
                    contentDescription = "Copy Hash",
                    tint = SlantDimText,
                    modifier = Modifier.size(13.dp)
                )
            }
        }

        // Кнопка перехода на внешний хаб
        if (post.externalUrl != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .border(0.8.dp, SlantPureWhite.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                    .clickable { onOpenUrl(post.externalUrl) }
                    .padding(horizontal = 10.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "🌐 Документация релиза на blog.sl-me.online",
                    color = SlantPureWhite,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.OpenInNew,
                    contentDescription = null,
                    tint = SlantPureWhite,
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Интерактивные реакции
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            reactions.forEachIndexed { index, rx ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (rx.isSelected) SlantPureWhite else Color(0x22FFFFFF))
                        .clickable {
                            val updated = reactions.toMutableList()
                            val current = updated[index]
                            if (current.isSelected) {
                                updated[index] = current.copy(count = current.count - 1, isSelected = false)
                            } else {
                                updated[index] = current.copy(count = current.count + 1, isSelected = true)
                            }
                            reactions = updated
                        }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "${rx.emoji} ${rx.count}",
                        color = if (rx.isSelected) SlantOledBlack else SlantPureWhite,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Футер поста: Просмотры, Репост и Переход к обсуждению
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "👁️ ${post.views} просмотров",
                color = SlantDimText,
                fontSize = 10.5.sp
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onDiscussInGroup,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Chat,
                        contentDescription = "Discuss",
                        tint = SlantPureWhite,
                        modifier = Modifier.size(17.dp)
                    )
                }
                IconButton(
                    onClick = {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, "${post.title}\nПодробнее: ${post.externalUrl}")
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Поделиться пакетом"))
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Share,
                        contentDescription = "Share",
                        tint = SlantPureWhite,
                        modifier = Modifier.size(17.dp)
                    )
                }
            }
        }
    }
}
