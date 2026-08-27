package com.slant.ui.screens.main

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.CollectionsBookmark
import androidx.compose.material.icons.rounded.FolderZip
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.QrCodeScanner
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slant.ui.components.LivingNeuralCanvas
import com.slant.ui.components.NeuralState
import com.slant.ui.models.ChatFolder
import com.slant.ui.models.FolderFilterType
import com.slant.ui.theme.SlantAppStateManager
import com.slant.ui.theme.SlantDimText
import com.slant.ui.theme.SlantGlassBase
import com.slant.ui.theme.SlantOledBlack
import com.slant.ui.theme.SlantPureWhite
import com.slant.ui.theme.SlantStrings
import com.slant.ui.theme.liquidGlass

private data class QuickTestItem(
    val title: String,
    val icon: ImageVector,
    val tag: String,
    val action: () -> Unit
)

@Composable
fun SlantMainScreen(
    chats: List<SlantChatItem>,
    onChatClick: (SlantChatItem) -> Unit,
    onProfileClick: () -> Unit,
    onQrScanClick: () -> Unit,
    onSearchClick: () -> Unit = {},
    onSavedNotesClick: () -> Unit = {},
    onGroupInfoClick: (String) -> Unit = {},
    onMediaViewerClick: () -> Unit = {},
    onCallClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onAuthClick: () -> Unit = {},
    onOpenFoldersManagement: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedCategoryIndex by remember { mutableIntStateOf(0) }
    var currentDockSection by remember { mutableStateOf(MainDockSection.CHATS) }

    val categories = listOf(
        SlantStrings.categoryAll,
        SlantStrings.categoryP2p,
        SlantStrings.categoryMesh,
        SlantStrings.categoryEncrypted,
        SlantStrings.categoryChannels
    )

    val sampleFolders = remember(SlantAppStateManager.language.value) {
        listOf(
            ChatFolder("all", SlantStrings.folderAllChats, isDefault = true),
            ChatFolder("p2p", SlantStrings.folderPersonalP2p, includedTypes = setOf(FolderFilterType.CONTACTS, FolderFilterType.P2P_ONLY)),
            ChatFolder("mesh", SlantStrings.folderMeshClusters, includedTypes = setOf(FolderFilterType.GROUPS, FolderFilterType.MESH_ONLY)),
            ChatFolder("channels", SlantStrings.folderChannelsInfo, includedTypes = setOf(FolderFilterType.CHANNELS))
        )
    }
    var selectedFolderId by remember { mutableStateOf("all") }

    val quickNavItems = listOf(
        QuickTestItem(SlantStrings.quickSearch, Icons.Rounded.Search, "quick_nav_search", onSearchClick),
        QuickTestItem(SlantStrings.quickVault, Icons.Rounded.CollectionsBookmark, "quick_nav_notes", onSavedNotesClick),
        QuickTestItem(SlantStrings.quickFolders, Icons.Rounded.Settings, "quick_nav_folders", onOpenFoldersManagement),
        QuickTestItem(SlantStrings.quickCluster, Icons.Rounded.Groups, "quick_nav_group", { onGroupInfoClick("Zero-Knowledge Clan") }),
        QuickTestItem(SlantStrings.quickMedia, Icons.Rounded.FolderZip, "quick_nav_media", onMediaViewerClick),
        QuickTestItem(SlantStrings.quickCall, Icons.Rounded.Call, "quick_nav_call", onCallClick),
        QuickTestItem(SlantStrings.quickSettings, Icons.Rounded.Settings, "quick_nav_settings", onSettingsClick),
        QuickTestItem(SlantStrings.quickAntiForensics, Icons.Rounded.Security, "quick_nav_security", onProfileClick),
        QuickTestItem(SlantStrings.quickAuth, Icons.Rounded.Key, "quick_nav_auth", onAuthClick)
    )

    val filteredChats = remember(selectedCategoryIndex, currentDockSection, selectedFolderId, chats) {
        val baseList = when {
            selectedFolderId == "p2p" -> chats.filter { it.isP2PDirect }
            selectedFolderId == "mesh" -> chats.filter { it.isMesh }
            selectedFolderId == "channels" -> chats.filter { it.id.startsWith("channel") || it.title.contains("Core") || it.title.contains("Broadcast") }
            currentDockSection == MainDockSection.GROUPS -> chats.filter { it.isMesh || it.title.contains("Cluster") || it.title.contains("Clan") }
            currentDockSection == MainDockSection.CHANNELS -> chats.filter { it.id.startsWith("channel") || it.title.contains("Core") || it.title.contains("Broadcast") }
            currentDockSection == MainDockSection.BOTS -> chats.filter { it.id.startsWith("bot") || it.title.contains("Agent") || it.title.contains("Cortex") }
            selectedCategoryIndex == 1 -> chats.filter { it.isP2PDirect }
            selectedCategoryIndex == 2 -> chats.filter { it.isMesh }
            selectedCategoryIndex == 3 -> chats.filter { it.isP2PDirect || it.isMesh }
            selectedCategoryIndex == 4 -> chats.filter { it.id.startsWith("channel") || it.title.contains("Core") }
            else -> chats
        }
        baseList
    }

    val isDark = SlantAppStateManager.isDark
    val textColor = if (isDark) SlantPureWhite else MaterialTheme.colorScheme.onBackground
    val dimColor = if (isDark) SlantDimText else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(if (isDark) SlantOledBlack else MaterialTheme.colorScheme.background)
            .testTag("slant_main_screen")
    ) {
        // Фоновый живой маскот
        LivingNeuralCanvas(
            state = NeuralState.IDLE,
            modifier = Modifier
                .fillMaxSize()
                .alpha(if (isDark) 0.18f else 0.08f)
                .align(Alignment.Center)
        )

        // Контент поверх фона
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Верхняя панель TopBar с компактным профилем слева сверху
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 6.dp)
                    .liquidGlass(
                        shape = RoundedCornerShape(22.dp),
                        alpha = 0.70f,
                        borderWidth = 1.dp
                    )
                    .padding(horizontal = 10.dp, vertical = 5.dp)
                    .testTag("main_top_bar"),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Иконка / Аватар профиля слева сверху
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onProfileClick() }
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                        .testTag("main_profile_header_btn")
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(if (isDark) Color(0x33FFFFFF) else MaterialTheme.colorScheme.primaryContainer)
                            .border(1.dp, if (isDark) Color(0x66FFFFFF) else MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), CircleShape)
                            .testTag("main_profile_avatar"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Person,
                            contentDescription = SlantStrings.profile,
                            tint = if (isDark) SlantPureWhite else MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(9.dp))

                    Column {
                        Text(
                            text = "SLANT",
                            color = textColor,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.5.sp
                        )
                        Text(
                            text = "dmiTry • online",
                            color = dimColor,
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onQrScanClick,
                        modifier = Modifier.size(36.dp).testTag("main_qr_scan_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.QrCodeScanner,
                            contentDescription = "Scan Node QR",
                            tint = textColor,
                            modifier = Modifier.size(19.dp)
                        )
                    }
                    IconButton(
                        onClick = onSearchClick,
                        modifier = Modifier.size(36.dp).testTag("main_search_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = "Search",
                            tint = textColor,
                            modifier = Modifier.size(19.dp)
                        )
                    }
                }
            }

            // Быстрый доступ к разделам и экранам (Quick Access Hub)
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp)
            ) {
                items(quickNavItems, key = { it.tag }) { item ->
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .liquidGlass(
                                shape = RoundedCornerShape(14.dp),
                                alpha = 0.55f,
                                borderWidth = 0.7.dp
                            )
                            .clickable { item.action() }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag(item.tag),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = textColor,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = item.title,
                            color = textColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Фильтры категорий
            ScrollableTabRow(
                selectedTabIndex = selectedCategoryIndex,
                containerColor = Color.Transparent,
                contentColor = textColor,
                edgePadding = 14.dp,
                divider = {},
                indicator = {},
                modifier = Modifier.testTag("categories_tab_row")
            ) {
                categories.forEachIndexed { index, title ->
                    val isSelected = selectedCategoryIndex == index
                    Tab(
                        selected = isSelected,
                        onClick = { selectedCategoryIndex = index },
                        modifier = Modifier
                            .padding(vertical = 4.dp, horizontal = 3.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .then(
                                if (isSelected) Modifier.liquidGlass(
                                    shape = RoundedCornerShape(14.dp),
                                    backgroundColor = if (isDark) Color(0x55FFFFFF) else MaterialTheme.colorScheme.primaryContainer,
                                    alpha = 0.35f
                                ) else Modifier
                            )
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                            .testTag("category_tab_$index")
                    ) {
                        Text(
                            text = title.uppercase(),
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            letterSpacing = 0.7.sp,
                            color = if (isSelected) textColor else dimColor
                        )
                    }
                }
            }

            // Список чатов
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp)
                    .testTag("chats_lazy_column"),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                item { Spacer(modifier = Modifier.height(2.dp)) }

                items(filteredChats, key = { it.id }) { chat ->
                    CompactGlassChatItem(
                        chat = chat,
                        onClick = {
                            if (chat.id == "saved_notes") {
                                onSavedNotesClick()
                            } else {
                                onChatClick(chat)
                            }
                        }
                    )
                }

                item { Spacer(modifier = Modifier.height(72.dp)) } // Отступ под компактный парящий докбар
            }
        }

        // Компактный парящий прозрачный докбар
        SlantDockWithFolderPicker(
            currentSection = currentDockSection,
            folders = sampleFolders,
            selectedFolderId = selectedFolderId,
            onSectionSelected = { section ->
                currentDockSection = section
            },
            onFolderSelected = { folder ->
                selectedFolderId = folder.id
            },
            onOpenFolderSettings = onOpenFoldersManagement,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
        )
    }
}
