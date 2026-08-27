package com.slant.ui.screens.main

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.QrCodeScanner
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slant.ui.components.LivingNeuralCanvas
import com.slant.ui.components.NeuralState
import com.slant.ui.theme.SlantDimText
import com.slant.ui.theme.SlantGlassBase
import com.slant.ui.theme.SlantOledBlack
import com.slant.ui.theme.SlantPureWhite
import com.slant.ui.theme.liquidGlass

@Composable
fun SlantMainScreen(
    chats: List<SlantChatItem>,
    onChatClick: (SlantChatItem) -> Unit,
    onProfileClick: () -> Unit,
    onQrScanClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategoryIndex by remember { mutableIntStateOf(0) }
    var currentDockSection by remember { mutableStateOf(MainDockSection.CHATS) }
    val categories = listOf("Все", "P2P", "Mesh", "Зашифрованные", "Каналы")

    val filteredChats = remember(selectedCategoryIndex, chats) {
        when (selectedCategoryIndex) {
            1 -> chats.filter { it.isP2PDirect }
            2 -> chats.filter { it.isMesh }
            3 -> chats.filter { it.isP2PDirect || it.isMesh }
            4 -> chats.filter { it.id.startsWith("channel") }
            else -> chats
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SlantOledBlack)
            .testTag("slant_main_screen")
    ) {
        // Фоновый живой маскот
        LivingNeuralCanvas(
            state = NeuralState.IDLE,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.20f)
                .align(Alignment.Center)
        )

        // Контент поверх фона
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Верхняя панель TopBar (Material 3 Liquid Glass)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .liquidGlass(
                        shape = RoundedCornerShape(24.dp),
                        backgroundColor = SlantGlassBase,
                        alpha = 0.65f,
                        borderWidth = 1.dp
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .testTag("main_top_bar"),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Color(0x33FFFFFF))
                            .clickable { onProfileClick() }
                            .testTag("main_profile_avatar"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "SL",
                            color = SlantPureWhite,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "SLANT",
                        color = SlantPureWhite,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onQrScanClick,
                        modifier = Modifier.testTag("main_qr_scan_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.QrCodeScanner,
                            contentDescription = "Scan Node QR",
                            tint = SlantPureWhite,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    IconButton(
                        onClick = {},
                        modifier = Modifier.testTag("main_search_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = "Search",
                            tint = SlantPureWhite,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Фильтры категорий
            ScrollableTabRow(
                selectedTabIndex = selectedCategoryIndex,
                containerColor = Color.Transparent,
                contentColor = SlantPureWhite,
                edgePadding = 16.dp,
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
                            .padding(vertical = 6.dp, horizontal = 4.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .then(
                                if (isSelected) Modifier.liquidGlass(
                                    shape = RoundedCornerShape(16.dp),
                                    backgroundColor = Color(0x66FFFFFF),
                                    alpha = 0.25f
                                ) else Modifier
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .testTag("category_tab_$index")
                    ) {
                        Text(
                            text = title.uppercase(),
                            fontSize = 10.5.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            letterSpacing = 0.8.sp,
                            color = if (isSelected) SlantPureWhite else SlantDimText
                        )
                    }
                }
            }

            // Список чатов
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp)
                    .testTag("chats_lazy_column"),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                item { Spacer(modifier = Modifier.height(4.dp)) }

                items(filteredChats, key = { it.id }) { chat ->
                    CompactGlassChatItem(
                        chat = chat,
                        onClick = { onChatClick(chat) }
                    )
                }

                item { Spacer(modifier = Modifier.height(96.dp)) } // Отступ под парящий докбар
            }
        }

        // Парящий прозрачный докбар
        LiquidCapsuleDock(
            currentSection = currentDockSection,
            onSectionSelected = { currentDockSection = it },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
        )
    }
}
