package com.slant.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.ContactPage
import androidx.compose.material.icons.rounded.FolderZip
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slant.ui.theme.SlantDimText
import com.slant.ui.theme.SlantGlassBase
import com.slant.ui.theme.SlantOledBlack
import com.slant.ui.theme.SlantPureWhite
import com.slant.ui.theme.liquidGlass

private data class AttachmentAction(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val testTag: String,
    val onClick: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttachmentBottomSheet(
    onDismiss: () -> Unit,
    onSendFile: () -> Unit,
    onSendMedia: (stripExif: Boolean) -> Unit,
    onSendLocationBeacon: () -> Unit,
    onSendContact: () -> Unit,
    onSetSelfDestruct: (String) -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
    var stripExif by remember { mutableStateOf(true) }
    var selectedTtl by remember { mutableStateOf("Выкл") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.Transparent,
        dragHandle = null,
        modifier = modifier.testTag("attachment_bottom_sheet")
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .liquidGlass(
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                    backgroundColor = SlantGlassBase,
                    alpha = 0.85f,
                    borderWidth = 1.dp
                )
                .padding(20.dp)
                .navigationBarsPadding()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Drag Indicator
                Box(
                    modifier = Modifier
                        .size(width = 38.dp, height = 4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color(0x33FFFFFF))
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "ЗАЩИЩЕННЫЕ ВЛОЖЕНИЯ",
                    color = SlantDimText,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Сетка действий
                val actions = listOf(
                    AttachmentAction("Галерея", "Очистка метаданных", Icons.Rounded.Image, "attach_gallery_action") { onSendMedia(stripExif) },
                    AttachmentAction("Файл (до 1 ГБ)", "Байт-в-байт (Raw)", Icons.Rounded.FolderZip, "attach_file_action", onSendFile),
                    AttachmentAction("Камера", "Снимок в ОЗУ", Icons.Rounded.CameraAlt, "attach_camera_action") { onSendMedia(stripExif) },
                    AttachmentAction("Mesh-Маяк", "Локальный пинг", Icons.Rounded.LocationOn, "attach_mesh_beacon_action", onSendLocationBeacon),
                    AttachmentAction("Контакт", "Анонимный ID", Icons.Rounded.ContactPage, "attach_contact_action", onSendContact),
                    AttachmentAction("Таймер", selectedTtl, Icons.Rounded.Timer, "attach_timer_action") {
                        selectedTtl = when (selectedTtl) {
                            "Выкл" -> "10 сек"
                            "10 сек" -> "1 мин"
                            "1 мин" -> "1 час"
                            "1 час" -> "24 часа"
                            else -> "Выкл"
                        }
                        onSetSelfDestruct(selectedTtl)
                    }
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(actions) { action ->
                        Column(
                            modifier = Modifier
                                .liquidGlass(
                                    shape = RoundedCornerShape(18.dp),
                                    backgroundColor = Color(0x33FFFFFF),
                                    alpha = 0.35f,
                                    borderWidth = 0.5.dp
                                )
                                .clickable { action.onClick() }
                                .padding(vertical = 14.dp, horizontal = 8.dp)
                                .testTag(action.testTag),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(Color(0x22FFFFFF)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(action.icon, null, tint = SlantPureWhite, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(action.title, color = SlantPureWhite, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            Text(action.subtitle, color = SlantDimText, fontSize = 9.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Тумблер удаления EXIF метаданных (Anti-Forensics)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .liquidGlass(
                            shape = RoundedCornerShape(16.dp),
                            backgroundColor = SlantGlassBase,
                            alpha = 0.6f,
                            borderWidth = 0.5.dp
                        )
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.Shield, null, tint = SlantPureWhite, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Очистка EXIF / Геотегов", color = SlantPureWhite, fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold)
                            Text("Удаление данных камеры перед отправкой", color = SlantDimText, fontSize = 9.5.sp)
                        }
                    }
                    Switch(
                        checked = stripExif,
                        onCheckedChange = { stripExif = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = SlantOledBlack,
                            checkedTrackColor = SlantPureWhite,
                            uncheckedThumbColor = SlantDimText,
                            uncheckedTrackColor = Color(0x22FFFFFF),
                            uncheckedBorderColor = Color.Transparent
                        ),
                        modifier = Modifier.testTag("strip_exif_switch")
                    )
                }
            }
        }
    }
}
