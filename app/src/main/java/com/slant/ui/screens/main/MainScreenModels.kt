package com.slant.ui.screens.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChatBubbleOutline
import androidx.compose.material.icons.rounded.Forum
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.PersonOutline
import androidx.compose.material.icons.rounded.SmartToy
import androidx.compose.ui.graphics.vector.ImageVector

enum class MainDockSection(val title: String, val icon: ImageVector) {
    CHATS("Чаты", Icons.Rounded.ChatBubbleOutline),
    GROUPS("Группы", Icons.Rounded.Forum),
    CHANNELS("Каналы", Icons.Rounded.GraphicEq),
    BOTS("Боты", Icons.Rounded.SmartToy),
    PROFILE("Профиль", Icons.Rounded.PersonOutline)
}

enum class DeliveryStatus {
    SENDING, SENT, READ
}

data class SlantChatItem(
    val id: String,
    val title: String,
    val lastMessage: String,
    val timestamp: String,
    val unreadCount: Int = 0,
    val isPinned: Boolean = false,
    val isMuted: Boolean = false,
    val deliveryStatus: DeliveryStatus = DeliveryStatus.READ,
    val isP2PDirect: Boolean = false,
    val isMesh: Boolean = false,
    val meshHopCount: Int? = null,
    val avatarInitials: String = title.take(2).uppercase()
)
