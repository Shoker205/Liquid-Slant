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
import androidx.compose.material.icons.automirrored.rounded.Reply
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.PhotoSizeSelectActual
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
import androidx.compose.ui.graphics.Color
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
fun ChatSettingsScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var fontSize by remember { mutableFloatStateOf(14f) }
    var sendByEnter by remember { mutableStateOf(false) }
    var swipeToReply by remember { mutableStateOf(true) }
    var autoPlayVideoNotes by remember { mutableStateOf(true) }

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
                    text = "НАСТРОЙКИ ЧАТОВ",
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
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .liquidGlass(RoundedCornerShape(22.dp), backgroundColor = SlantGlassBase, alpha = 0.65f)
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Размер шрифта сообщений", color = SlantPureWhite, fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold)
                            Text("${fontSize.toInt()} sp", color = SlantDimText, fontSize = 12.sp)
                        }
                        Slider(
                            value = fontSize,
                            onValueChange = { fontSize = it },
                            valueRange = 11f..20f,
                            steps = 8,
                            colors = SliderDefaults.colors(thumbColor = SlantPureWhite, activeTrackColor = SlantPureWhite, inactiveTrackColor = Color(0x33FFFFFF))
                        )
                    }
                }

                item {
                    GlassSettingTile(
                        title = "Свайп для ответа",
                        subtitle = "Быстрый жест цитирования сообщения",
                        icon = Icons.AutoMirrored.Rounded.Reply,
                        isChecked = swipeToReply,
                        onCheckedChange = { swipeToReply = it }
                    )
                }

                item {
                    GlassSettingTile(
                        title = "Отправка по Enter",
                        subtitle = "Перенос строки через Shift + Enter",
                        icon = Icons.AutoMirrored.Rounded.Send,
                        isChecked = sendByEnter,
                        onCheckedChange = { sendByEnter = it }
                    )
                }

                item {
                    GlassSettingTile(
                        title = "Автозапуск кружков (Video Notes)",
                        subtitle = "Воспроизведение без сохранения в медиа-кэш",
                        icon = Icons.Rounded.PhotoSizeSelectActual,
                        isChecked = autoPlayVideoNotes,
                        onCheckedChange = { autoPlayVideoNotes = it }
                    )
                }
            }
        }
    }
}
