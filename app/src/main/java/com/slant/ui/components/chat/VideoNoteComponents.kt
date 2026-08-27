package com.slant.ui.components.chat

import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cameraswitch
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slant.ui.theme.SlantDimText
import com.slant.ui.theme.SlantGlassBase
import com.slant.ui.theme.SlantOledBlack
import com.slant.ui.theme.SlantPureWhite
import com.slant.ui.theme.liquidGlass

/**
 * Оверлей круговой записи видеосообщения в реальном времени
 */
@Composable
fun VideoNoteRecorderOverlay(
    isRecording: Boolean,
    maxDurationSeconds: Int = 60,
    onCancel: () -> Unit,
    onSend: (durationSec: Int) -> Unit,
    onFlipCamera: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!isRecording) return

    val view = LocalView.current
    val progress = remember { Animatable(0f) }

    LaunchedEffect(isRecording) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = maxDurationSeconds * 1000, easing = LinearEasing)
        )
        if (progress.value >= 1f) {
            onSend(maxDurationSeconds)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.75f))
            .testTag("video_note_recorder_overlay"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Круглый видоискатель
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .clip(CircleShape)
                    .liquidGlass(CircleShape, backgroundColor = SlantGlassBase, alpha = 0.85f, borderWidth = 0.dp),
                contentAlignment = Alignment.Center
            ) {
                // Визуализатор видеопотока
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF151515)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "LIVE CAMERA FEED",
                        color = SlantDimText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                // Круговой прогресс-бар записи
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val stroke = 5.dp.toPx()
                    drawCircle(
                        color = Color(0x33FFFFFF),
                        style = Stroke(stroke)
                    )
                    drawArc(
                        brush = Brush.sweepGradient(
                            listOf(SlantPureWhite.copy(alpha = 0.5f), SlantPureWhite)
                        ),
                        startAngle = -90f,
                        sweepAngle = progress.value * 360f,
                        useCenter = false,
                        style = Stroke(stroke, cap = StrokeCap.Round)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Время записи
            val currentSec = (progress.value * maxDurationSeconds).toInt()
            Text(
                text = String.format("00:%02d / 01:00", currentSec),
                color = SlantPureWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Панель управления записью
            Row(
                modifier = Modifier
                    .liquidGlass(RoundedCornerShape(32.dp), backgroundColor = SlantGlassBase, alpha = 0.75f)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                IconButton(
                    onClick = onFlipCamera,
                    modifier = Modifier.testTag("video_note_flip_camera_button")
                ) {
                    Icon(Icons.Rounded.Cameraswitch, "Flip", tint = SlantPureWhite)
                }

                IconButton(
                    onClick = {
                        view.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
                        onSend(currentSec)
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(SlantPureWhite)
                        .testTag("video_note_stop_send_button")
                ) {
                    Icon(Icons.Rounded.Stop, "Stop & Send", tint = SlantOledBlack)
                }

                Text(
                    text = "Отмена",
                    color = SlantDimText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable { onCancel() }
                        .padding(horizontal = 8.dp)
                        .testTag("video_note_cancel_button")
                )
            }
        }
    }
}

/**
 * Круглое видеосообщение в ленте чата
 */
@Composable
fun VideoNoteBubble(
    durationSec: Int,
    isPlaying: Boolean,
    playbackProgress: Float,
    onTogglePlay: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(190.dp)
            .clip(CircleShape)
            .liquidGlass(CircleShape, backgroundColor = SlantGlassBase, alpha = 0.6f, borderWidth = 1.dp)
            .clickable { onTogglePlay() }
            .testTag("video_note_bubble"),
        contentAlignment = Alignment.Center
    ) {
        // Контейнер видео
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(3.dp)
                .clip(CircleShape)
                .background(Color(0xFF121212)),
            contentAlignment = Alignment.Center
        ) {
            if (!isPlaying) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .liquidGlass(CircleShape, backgroundColor = SlantGlassBase, alpha = 0.8f)
                        .border(1.dp, SlantPureWhite.copy(alpha = 0.3f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.PlayArrow,
                        contentDescription = "Play",
                        tint = SlantPureWhite,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        }

        // Индикатор воспроизведения
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = 3.dp.toPx()
            drawArc(
                color = SlantPureWhite,
                startAngle = -90f,
                sweepAngle = playbackProgress * 360f,
                useCenter = false,
                style = Stroke(stroke, cap = StrokeCap.Round)
            )
        }

        // Длительность
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 10.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Black.copy(alpha = 0.6f))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = String.format("00:%02d", durationSec),
                color = SlantPureWhite,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
