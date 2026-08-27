package com.slant.ui.screens.chat

import android.view.HapticFeedbackConstants
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.AttachFile
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slant.ui.theme.SlantDimText
import com.slant.ui.theme.SlantGlassBase
import com.slant.ui.theme.SlantOledBlack
import com.slant.ui.theme.SlantPureWhite
import com.slant.ui.theme.liquidGlass
import kotlin.math.roundToInt

@Composable
fun ChatInputBar(
    text: String,
    onTextChange: (String) -> Unit,
    onSendMessage: () -> Unit,
    onAttachmentClick: () -> Unit,
    onVoiceRecordComplete: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current
    var isRecording by remember { mutableStateOf(false) }
    var isLocked by remember { mutableStateOf(false) }
    var dragOffsetY by remember { mutableFloatStateOf(0f) }
    var dragOffsetX by remember { mutableFloatStateOf(0f) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 6.dp)
            .testTag("chat_input_bar"),
        verticalAlignment = Alignment.Bottom
    ) {
        // Главный контейнер ввода Liquid Glass
        Box(
            modifier = Modifier
                .weight(1f)
                .liquidGlass(
                    shape = RoundedCornerShape(26.dp),
                    backgroundColor = SlantGlassBase,
                    alpha = 0.70f,
                    borderWidth = 1.dp
                )
                .padding(horizontal = 6.dp, vertical = 4.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            if (isRecording) {
                // UI активной записи голоса
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color.Red)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ЗАПИСЬ E2EE...",
                            color = SlantPureWhite,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }

                    Text(
                        text = "‹ Свайп для отмены",
                        color = SlantDimText,
                        fontSize = 11.sp
                    )
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onAttachmentClick,
                        modifier = Modifier
                            .size(38.dp)
                            .testTag("chat_attach_button")
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.AttachFile,
                            contentDescription = "Attach File",
                            tint = SlantDimText,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 10.dp)
                    ) {
                        if (text.isEmpty()) {
                            Text(
                                text = "Зашифрованное сообщение...",
                                color = SlantDimText.copy(alpha = 0.6f),
                                fontSize = 13.5.sp
                            )
                        }
                        BasicTextField(
                            value = text,
                            onValueChange = onTextChange,
                            textStyle = TextStyle(
                                color = SlantPureWhite,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Normal
                            ),
                            cursorBrush = SolidColor(SlantPureWhite),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("chat_text_input")
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Кнопка Отправки / Hold-to-Lock Микрофона
        Box(
            modifier = Modifier
                .offset { IntOffset(0, dragOffsetY.coerceIn(-120f, 0f).roundToInt()) }
                .size(48.dp)
                .clip(CircleShape)
                .background(SlantPureWhite)
                .pointerInput(text) {
                    if (text.isBlank()) {
                        detectDragGestures(
                            onDragStart = {
                                isRecording = true
                                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                            },
                            onDragEnd = {
                                if (dragOffsetY < -80f) {
                                    isLocked = true
                                } else if (dragOffsetX < -100f) {
                                    isRecording = false
                                } else {
                                    isRecording = false
                                    onVoiceRecordComplete(5)
                                }
                                dragOffsetY = 0f
                                dragOffsetX = 0f
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                dragOffsetY += dragAmount.y
                                dragOffsetX += dragAmount.x
                            }
                        )
                    }
                }
                .clickable {
                    if (text.isNotBlank()) {
                        onSendMessage()
                    }
                }
                .testTag("chat_action_button"),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (text.isNotBlank()) Icons.AutoMirrored.Rounded.Send else Icons.Rounded.Mic,
                contentDescription = "Action",
                tint = SlantOledBlack,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}
