package com.slant.ui.screens.media

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.FileDownload
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slant.ui.theme.SlantDimText
import com.slant.ui.theme.SlantGlassBase
import com.slant.ui.theme.SlantOledBlack
import com.slant.ui.theme.SlantPureWhite
import com.slant.ui.theme.liquidGlass

@Composable
fun MediaViewerScreen(
    fileName: String,
    fileSize: String,
    senderName: String,
    timestamp: String,
    onBackClick: () -> Unit,
    onExportByteForByte: () -> Unit,
    onDeleteLocally: () -> Unit,
    modifier: Modifier = Modifier
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SlantOledBlack)
            .testTag("media_viewer_screen")
    ) {
        // Контейнер медиафайла с жестами масштабирования
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale = (scale * zoom).coerceIn(1f, 4f)
                        offsetX += pan.x
                        offsetY += pan.y
                    }
                }
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offsetX,
                    translationY = offsetY
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(320.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFF141414)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Rounded.Lock, null, tint = SlantDimText, modifier = Modifier.size(36.dp))
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("ЗАШИФРОВАННЫЙ RAW МЕДИАФАЙЛ", color = SlantDimText, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text("$fileName ($fileSize)", color = SlantPureWhite, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
            }
        }

        // Верхний бар
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .liquidGlass(RoundedCornerShape(24.dp), backgroundColor = SlantGlassBase, alpha = 0.70f)
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.testTag("media_viewer_back_button")
                ) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Back", tint = SlantPureWhite)
                }
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(senderName, color = SlantPureWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text(timestamp, color = SlantDimText, fontSize = 10.sp)
                }
            }

            IconButton(
                onClick = {},
                modifier = Modifier.testTag("media_viewer_info_button")
            ) {
                Icon(Icons.Rounded.Info, "Info", tint = SlantPureWhite)
            }
        }

        // Нижняя панель действий (Экспорт, Удаление, Поделиться)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .liquidGlass(RoundedCornerShape(28.dp), backgroundColor = SlantGlassBase, alpha = 0.80f)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onExportByteForByte,
                modifier = Modifier.testTag("media_viewer_export_button")
            ) {
                Icon(Icons.Rounded.FileDownload, "Экспорт Raw", tint = SlantPureWhite)
            }
            IconButton(
                onClick = {},
                modifier = Modifier.testTag("media_viewer_share_button")
            ) {
                Icon(Icons.Rounded.Share, "Анонимная пересылка", tint = SlantPureWhite)
            }
            IconButton(
                onClick = onDeleteLocally,
                modifier = Modifier.testTag("media_viewer_delete_button")
            ) {
                Icon(Icons.Rounded.Delete, "Удалить из ОЗУ/Кэша", tint = Color(0xFFFF453A))
            }
        }
    }
}
