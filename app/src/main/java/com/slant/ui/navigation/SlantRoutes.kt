package com.slant.ui.navigation

import android.net.Uri

sealed class Screen(val route: String) {
    data object Auth : Screen("auth_screen")
    data object Main : Screen("main_screen")
    data object Profile : Screen("profile_screen")
    data object SettingsHub : Screen("settings_hub_screen")
    data object Search : Screen("search_screen")
    data object SavedNotes : Screen("saved_notes_screen")
    data object GroupInfo : Screen("group_info/{groupTitle}") {
        fun createRoute(groupTitle: String): String {
            val enc = Uri.encode(groupTitle)
            return "group_info/$enc"
        }
    }
    data object MediaViewer : Screen("media_viewer/{fileName}/{fileSize}/{senderName}") {
        fun createRoute(fileName: String, fileSize: String, senderName: String): String {
            val encFile = Uri.encode(fileName)
            val encSize = Uri.encode(fileSize)
            val encSender = Uri.encode(senderName)
            return "media_viewer/$encFile/$encSize/$encSender"
        }
    }
    data object ChatRoom : Screen("chat_room/{chatId}/{peerName}") {
        fun createRoute(chatId: String, peerName: String): String {
            val encodedName = Uri.encode(peerName)
            return "chat_room/$chatId/$encodedName"
        }
    }
    data object PeerProfile : Screen("peer_profile/{peerName}/{username}") {
        fun createRoute(peerName: String, username: String): String {
            val encName = Uri.encode(peerName)
            val encUser = Uri.encode(username)
            return "peer_profile/$encName/$encUser"
        }
    }
    data object SecureCall : Screen("secure_call/{peerName}/{callType}") {
        fun createRoute(peerName: String, callType: String = "AUDIO"): String {
            val encName = Uri.encode(peerName)
            return "secure_call/$encName/$callType"
        }
    }
}
