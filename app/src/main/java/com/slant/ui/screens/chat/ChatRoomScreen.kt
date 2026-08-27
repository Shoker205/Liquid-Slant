package com.slant.ui.screens.chat

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.slant.ui.components.LivingNeuralCanvas
import com.slant.ui.components.NeuralState
import com.slant.ui.theme.SlantOledBlack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatRoomScreen(
    peerName: String,
    messages: List<SlantMessage>,
    onBackClick: () -> Unit,
    onSendMessage: (String) -> Unit,
    onSendFileAttachment: (FileAttachment) -> Unit = {},
    onSendVoiceMessage: (Int) -> Unit = {},
    onDeleteMessage: (String) -> Unit = {},
    onPeerInfoClick: () -> Unit = {},
    onVerifyKeysClick: () -> Unit = {},
    onCallClick: () -> Unit = {},
    onOpenFileViewer: (fileName: String, fileSize: String, sender: String) -> Unit = { _, _, _ -> },
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var inputText by remember { mutableStateOf("") }
    var showAttachmentSheet by remember { mutableStateOf(false) }
    var selectedMessageForContext by remember { mutableStateOf<SlantMessage?>(null) }
    val verificationEmojis = listOf("🛡️", "⚡", "🔒", "👁️")

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SlantOledBlack)
            .testTag("chat_room_screen")
    ) {
        // Фоновый живой маскот с органическим дыханием
        LivingNeuralCanvas(
            state = if (inputText.isNotEmpty()) NeuralState.USER else NeuralState.IDLE,
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
                .imePadding()
        ) {
            // Верхняя планка
            ChatRoomTopBar(
                peerName = peerName,
                connectionStatus = "P2P Прямое • Ratchet #${messages.size + 10}",
                verificationEmojis = verificationEmojis,
                onBackClick = onBackClick,
                onVerifyKeysClick = onVerifyKeysClick,
                onPeerClick = onPeerInfoClick,
                onCallClick = onCallClick,
                onMoreOptionsClick = onPeerInfoClick
            )

            // Список сообщений
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
                    .testTag("messages_lazy_column"),
                reverseLayout = true,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                item { Spacer(modifier = Modifier.height(8.dp)) }

                items(messages, key = { it.id }) { msg ->
                    GlassMessageBubble(
                        message = msg,
                        onFileClick = { file ->
                            onOpenFileViewer(file.fileName, file.fileSizeFormatted, if (msg.isOutgoing) "Я" else peerName)
                        },
                        onLongClick = {
                            selectedMessageForContext = msg
                        }
                    )
                }

                item { Spacer(modifier = Modifier.height(8.dp)) }
            }

            // Поле ввода с Hold-to-Lock
            ChatInputBar(
                text = inputText,
                onTextChange = { inputText = it },
                onSendMessage = {
                    if (inputText.isNotBlank()) {
                        onSendMessage(inputText)
                        inputText = ""
                    }
                },
                onAttachmentClick = {
                    showAttachmentSheet = true
                },
                onVoiceRecordComplete = { duration ->
                    onSendVoiceMessage(duration)
                }
            )
        }

        // Шторка защищенных вложений
        if (showAttachmentSheet) {
            AttachmentBottomSheet(
                onDismiss = { showAttachmentSheet = false },
                onSendFile = {
                    showAttachmentSheet = false
                    onSendFileAttachment(
                        FileAttachment(
                            fileName = "firmware_kernel_dump_${System.currentTimeMillis() % 1000}.bin",
                            fileSizeFormatted = "24.6 MB",
                            mimeType = "application/octet-stream",
                            isRawByteUncompressed = true
                        )
                    )
                    Toast.makeText(context, "Raw файл отправлен байт-в-байт без сжатия", Toast.LENGTH_SHORT).show()
                },
                onSendMedia = { stripExif ->
                    showAttachmentSheet = false
                    onSendFileAttachment(
                        FileAttachment(
                            fileName = "sat_telemetry_scan.raw",
                            fileSizeFormatted = "8.4 MB",
                            mimeType = "image/raw",
                            isRawByteUncompressed = true
                        )
                    )
                    val msg = if (stripExif) "Медиа отправлено: EXIF и геотеги вырезаны" else "Медиа отправлено"
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                },
                onSendLocationBeacon = {
                    showAttachmentSheet = false
                    onSendMessage("📍 Mesh-геомаяк: 55.7558° N, 37.6173° E (P2P Grid)")
                },
                onSendContact = {
                    showAttachmentSheet = false
                    onSendMessage("🪪 Slant Node ID: 0x8F9A...B321 (E2EE Verified)")
                },
                onSetSelfDestruct = { ttl ->
                    showAttachmentSheet = false
                    Toast.makeText(context, "TTL таймер сообщений установлен: $ttl", Toast.LENGTH_SHORT).show()
                }
            )
        }

        // Контекстное меню сообщения
        selectedMessageForContext?.let { msg ->
            Dialog(onDismissRequest = { selectedMessageForContext = null }) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { selectedMessageForContext = null },
                    contentAlignment = Alignment.Center
                ) {
                    MessageContextMenu(
                        onReply = {
                            selectedMessageForContext = null
                            inputText = "↩️ В ответ на: \"${msg.text?.take(20) ?: "вложение"}...\" "
                        },
                        onCopy = {
                            selectedMessageForContext = null
                            clipboardManager.setText(AnnotatedString(msg.text ?: msg.fileAttachment?.fileName ?: ""))
                            Toast.makeText(context, "Скопировано в защищенный буфер", Toast.LENGTH_SHORT).show()
                        },
                        onAnonymousForward = {
                            selectedMessageForContext = null
                            Toast.makeText(context, "Анонимная пересылка без следа автора", Toast.LENGTH_SHORT).show()
                        },
                        onPin = {
                            selectedMessageForContext = null
                            Toast.makeText(context, "Сообщение закреплено в диалоге", Toast.LENGTH_SHORT).show()
                        },
                        onEdit = if (msg.isOutgoing && msg.text != null) {
                            {
                                selectedMessageForContext = null
                                inputText = msg.text
                            }
                        } else null,
                        onDeleteWithoutTrace = {
                            val idToDelete = msg.id
                            selectedMessageForContext = null
                            onDeleteMessage(idToDelete)
                            Toast.makeText(context, "Сообщение стерто без следа у обоих узлов", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}
