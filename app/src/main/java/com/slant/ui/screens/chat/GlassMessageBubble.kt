package com.slant.ui.screens.chat

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.DoneAll
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slant.ui.screens.main.DeliveryStatus
import com.slant.ui.theme.SlantDimText
import com.slant.ui.theme.SlantGlassBase
import com.slant.ui.theme.SlantOledBlack
import com.slant.ui.theme.SlantPureWhite
import com.slant.ui.theme.liquidGlass

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GlassMessageBubble(
    message: SlantMessage,
    onFileClick: ((FileAttachment) -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val bubbleShape = if (message.isOutgoing) {
        RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 20.dp, bottomEnd = 4.dp)
    } else {
        RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 4.dp, bottomEnd = 20.dp)
    }

    val bubbleBackground = if (message.isOutgoing) {
        Color(0x38FFFFFF)
    } else {
        SlantGlassBase
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = if (message.isOutgoing) 64.dp else 0.dp,
                end = if (message.isOutgoing) 0.dp else 64.dp,
                top = 3.dp,
                bottom = 3.dp
            )
            .testTag("message_bubble_${message.id}"),
        contentAlignment = if (message.isOutgoing) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Column(
            modifier = Modifier
                .widthIn(min = 90.dp, max = 320.dp)
                .liquidGlass(
                    shape = bubbleShape,
                    backgroundColor = bubbleBackground,
                    alpha = if (message.isOutgoing) 0.60f else 0.45f,
                    borderWidth = 0.5.dp
                )
                .combinedClickable(
                    onClick = {
                        if (message.fileAttachment != null) {
                            onFileClick?.invoke(message.fileAttachment)
                        }
                    },
                    onLongClick = onLongClick
                )
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            when (message.messageType) {
                MessageType.TEXT -> {
                    Text(
                        text = message.text.orEmpty(),
                        color = SlantPureWhite,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
                MessageType.VOICE -> {
                    VoiceMessagePlayerContent(voice = message.voiceAttachment)
                }
                MessageType.FILE -> {
                    FileAttachmentContent(
                        file = message.fileAttachment,
                        onClick = {
                            message.fileAttachment?.let { onFileClick?.invoke(it) }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Нижняя служебная строка: время, Ratchet шаг, статус
            Row(
                modifier = Modifier.align(Alignment.End),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "R#${message.ratchetStep}",
                    color = SlantDimText.copy(alpha = 0.6f),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = message.timestamp,
                    color = SlantDimText,
                    fontSize = 10.sp
                )

                if (message.isOutgoing) {
                    when (message.deliveryStatus) {
                        DeliveryStatus.SENDING -> Icon(Icons.Rounded.Schedule, null, tint = SlantDimText, modifier = Modifier.size(11.dp))
                        DeliveryStatus.SENT -> Icon(Icons.Rounded.Done, null, tint = SlantDimText, modifier = Modifier.size(11.dp))
                        DeliveryStatus.READ -> Icon(Icons.Rounded.DoneAll, null, tint = SlantPureWhite, modifier = Modifier.size(11.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun VoiceMessagePlayerContent(voice: VoiceAttachment?) {
    if (voice == null) return

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(SlantPureWhite)
                .clickable { },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.PlayArrow,
                contentDescription = "Play",
                tint = SlantOledBlack,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Визуализатор волны
        Row(
            modifier = Modifier
                .weight(1f)
                .height(26.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            voice.waveformAmplitudes.take(28).forEach { amp ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height((amp * 24).coerceIn(4f, 24f).dp)
                        .clip(RoundedCornerShape(1.dp))
                        .background(SlantPureWhite.copy(alpha = 0.75f))
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "${voice.durationSeconds}s",
            color = SlantPureWhite,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun FileAttachmentContent(
    file: FileAttachment?,
    onClick: (() -> Unit)? = null
) {
    if (file == null) return

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0x33FFFFFF)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Description,
                contentDescription = "File",
                tint = SlantPureWhite,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = file.fileName,
                color = SlantPureWhite,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${file.fileSizeFormatted} • RAW BYTE",
                    color = SlantDimText,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Rounded.Shield,
                    contentDescription = "Uncompressed",
                    tint = SlantDimText,
                    modifier = Modifier.size(10.dp)
                )
            }
        }
    }
}
