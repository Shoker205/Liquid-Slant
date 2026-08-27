package com.slant.ui.screens.folders

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slant.ui.components.LivingNeuralCanvas
import com.slant.ui.components.NeuralState
import com.slant.ui.components.SlantGlassTextField
import com.slant.ui.models.ChatFolder
import com.slant.ui.models.FolderFilterType
import com.slant.ui.theme.SlantDimText
import com.slant.ui.theme.SlantErrorRed
import com.slant.ui.theme.SlantGlassBase
import com.slant.ui.theme.SlantOledBlack
import com.slant.ui.theme.SlantPureWhite
import com.slant.ui.theme.liquidGlass

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChatFoldersManagementScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val folders = remember {
        mutableStateListOf(
            ChatFolder("1", "Личные P2P", includedTypes = setOf(FolderFilterType.CONTACTS, FolderFilterType.P2P_ONLY)),
            ChatFolder("2", "Mesh Кластеры", includedTypes = setOf(FolderFilterType.GROUPS, FolderFilterType.MESH_ONLY)),
            ChatFolder("3", "Каналы и Инфо", includedTypes = setOf(FolderFilterType.CHANNELS))
        )
    }

    var isCreatingNewFolder by remember { mutableStateOf(false) }
    var newFolderName by remember { mutableStateOf("") }
    var selectedFilters by remember { mutableStateOf(setOf<FolderFilterType>()) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SlantOledBlack)
    ) {
        LivingNeuralCanvas(
            state = NeuralState.IDLE,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.15f)
                .align(Alignment.Center)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // Верхняя планка
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
                    text = "ПАПКИ С ЧАТАМИ",
                    color = SlantPureWhite,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "Папки помогают группировать зашифрованные чаты и каналы. Они доступны по долгому нажатию на вкладку в докбаре.",
                        color = SlantDimText,
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                    )
                }

                // Кнопка создания новой папки
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .liquidGlass(RoundedCornerShape(20.dp), backgroundColor = SlantGlassBase, alpha = 0.70f)
                            .clickable { isCreatingNewFolder = !isCreatingNewFolder }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0x22FFFFFF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Rounded.Add, null, tint = SlantPureWhite, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Text(
                            text = "Создать новую папку",
                            color = SlantPureWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Форма создания новой папки
                item {
                    AnimatedVisibility(visible = isCreatingNewFolder) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .liquidGlass(RoundedCornerShape(24.dp), backgroundColor = SlantGlassBase, alpha = 0.85f)
                                .padding(16.dp)
                        ) {
                            Text("НАЗВАНИЕ ПАПКИ", color = SlantDimText, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            SlantGlassTextField(
                                value = newFolderName,
                                onValueChange = { newFolderName = it },
                                placeholder = "Например: Секретные P2P"
                            )

                            Spacer(modifier = Modifier.height(14.dp))
                            Text("ТИПЫ ЧАТОВ", color = SlantDimText, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                            Spacer(modifier = Modifier.height(8.dp))

                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                FolderFilterType.entries.forEach { filter ->
                                    val isSelected = selectedFilters.contains(filter)
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(if (isSelected) SlantPureWhite else Color(0x22FFFFFF))
                                            .clickable {
                                                selectedFilters = if (isSelected) selectedFilters - filter else selectedFilters + filter
                                            }
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = filter.title,
                                            color = if (isSelected) SlantOledBlack else SlantPureWhite,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    if (newFolderName.isNotBlank()) {
                                        folders.add(
                                            ChatFolder(
                                                id = System.currentTimeMillis().toString(),
                                                name = newFolderName,
                                                includedTypes = selectedFilters
                                            )
                                        )
                                        newFolderName = ""
                                        selectedFilters = emptySet()
                                        isCreatingNewFolder = false
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().height(46.dp),
                                shape = RoundedCornerShape(18.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SlantPureWhite, contentColor = SlantOledBlack)
                            ) {
                                Text("СОХРАНИТЬ ПАПКУ", fontWeight = FontWeight.Black, fontSize = 11.5.sp, letterSpacing = 1.sp)
                            }
                        }
                    }
                }

                // Список существующих папок
                item {
                    Text(
                        text = "АКТИВНЫЕ ПАПКИ",
                        color = SlantDimText,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        modifier = Modifier.padding(start = 6.dp, top = 6.dp)
                    )
                }

                items(folders, key = { it.id }) { folder ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .liquidGlass(RoundedCornerShape(20.dp), backgroundColor = SlantGlassBase, alpha = 0.55f)
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Rounded.Folder, null, tint = SlantPureWhite, modifier = Modifier.size(22.dp))
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(folder.name, color = SlantPureWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                Text(
                                    text = folder.includedTypes.joinToString(", ") { it.title }.ifEmpty { "Все чаты" },
                                    color = SlantDimText,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        IconButton(onClick = { folders.remove(folder) }) {
                            Icon(Icons.Rounded.Delete, "Удалить", tint = SlantErrorRed, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    }
}
