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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.DoneAll
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.PushPin
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slant.ui.theme.SlantAppStateManager
import com.slant.ui.theme.SlantDimText
import com.slant.ui.theme.SlantOledBlack
import com.slant.ui.theme.SlantPureWhite
import com.slant.ui.theme.liquidGlass

@Composable
fun CompactGlassChatItem(
    chat: SlantChatItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = SlantAppStateManager.isDark
    val titleColor = if (isDark) SlantPureWhite else MaterialTheme.colorScheme.onBackground
    val subtitleColor = if (isDark) SlantDimText else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f)
    val readCheckColor = if (isDark) SlantPureWhite else MaterialTheme.colorScheme.primary
    val unreadBadgeBg = if (isDark) SlantPureWhite else MaterialTheme.colorScheme.primary
    val unreadBadgeText = if (isDark) SlantOledBlack else MaterialTheme.colorScheme.onPrimary

    Row(
        modifier = modifier
            .fillMaxWidth()
            .liquidGlass(
                shape = RoundedCornerShape(20.dp),
                alpha = 0.55f,
                borderWidth = 0.6.dp
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 10.dp)
            .testTag("chat_item_${chat.id}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Аватар узла с E2EE / Mesh бейджем
        Box(
            modifier = Modifier.size(44.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(if (isDark) Color(0x33FFFFFF) else Color(0x1F000000)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = chat.avatarInitials,
                    color = titleColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            if (chat.isP2PDirect || chat.isMesh) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(if (isDark) SlantOledBlack else SlantPureWhite)
                        .padding(1.5.dp)
                        .clip(CircleShape)
                        .background(if (chat.isMesh) Color(0xFF00E5FF) else titleColor)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            // Верхняя строка: Имя, замок E2EE и время
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = chat.title,
                        color = titleColor,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.5.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (chat.isP2PDirect) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Rounded.Lock,
                            contentDescription = "P2P",
                            tint = subtitleColor,
                            modifier = Modifier.size(11.dp)
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    when (chat.deliveryStatus) {
                        DeliveryStatus.SENDING -> Icon(Icons.Rounded.Schedule, null, tint = subtitleColor, modifier = Modifier.size(12.dp))
                        DeliveryStatus.SENT -> Icon(Icons.Rounded.Done, null, tint = subtitleColor, modifier = Modifier.size(12.dp))
                        DeliveryStatus.READ -> Icon(Icons.Rounded.DoneAll, null, tint = readCheckColor, modifier = Modifier.size(12.dp))
                    }
                    Text(
                        text = chat.timestamp,
                        color = subtitleColor,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(3.dp))

            // Нижняя строка: Текст сообщения, статус пина и бейдж счетчика
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = chat.lastMessage,
                    color = subtitleColor,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (chat.isPinned) {
                        Icon(
                            imageVector = Icons.Rounded.PushPin,
                            contentDescription = "Pinned",
                            tint = subtitleColor,
                            modifier = Modifier.size(12.dp)
                        )
                    }

                    if (chat.unreadCount > 0) {
                        Box(
                            modifier = Modifier
                                .height(18.dp)
                                .clip(RoundedCornerShape(9.dp))
                                .background(if (chat.isMuted) (if (isDark) Color(0x33FFFFFF) else Color(0x22000000)) else unreadBadgeBg)
                                .padding(horizontal = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = chat.unreadCount.toString(),
                                color = if (chat.isMuted) titleColor else unreadBadgeText,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
