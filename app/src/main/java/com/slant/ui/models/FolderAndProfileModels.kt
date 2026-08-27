package com.slant.ui.models

import androidx.compose.ui.graphics.vector.ImageVector

data class ChatFolder(
    val id: String,
    val name: String,
    val icon: String = "folder",
    val includedTypes: Set<FolderFilterType> = emptySet(),
    val includedChatIds: Set<String> = emptySet(),
    val excludedChatIds: Set<String> = emptySet(),
    val isDefault: Boolean = false
)

enum class FolderFilterType(val title: String) {
    CONTACTS("Контакты"),
    NON_CONTACTS("Не контакты"),
    GROUPS("Группы"),
    CHANNELS("Каналы"),
    BOTS("Боты"),
    UNREAD("Непрочитанные"),
    MUTED("Без звука"),
    P2P_ONLY("Только P2P"),
    MESH_ONLY("Только Mesh")
}

data class ProfileMenuItem(
    val id: String,
    val title: String,
    val subtitle: String? = null,
    val badge: String? = null,
    val icon: ImageVector,
    val iconBackgroundAlpha: Float = 0.25f,
    val isDanger: Boolean = false,
    val onClick: () -> Unit
)
