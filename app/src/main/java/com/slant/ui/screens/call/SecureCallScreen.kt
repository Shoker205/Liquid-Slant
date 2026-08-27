package com.slant.ui.screens.call

import android.view.HapticFeedbackConstants
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CallEnd
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.MicOff
import androidx.compose.material.icons.rounded.ScreenShare
import androidx.compose.material.icons.rounded.StopScreenShare
import androidx.compose.material.icons.rounded.Videocam
import androidx.compose.material.icons.rounded.VideocamOff
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slant.ui.components.LivingNeuralCanvas
import com.slant.ui.components.NeuralState
import com.slant.ui.theme.SlantDimText
import com.slant.ui.theme.SlantErrorRed
import com.slant.ui.theme.SlantGlassBase
import com.slant.ui.theme.SlantOledBlack
import com.slant.ui.theme.SlantPureWhite
import com.slant.ui.theme.liquidGlass
import kotlinx.coroutines.delay

enum class CallType {
    AUDIO, VIDEO, SCREEN_SHARE
}

@Composable
fun SecureCallScreen(
    peerName: String,
    callType: CallType = CallType.AUDIO,
    verificationEmojis: List<String> = listOf("🛡️", "⚡", "🔒", "👁️"),
    onEndCall: () -> Unit,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current
    var isMuted by remember { mutableStateOf(false) }
    var isVideoEnabled by remember { mutableStateOf(callType == CallType.VIDEO) }
    var isScreenSharing by remember { mutableStateOf(callType == CallType.SCREEN_SHARE) }
    var isSpeakerOn by remember { mutableStateOf(false) }
    var durationSeconds by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            durationSeconds++
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SlantOledBlack)
            .testTag("secure_call_screen")
    ) {
        // Фоновый живой холст или видеопоток
        if (isVideoEnabled || isScreenSharing) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF0A0A0A)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isScreenSharing) "ДЕМОНСТРАЦИЯ ЭКРАНА (E2EE)" else "ВХОДЯЩИЙ ВИДЕОПОТОК (WEBRTC)",
                    color = SlantDimText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
            }
        } else {
            LivingNeuralCanvas(
                state = NeuralState.IDLE,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.25f)
                    .align(Alignment.Center)
            )
        }

        // Основной слой поверх видео
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Верхняя планка: Верификация и статус шифрования
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .liquidGlass(
                        shape = RoundedCornerShape(26.dp),
                        backgroundColor = SlantGlassBase,
                        alpha = 0.70f,
                        borderWidth = 1.dp
                    )
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.Lock,
                        contentDescription = "E2EE",
                        tint = SlantPureWhite,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "P2P ENCRYPTED",
                        color = SlantPureWhite,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                }

                // 4-Emoji верификатор
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0x22FFFFFF))
                        .padding(horizontal = 8.dp, vertical = 3.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    verificationEmojis.forEach { emoji ->
                        Text(text = emoji, fontSize = 12.sp)
                    }
                }
            }

            // Центральный блок профиля (при аудиовызове)
            if (!isVideoEnabled && !isScreenSharing) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .liquidGlass(CircleShape, backgroundColor = SlantGlassBase, alpha = 0.8f, borderWidth = 1.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = peerName.take(2).uppercase(),
                            color = SlantPureWhite,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = peerName,
                        color = SlantPureWhite,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    val min = durationSeconds / 60
                    val sec = durationSeconds % 60
                    Text(
                        text = String.format("%02d:%02d", min, sec),
                        color = SlantDimText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 1.sp
                    )
                }
            } else {
                // PIP миниатюра собственной камеры
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.TopEnd
                ) {
                    Box(
                        modifier = Modifier
                            .size(width = 100.dp, height = 140.dp)
                            .liquidGlass(RoundedCornerShape(18.dp), backgroundColor = SlantGlassBase, alpha = 0.85f, borderWidth = 1.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "YOU",
                            color = SlantDimText,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Нижняя панель управления вызовом (Material 3 Liquid Glass Dock)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .liquidGlass(
                        shape = RoundedCornerShape(36.dp),
                        backgroundColor = SlantGlassBase,
                        alpha = 0.80f,
                        borderWidth = 1.dp
                    )
                    .padding(horizontal = 14.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Микрофон
                    CallControlButton(
                        icon = if (isMuted) Icons.Rounded.MicOff else Icons.Rounded.Mic,
                        isActive = isMuted,
                        testTag = "call_mute_button",
                        onClick = {
                            view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                            isMuted = !isMuted
                        }
                    )

                    // Видеокамера
                    CallControlButton(
                        icon = if (isVideoEnabled) Icons.Rounded.Videocam else Icons.Rounded.VideocamOff,
                        isActive = isVideoEnabled,
                        testTag = "call_video_toggle_button",
                        onClick = {
                            view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                            isVideoEnabled = !isVideoEnabled
                            if (isVideoEnabled) isScreenSharing = false
                        }
                    )

                    // Демонстрация экрана
                    CallControlButton(
                        icon = if (isScreenSharing) Icons.Rounded.StopScreenShare else Icons.Rounded.ScreenShare,
                        isActive = isScreenSharing,
                        testTag = "call_screenshare_toggle_button",
                        onClick = {
                            view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                            isScreenSharing = !isScreenSharing
                            if (isScreenSharing) isVideoEnabled = false
                        }
                    )

                    // Динамик
                    CallControlButton(
                        icon = Icons.Rounded.VolumeUp,
                        isActive = isSpeakerOn,
                        testTag = "call_speaker_toggle_button",
                        onClick = {
                            view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                            isSpeakerOn = !isSpeakerOn
                        }
                    )

                    // Сброс звонка (Красный стеклянный акцент)
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(SlantErrorRed)
                            .clickable {
                                view.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
                                onEndCall()
                            }
                            .testTag("call_end_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.CallEnd,
                            contentDescription = "End Call",
                            tint = SlantPureWhite,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CallControlButton(
    icon: ImageVector,
    isActive: Boolean,
    testTag: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(if (isActive) SlantPureWhite else Color(0x22FFFFFF))
            .border(
                1.dp,
                if (isActive) SlantPureWhite else Color(0x33FFFFFF),
                CircleShape
            )
            .clickable { onClick() }
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isActive) SlantOledBlack else SlantPureWhite,
            modifier = Modifier.size(22.dp)
        )
    }
}
