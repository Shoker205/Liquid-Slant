package com.slant.ui.screens.main

import android.view.HapticFeedbackConstants
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material.icons.rounded.FolderOpen
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slant.ui.models.ChatFolder
import com.slant.ui.theme.SlantAppStateManager
import com.slant.ui.theme.SlantDimText
import com.slant.ui.theme.SlantGlassBase
import com.slant.ui.theme.SlantOledBlack
import com.slant.ui.theme.SlantPureWhite
import com.slant.ui.theme.SlantStrings
import com.slant.ui.theme.liquidGlass
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SlantDockWithFolderPicker(
    currentSection: MainDockSection,
    folders: List<ChatFolder>,
    selectedFolderId: String,
    onSectionSelected: (MainDockSection) -> Unit,
    onFolderSelected: (ChatFolder) -> Unit,
    onOpenFolderSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current
    val sections = MainDockSection.entries.toTypedArray()
    val selectedIndex = sections.indexOf(currentSection).coerceAtLeast(0)
    val density = LocalDensity.current

    var isFolderPopupVisible by remember { mutableStateOf(false) }

    val animatedIndex = remember { Animatable(selectedIndex.toFloat()) }
    val dropletStretch = remember { Animatable(1f) }

    LaunchedEffect(selectedIndex) {
        dropletStretch.animateTo(
            targetValue = 1.25f,
            animationSpec = spring(dampingRatio = 0.55f, stiffness = Spring.StiffnessHigh)
        )
        dropletStretch.animateTo(
            targetValue = 1f,
            animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessMediumLow)
        )
    }

    LaunchedEffect(selectedIndex) {
        animatedIndex.animateTo(
            targetValue = selectedIndex.toFloat(),
            animationSpec = spring(dampingRatio = 0.70f, stiffness = Spring.StiffnessLow)
        )
    }

    val isDark = SlantAppStateManager.isDark
    val isMonet = SlantAppStateManager.isMonet
    val activePillColor = when {
        isMonet -> MaterialTheme.colorScheme.primary
        isDark -> SlantPureWhite
        else -> SlantOledBlack
    }
    val activeIconColor = when {
        isMonet -> MaterialTheme.colorScheme.onPrimary
        isDark -> SlantOledBlack
        else -> SlantPureWhite
    }
    val inactiveIconColor = if (isDark) SlantDimText else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Всплывающее меню выбора папок при удержании
        AnimatedVisibility(
            visible = isFolderPopupVisible,
            enter = fadeIn() + slideInVertically { it / 2 },
            exit = fadeOut() + slideOutVertically { it / 2 }
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 6.dp)
                    .liquidGlass(
                        shape = RoundedCornerShape(22.dp),
                        alpha = 0.92f,
                        borderWidth = 1.dp
                    )
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = SlantStrings.chatFoldersHeader,
                        color = inactiveIconColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        modifier = Modifier.padding(start = 6.dp)
                    )
                    Icon(
                        imageVector = Icons.Rounded.Settings,
                        contentDescription = "Настроить папки",
                        tint = if (isDark) SlantPureWhite else SlantOledBlack,
                        modifier = Modifier
                            .size(17.dp)
                            .clickable {
                                isFolderPopupVisible = false
                                onOpenFolderSettings()
                            }
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                folders.forEach { folder ->
                    val isSelected = folder.id == selectedFolderId
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                if (isSelected) {
                                    if (isDark) Color(0x33FFFFFF) else Color(0x22000000)
                                } else Color.Transparent
                            )
                            .clickable {
                                view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
                                onFolderSelected(folder)
                                isFolderPopupVisible = false
                            }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isSelected) Icons.Rounded.FolderOpen else Icons.Rounded.Folder,
                                contentDescription = null,
                                tint = if (isSelected) (if (isDark) SlantPureWhite else SlantOledBlack) else inactiveIconColor,
                                modifier = Modifier.size(17.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = folder.name,
                                color = if (isSelected) (if (isDark) SlantPureWhite else SlantOledBlack) else inactiveIconColor,
                                fontSize = 12.5.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }

                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Rounded.Check,
                                contentDescription = null,
                                tint = if (isDark) SlantPureWhite else SlantOledBlack,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }
                }
            }
        }

        // Основной капсульный докбар
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 42.dp, vertical = 6.dp)
                .height(50.dp)
                .liquidGlass(
                    shape = RoundedCornerShape(25.dp),
                    alpha = 0.80f,
                    borderWidth = 1.dp
                )
                .padding(horizontal = 4.dp, vertical = 4.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val totalWidthPx = with(density) { maxWidth.toPx() }
                val itemWidthPx = totalWidthPx / sections.size
                val itemWidthDp = maxWidth / sections.size

                val currentOffsetPx = animatedIndex.value * itemWidthPx
                val indicatorWidthDp = (itemWidthDp * dropletStretch.value).coerceAtMost(itemWidthDp * 1.3f)

                // Перетекающая капля
                Box(
                    modifier = Modifier
                        .offset {
                            val centeredOffset = currentOffsetPx + (itemWidthPx - with(density) { indicatorWidthDp.toPx() }) / 2
                            IntOffset(centeredOffset.roundToInt(), 0)
                        }
                        .width(indicatorWidthDp)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(20.dp))
                        .background(activePillColor)
                )

                // Иконки
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    sections.forEachIndexed { _, section ->
                        val isSelected = section == currentSection

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(20.dp))
                                .combinedClickable(
                                    onClick = {
                                        if (isFolderPopupVisible) isFolderPopupVisible = false
                                        if (!isSelected) {
                                            view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                                            onSectionSelected(section)
                                        }
                                    },
                                    onLongClick = {
                                        if (section == MainDockSection.CHATS || section == MainDockSection.GROUPS || section == MainDockSection.CHANNELS) {
                                            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                                            isFolderPopupVisible = !isFolderPopupVisible
                                        }
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = section.icon,
                                contentDescription = section.localizedTitle,
                                tint = if (isSelected) activeIconColor else inactiveIconColor,
                                modifier = Modifier.size(19.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
