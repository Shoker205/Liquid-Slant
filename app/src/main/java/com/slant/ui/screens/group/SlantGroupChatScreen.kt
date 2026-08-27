package com.slant.ui.screens.group

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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.FileDownload
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.PushPin
import androidx.compose.material.icons.rounded.Reply
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slant.ui.components.LivingNeuralCanvas
import com.slant.ui.components.NeuralState
import com.slant.ui.screens.chat.AttachmentBottomSheet
import com.slant.ui.screens.chat.ChatInputBar
import com.slant.ui.screens.chat.FileAttachment
import com.slant.ui.screens.chat.MessageType
import com.slant.ui.screens.chat.SlantMessage
import com.slant.ui.screens.chat.VoiceAttachment
import com.slant.ui.screens.main.DeliveryStatus
import com.slant.ui.theme.SlantDimText
import com.slant.ui.theme.SlantGlassBase
import com.slant.ui.theme.SlantOledBlack
import com.slant.ui.theme.SlantPureWhite
import com.slant.ui.theme.liquidGlass

data class GroupPollOption(
    val id: String,
    val text: String,
    var votes: Int
)

data class GroupPoll(
    val question: String,
    val totalVotes: Int,
    val options: List<GroupPollOption>,
    var selectedOptionId: String? = null
)

data class GroupChatMessage(
    val id: String,
    val senderName: String,
    val senderRole: String? = null,
    val senderColor: Color = SlantPureWhite,
    val text: String? = null,
    val timestamp: String,
    val isOutgoing: Boolean = false,
    val replyToText: String? = null,
    val replyToSender: String? = null,
    val isSystemEvent: Boolean = false,
    val poll: GroupPoll? = null,
    val voiceAttachment: VoiceAttachment? = null,
    val fileAttachment: FileAttachment? = null,
    val ratchetStep: Int = 1
)

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun SlantGroupChatScreen(
    groupTitle: String = "Zero-Knowledge Clan",
    membersInfo: String = "48 узлов в сети • 12 онлайн",
    onBackClick: () -> Unit,
    onGroupInfoClick: () -> Unit,
    onCallClick: () -> Unit,
    onOpenFileViewer: (fileName: String, fileSize: String, sender: String) -> Unit = { _, _, _ -> },
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var inputText by remember { mutableStateOf("") }
    var replyingMessage by remember { mutableStateOf<GroupChatMessage?>(null) }
    var showAttachmentSheet by remember { mutableStateOf(false) }
    var showPinnedBanner by remember { mutableStateOf(true) }

    val messages = remember {
        mutableStateListOf(
            GroupChatMessage(
                id = "gm_sys_2",
                senderName = "SYSTEM",
                text = "🔒 Обновлен общий сессионный ключ группы TreeKEM (Эпоха #128)",
                timestamp = "14:15",
                isSystemEvent = true
            ),
            GroupChatMessage(
                id = "gm_6",
                senderName = "cypher_root",
                senderRole = "Security Audit",
                senderColor = Color(0xFFE0E0E0),
                text = "Zero-RAM footprint подтвержден: дамп памяти устройства не содержит ключей Double Ratchet после сворачивания.",
                timestamp = "14:14",
                ratchetStep = 88
            ),
            GroupChatMessage(
                id = "gm_5",
                senderName = "dmiTry",
                senderRole = "Вы",
                text = "У меня в Челябинске через BLE Mesh пинг до соседа всего 4мс, пакеты долетают без потерь.",
                timestamp = "14:12",
                isOutgoing = true,
                ratchetStep = 87
            ),
            GroupChatMessage(
                id = "gm_4",
                senderName = "elena_node",
                senderRole = "Mesh Relay #4",
                senderColor = Color(0xFFCCCCCC),
                replyToSender = "alex_slant",
                replyToText = "Выкатили сборку v3.2.0 на релеи sl-me.ru, проверьте задержки.",
                text = "Пинг через релей sl-me.ru составляет стабильные 18мс. Тест сквозного шифрования успешен.",
                timestamp = "14:10",
                ratchetStep = 86
            ),
            GroupChatMessage(
                id = "gm_poll",
                senderName = "alex_slant",
                senderRole = "Core Dev",
                senderColor = SlantPureWhite,
                timestamp = "14:08",
                poll = GroupPoll(
                    question = "Какой протокол обхода фильтраций приоритезировать в v3.3?",
                    totalVotes = 38,
                    options = listOf(
                        GroupPollOption("opt_1", "VLESS + XTLS Reality (SNI Bypass)", 19),
                        GroupPollOption("opt_2", "Прямой BLE/Wi-Fi Multi-hop Mesh", 12),
                        GroupPollOption("opt_3", "Nostr Relay шлюзы sl-me.online", 7)
                    ),
                    selectedOptionId = "opt_1"
                ),
                ratchetStep = 85
            ),
            GroupChatMessage(
                id = "gm_3",
                senderName = "alex_slant",
                senderRole = "Core Dev",
                senderColor = SlantPureWhite,
                text = "Выкатили сборку v3.2.0 на релеи sl-me.ru, проверьте задержки через BLE Mesh и VLESS Reality.",
                timestamp = "14:05",
                ratchetStep = 84
            ),
            GroupChatMessage(
                id = "gm_voice",
                senderName = "node_relay_09",
                senderRole = "Node Operator",
                senderColor = Color(0xFFAAAAAA),
                timestamp = "13:58",
                voiceAttachment = VoiceAttachment(
                    durationSeconds = 12,
                    waveformAmplitudes = listOf(0.3f, 0.7f, 0.9f, 0.4f, 0.8f, 0.5f, 0.2f, 0.9f, 0.6f, 0.8f, 0.4f, 0.7f, 0.5f, 0.3f)
                ),
                ratchetStep = 83
            ),
            GroupChatMessage(
                id = "gm_sys_1",
                senderName = "SYSTEM",
                text = "⚙️ Узел 0x99A8F3 подключился к кластеру по BLE Mesh (2 хопа)",
                timestamp = "13:50",
                isSystemEvent = true
            )
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SlantOledBlack)
            .testTag("slant_group_chat_screen")
    ) {
        // Живой Neural Canvas
        LivingNeuralCanvas(
            state = if (inputText.isNotEmpty()) NeuralState.USER else NeuralState.IDLE,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.16f)
                .align(Alignment.Center)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
        ) {
            // Верхняя планка группы
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
                    .padding(horizontal = 6.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onGroupInfoClick() }
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

                    Spacer(modifier = Modifier.width(4.dp))

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0x33FFFFFF))
                            .border(1.dp, SlantPureWhite.copy(alpha = 0.4f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = groupTitle.take(2).uppercase(),
                            color = SlantPureWhite,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = groupTitle,
                            color = SlantPureWhite,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = membersInfo,
                            color = SlantDimText,
                            fontSize = 10.sp
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onCallClick,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Call,
                            contentDescription = "Voice Chat",
                            tint = SlantPureWhite,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = onGroupInfoClick,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Info,
                            contentDescription = "Group Info",
                            tint = SlantPureWhite,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Закрепленное сообщение группы
            AnimatedVisibility(
                visible = showPinnedBanner,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 2.dp)
                        .liquidGlass(
                            shape = RoundedCornerShape(14.dp),
                            backgroundColor = Color(0x331E1E1E),
                            alpha = 0.65f,
                            borderWidth = 0.8.dp
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp),
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
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "ПРАВИЛА КЛАСТЕРА • ZERO-KNOWLEDGE",
                                color = SlantPureWhite,
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Только E2EE пакеты, запрет открытых логов. Релеи: sl-me.ru",
                                color = SlantDimText,
                                fontSize = 9.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = { showPinnedBanner = false },
                        modifier = Modifier.size(22.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Dismiss",
                            tint = SlantDimText,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            // Список сообщений группы
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                reverseLayout = true,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                item { Spacer(modifier = Modifier.height(4.dp)) }

                items(messages, key = { it.id }) { msg ->
                    if (msg.isSystemEvent) {
                        GroupSystemEventBanner(text = msg.text ?: "", timestamp = msg.timestamp)
                    } else {
                        GroupMessageBubble(
                            message = msg,
                            onReplyClick = {
                                replyingMessage = msg
                            },
                            onFileClick = { file ->
                                onOpenFileViewer(file.fileName, file.fileSizeFormatted, msg.senderName)
                            }
                        )
                    }
                }

                item { Spacer(modifier = Modifier.height(4.dp)) }
            }

            // Баннер ответа на сообщение (Reply Box)
            AnimatedVisibility(
                visible = replyingMessage != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                if (replyingMessage != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 2.dp)
                            .liquidGlass(
                                shape = RoundedCornerShape(14.dp),
                                backgroundColor = Color(0x33222222),
                                alpha = 0.8f,
                                borderWidth = 0.8.dp
                            )
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Reply,
                                contentDescription = "Replying",
                                tint = SlantPureWhite,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Ответ для ${replyingMessage?.senderName}",
                                    color = SlantPureWhite,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = replyingMessage?.text ?: "Вложение",
                                    color = SlantDimText,
                                    fontSize = 10.sp,
                                    maxLines = 1
                                )
                            }
                        }

                        IconButton(
                            onClick = { replyingMessage = null },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = "Cancel reply",
                                tint = SlantDimText,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }

            // Строка ввода сообщений
            ChatInputBar(
                text = inputText,
                onTextChange = { inputText = it },
                onSendMessage = {
                    if (inputText.isNotBlank()) {
                        messages.add(
                            0,
                            GroupChatMessage(
                                id = "gm_${System.currentTimeMillis()}",
                                senderName = "dmiTry",
                                senderRole = "Вы",
                                text = inputText,
                                timestamp = "14:20",
                                isOutgoing = true,
                                replyToSender = replyingMessage?.senderName,
                                replyToText = replyingMessage?.text,
                                ratchetStep = messages.size + 1
                            )
                        )
                        inputText = ""
                        replyingMessage = null
                    }
                },
                onAttachmentClick = {
                    showAttachmentSheet = true
                },
                onVoiceRecordComplete = { duration ->
                    messages.add(
                        0,
                        GroupChatMessage(
                            id = "gm_v_${System.currentTimeMillis()}",
                            senderName = "dmiTry",
                            senderRole = "Вы",
                            timestamp = "14:21",
                            isOutgoing = true,
                            voiceAttachment = VoiceAttachment(
                                durationSeconds = duration,
                                waveformAmplitudes = listOf(0.4f, 0.8f, 0.5f, 0.9f, 0.3f, 0.7f, 0.6f)
                            ),
                            ratchetStep = messages.size + 1
                        )
                    )
                }
            )
        }

        // Нижняя шторка прикрепления файлов
        if (showAttachmentSheet) {
            AttachmentBottomSheet(
                onDismiss = { showAttachmentSheet = false },
                onSendFile = {
                    showAttachmentSheet = false
                    messages.add(
                        0,
                        GroupChatMessage(
                            id = "gm_f_${System.currentTimeMillis()}",
                            senderName = "dmiTry",
                            senderRole = "Вы",
                            timestamp = "14:22",
                            isOutgoing = true,
                            fileAttachment = FileAttachment("peer_discovery_table.bin", "1.8 MB", "application/octet-stream"),
                            ratchetStep = messages.size + 1
                        )
                    )
                },
                onSendMedia = { _ ->
                    showAttachmentSheet = false
                    messages.add(
                        0,
                        GroupChatMessage(
                            id = "gm_f_${System.currentTimeMillis()}",
                            senderName = "dmiTry",
                            senderRole = "Вы",
                            timestamp = "14:22",
                            isOutgoing = true,
                            fileAttachment = FileAttachment("mesh_topology_snapshot.raw", "4.2 MB", "image/raw"),
                            ratchetStep = messages.size + 1
                        )
                    )
                },
                onSendLocationBeacon = {
                    showAttachmentSheet = false
                    Toast.makeText(context, "Отправка гео-узла заблокирована политикой Anti-Forensics", Toast.LENGTH_SHORT).show()
                },
                onSendContact = {
                    showAttachmentSheet = false
                    Toast.makeText(context, "Zero-Knowledge контакт узла скопирован", Toast.LENGTH_SHORT).show()
                },
                onSetSelfDestruct = { ttl ->
                    showAttachmentSheet = false
                    Toast.makeText(context, "Таймер автоудаления: $ttl", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}

@Composable
private fun GroupSystemEventBanner(text: String, timestamp: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0x22FFFFFF))
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Text(
                text = text,
                color = SlantDimText,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun GroupMessageBubble(
    message: GroupChatMessage,
    onReplyClick: () -> Unit,
    onFileClick: (FileAttachment) -> Unit
) {
    val isOut = message.isOutgoing

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalAlignment = if (isOut) Alignment.End else Alignment.Start
    ) {
        Column(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 20.dp,
                        bottomStart = if (isOut) 20.dp else 4.dp,
                        bottomEnd = if (isOut) 4.dp else 20.dp
                    )
                )
                .liquidGlass(
                    shape = RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 20.dp,
                        bottomStart = if (isOut) 20.dp else 4.dp,
                        bottomEnd = if (isOut) 4.dp else 20.dp
                    ),
                    backgroundColor = if (isOut) Color(0x33FFFFFF) else SlantGlassBase,
                    alpha = if (isOut) 0.65f else 0.55f,
                    borderWidth = 0.8.dp
                )
                .clickable { onReplyClick() }
                .padding(horizontal = 14.dp, vertical = 10.dp)
                .fillMaxWidth(0.85f)
        ) {
            // Имя отправителя и роль
            if (!isOut) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "@${message.senderName}",
                        color = message.senderColor,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Black
                    )

                    if (message.senderRole != null) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0x22FFFFFF))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = message.senderRole,
                                color = SlantDimText,
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

            // Цитата ответа (Reply Quote)
            if (message.replyToText != null && message.replyToSender != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0x18FFFFFF))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(2.dp)
                            .height(24.dp)
                            .background(SlantPureWhite)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = "@${message.replyToSender}",
                            color = SlantPureWhite,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = message.replyToText,
                            color = SlantDimText,
                            fontSize = 9.5.sp,
                            maxLines = 1
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
            }

            // Текст сообщения
            if (message.text != null) {
                Text(
                    text = message.text,
                    color = SlantPureWhite,
                    fontSize = 13.5.sp,
                    lineHeight = 18.sp
                )
            }

            // Интерактивный опрос в группе (Group Poll)
            if (message.poll != null) {
                var selectedOption by remember { mutableStateOf(message.poll.selectedOptionId) }
                var totalVotes by remember { mutableIntStateOf(message.poll.totalVotes) }
                val options = message.poll.options

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                ) {
                    Text(
                        text = "📊 ${message.poll.question}",
                        color = SlantPureWhite,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    options.forEach { opt ->
                        val isSelected = opt.id == selectedOption
                        val percent = if (totalVotes > 0) (opt.votes.toFloat() / totalVotes.toFloat()) else 0f

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) Color(0x33FFFFFF) else Color(0x15FFFFFF))
                                .clickable {
                                    if (selectedOption != opt.id) {
                                        selectedOption = opt.id
                                        totalVotes += 1
                                        opt.votes += 1
                                    }
                                }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (isSelected) {
                                        Icon(Icons.Rounded.Check, null, tint = SlantPureWhite, modifier = Modifier.size(13.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                    }
                                    Text(
                                        text = opt.text,
                                        color = SlantPureWhite,
                                        fontSize = 11.5.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                                Text(
                                    text = "${(percent * 100).toInt()}%",
                                    color = SlantDimText,
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { percent },
                                modifier = Modifier.fillMaxWidth().height(3.dp).clip(CircleShape),
                                color = SlantPureWhite,
                                trackColor = Color(0x22FFFFFF),
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                    Text(
                        text = "$totalVotes голосов • Анонимное голосование кластера",
                        color = SlantDimText,
                        fontSize = 9.5.sp
                    )
                }
            }

            // Голосовое вложение
            if (message.voiceAttachment != null) {
                var isPlaying by remember { mutableStateOf(false) }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { isPlaying = !isPlaying },
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(SlantPureWhite)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.PlayArrow,
                            contentDescription = "Play",
                            tint = SlantOledBlack,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        message.voiceAttachment.waveformAmplitudes.forEach { amp ->
                            Box(
                                modifier = Modifier
                                    .width(3.dp)
                                    .height((amp * 20).dp.coerceAtLeast(3.dp))
                                    .clip(CircleShape)
                                    .background(SlantPureWhite)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "0:${message.voiceAttachment.durationSeconds.toString().padStart(2, '0')}",
                        color = SlantDimText,
                        fontSize = 10.sp
                    )
                }
            }

            // Файловое вложение
            if (message.fileAttachment != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x22FFFFFF))
                        .clickable { onFileClick(message.fileAttachment) }
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.FileDownload,
                            contentDescription = null,
                            tint = SlantPureWhite,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = message.fileAttachment.fileName,
                                color = SlantPureWhite,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${message.fileAttachment.fileSizeFormatted} • RAW Без сжатия",
                                color = SlantDimText,
                                fontSize = 9.5.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Метка времени и Double Ratchet шаг
            Row(
                modifier = Modifier.align(Alignment.End),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "R#${message.ratchetStep} • ${message.timestamp}",
                    color = SlantDimText,
                    fontSize = 9.5.sp,
                    fontFamily = FontFamily.Monospace
                )
                if (isOut) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = "Delivered",
                        tint = SlantPureWhite,
                        modifier = Modifier.size(11.dp)
                    )
                }
            }
        }
    }
}
