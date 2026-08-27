package com.slant.ui.screens.search

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.History
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
import com.slant.ui.components.SlantGlassTextField
import com.slant.ui.screens.main.SlantChatItem
import com.slant.ui.theme.SlantDimText
import com.slant.ui.theme.SlantGlassBase
import com.slant.ui.theme.SlantOledBlack
import com.slant.ui.theme.SlantPureWhite
import com.slant.ui.theme.liquidGlass

@Composable
fun SearchScreen(
    onBackClick: () -> Unit,
    onChatSelect: (SlantChatItem) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Все", "Чаты", "Медиа", "Файлы", "Ссылки")

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SlantOledBlack)
            .testTag("search_screen")
    ) {
        LivingNeuralCanvas(
            state = if (searchQuery.isNotEmpty()) NeuralState.USER else NeuralState.IDLE,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.18f)
                .align(Alignment.Center)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // Строка поиска
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.testTag("search_back_button")
                ) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Back", tint = SlantPureWhite)
                }
                Spacer(modifier = Modifier.width(4.dp))
                SlantGlassTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = "ПОИСК СООБЩЕНИЙ И УЗЛОВ...",
                    leadingIcon = Icons.Rounded.Search,
                    modifier = Modifier.weight(1f)
                )
            }

            // Категории поиска
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                contentColor = SlantPureWhite,
                edgePadding = 16.dp,
                divider = {},
                indicator = {}
            ) {
                tabs.forEachIndexed { index, title ->
                    val isSelected = selectedTab == index
                    Tab(
                        selected = isSelected,
                        onClick = { selectedTab = index },
                        modifier = Modifier
                            .padding(vertical = 4.dp, horizontal = 4.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .then(
                                if (isSelected) Modifier.liquidGlass(RoundedCornerShape(14.dp), backgroundColor = Color(0x66FFFFFF), alpha = 0.25f)
                                else Modifier
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = title.uppercase(),
                            fontSize = 10.5.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) SlantPureWhite else SlantDimText
                        )
                    }
                }
            }

            // Результаты или история недавних запросов
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (searchQuery.isEmpty()) {
                    item {
                        Text(
                            text = "НЕДАВНИЕ ЗАПРОСЫ (ЛОКАЛЬНО)",
                            color = SlantDimText,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp,
                            modifier = Modifier.padding(start = 4.dp, top = 12.dp)
                        )
                    }

                    val recentQueries = listOf("0xGhost_Relay", "Raw Video Note", "Double Ratchet Keys")
                    items(recentQueries) { query ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .liquidGlass(RoundedCornerShape(16.dp), backgroundColor = SlantGlassBase, alpha = 0.45f)
                                .clickable { searchQuery = query }
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Rounded.History, null, tint = SlantDimText, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(query, color = SlantPureWhite, fontSize = 13.sp)
                            }
                            Icon(Icons.Rounded.Close, null, tint = SlantDimText, modifier = Modifier.size(14.dp))
                        }
                    }
                } else {
                    item {
                        Text(
                            text = "НАЙДЕНО В ЗАШИФРОВАННОМ ИНДЕКСЕ",
                            color = SlantDimText,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp,
                            modifier = Modifier.padding(start = 4.dp, top = 12.dp)
                        )
                    }

                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .liquidGlass(RoundedCornerShape(18.dp), backgroundColor = SlantGlassBase, alpha = 0.55f)
                                .padding(14.dp)
                        ) {
                            Column {
                                Text("0xGhost_Relay", color = SlantPureWhite, fontWeight = FontWeight.Bold, fontSize = 13.5.sp)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text("...Double Ratchet ключ обновлен. Сессия валидна...", color = SlantDimText, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
