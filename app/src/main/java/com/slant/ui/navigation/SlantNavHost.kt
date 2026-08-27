package com.slant.ui.navigation

import android.widget.Toast
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.slant.ui.screens.auth.AuthScreen
import com.slant.ui.screens.chat.ChatRoomScreen
import com.slant.ui.screens.chat.MessageType
import com.slant.ui.screens.chat.SlantMessage
import com.slant.ui.screens.chat.VoiceAttachment
import com.slant.ui.screens.main.DeliveryStatus
import com.slant.ui.screens.main.SlantChatItem
import com.slant.ui.screens.main.SlantMainScreen
import com.slant.ui.screens.group.GroupChannelInfoScreen
import com.slant.ui.screens.group.GroupMember
import com.slant.ui.screens.media.MediaViewerScreen
import com.slant.ui.screens.notes.SavedNotesScreen
import com.slant.ui.screens.search.SearchScreen
import com.slant.ui.screens.call.CallType
import com.slant.ui.screens.call.SecureCallScreen
import com.slant.ui.screens.profile.PeerProfileScreen
import com.slant.ui.screens.profile.ProfileSecurityScreen
import com.slant.ui.screens.settings.SettingsHubScreen

@Composable
fun SlantNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Auth.route
) {
    val context = LocalContext.current

    // Демо-данные для списка чатов
    val mockChats = remember {
        mutableStateListOf(
            SlantChatItem(
                id = "node_01",
                title = "0xGhost_Relay",
                lastMessage = "Double Ratchet ключ обновлен. Сессия валидна.",
                timestamp = "04:12",
                unreadCount = 2,
                isPinned = true,
                deliveryStatus = DeliveryStatus.READ,
                isP2PDirect = true
            ),
            SlantChatItem(
                id = "node_02",
                title = "Mesh_Local_Cluster",
                lastMessage = "Пакет ретранслирован через 3 хопа.",
                timestamp = "03:45",
                unreadCount = 0,
                isMesh = true,
                meshHopCount = 3,
                deliveryStatus = DeliveryStatus.SENT
            ),
            SlantChatItem(
                id = "node_03",
                title = "CipherVault Core",
                lastMessage = "Байт-в-байт файл (142 MB) доставлен в хранилище.",
                timestamp = "Вчера",
                unreadCount = 0,
                deliveryStatus = DeliveryStatus.READ,
                isP2PDirect = false
            )
        )
    }

    // Демо-сообщения активного диалога
    val mockMessages = remember {
        mutableStateListOf(
            SlantMessage(
                id = "m1",
                senderId = "peer",
                text = "Инициализация защищенного канала завершена. Проверь 4-эмодзи код.",
                timestamp = "04:10",
                isOutgoing = false,
                ratchetStep = 11
            ),
            SlantMessage(
                id = "m2",
                senderId = "me",
                text = "Код совпадает: 🛡️⚡🔒👁️. Переключаюсь на прямой P2P сокет.",
                timestamp = "04:11",
                isOutgoing = true,
                deliveryStatus = DeliveryStatus.READ,
                ratchetStep = 12
            ),
            SlantMessage(
                id = "m3",
                senderId = "peer",
                timestamp = "04:12",
                isOutgoing = false,
                messageType = MessageType.VOICE,
                voiceAttachment = VoiceAttachment(
                    durationSeconds = 8,
                    waveformAmplitudes = listOf(0.2f, 0.5f, 0.8f, 0.4f, 0.9f, 0.6f, 0.3f, 0.7f, 0.5f, 0.8f, 0.3f, 0.6f, 0.9f, 0.4f, 0.2f, 0.7f, 0.5f, 0.8f)
                ),
                ratchetStep = 13
            )
        )
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        enterTransition = { fadeIn(tween(250)) + slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(250)) },
        exitTransition = { fadeOut(tween(200)) },
        popEnterTransition = { fadeIn(tween(250)) },
        popExitTransition = { fadeOut(tween(200)) + slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(250)) }
    ) {
        // 1. Экран авторизации и создания личности
        composable(Screen.Auth.route) {
            AuthScreen(
                onAuthComplete = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Auth.route) { inclusive = true }
                    }
                }
            )
        }

        // 2. Главный экран (Диалоги + Докбар)
        composable(Screen.Main.route) {
            SlantMainScreen(
                chats = mockChats,
                onChatClick = { chat ->
                    navController.navigate(Screen.ChatRoom.createRoute(chat.id, chat.title))
                },
                onProfileClick = {
                    navController.navigate(Screen.Profile.route)
                },
                onQrScanClick = {
                    Toast.makeText(context, "Сканер открытых ключей активен", Toast.LENGTH_SHORT).show()
                }
            )
        }

        // 3. Экран диалога (Chat Room)
        composable(
            route = Screen.ChatRoom.route,
            arguments = listOf(
                navArgument("chatId") { type = NavType.StringType },
                navArgument("peerName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val peerName = backStackEntry.arguments?.getString("peerName") ?: "Unknown Node"

            ChatRoomScreen(
                peerName = peerName,
                messages = mockMessages,
                onBackClick = { navController.popBackStack() },
                onSendMessage = { text ->
                    mockMessages.add(
                        0,
                        SlantMessage(
                            id = "m_${System.currentTimeMillis()}",
                            senderId = "me",
                            text = text,
                            timestamp = "04:14",
                            isOutgoing = true,
                            deliveryStatus = DeliveryStatus.SENT,
                            ratchetStep = mockMessages.size + 1
                        )
                    )
                }
            )
        }

        // 4. Экран настроек системы
        composable(Screen.SettingsHub.route) {
            SettingsHubScreen(
                onBackClick = { navController.popBackStack() },
                onNavigateToSecurity = {
                    navController.navigate(Screen.Profile.route)
                }
            )
        }

        // 5. Экран профиля собеседника
        composable(
            route = Screen.PeerProfile.route,
            arguments = listOf(
                navArgument("peerName") { type = NavType.StringType },
                navArgument("username") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val peerName = backStackEntry.arguments?.getString("peerName") ?: "Node"
            val username = backStackEntry.arguments?.getString("username") ?: "unknown"

            PeerProfileScreen(
                peerName = peerName,
                username = username,
                publicKey = "slant_ed25519_88a7b6c5d4e3f2a10fedcba987654321",
                onBackClick = { navController.popBackStack() },
                onAudioCallClick = {
                    navController.navigate(Screen.SecureCall.createRoute(peerName, "AUDIO"))
                },
                onVideoCallClick = {
                    navController.navigate(Screen.SecureCall.createRoute(peerName, "VIDEO"))
                }
            )
        }

        // 6. Экран E2EE звонка
        composable(
            route = Screen.SecureCall.route,
            arguments = listOf(
                navArgument("peerName") { type = NavType.StringType },
                navArgument("callType") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val peerName = backStackEntry.arguments?.getString("peerName") ?: "Node"
            val callTypeStr = backStackEntry.arguments?.getString("callType") ?: "AUDIO"
            val callType = try {
                CallType.valueOf(callTypeStr)
            } catch (e: Exception) {
                CallType.AUDIO
            }

            SecureCallScreen(
                peerName = peerName,
                callType = callType,
                onEndCall = { navController.popBackStack() }
            )
        }

        // 7. Экран профиля и Anti-Forensics
        composable(Screen.Profile.route) {
            ProfileSecurityScreen(
                publicKey = "slant_ed25519_99a8b7c6d5e4f3a210fedcba98765432",
                onBackClick = { navController.popBackStack() },
                onPanicTrigger = {
                    mockMessages.clear()
                    mockChats.clear()
                    Toast.makeText(context, "Криптографические ключи и ОЗУ занулены", Toast.LENGTH_LONG).show()
                    navController.navigate(Screen.Auth.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // 8. Глобальный и локальный зашифрованный поиск
        composable(Screen.Search.route) {
            SearchScreen(
                onBackClick = { navController.popBackStack() },
                onChatSelect = { chat ->
                    navController.navigate(Screen.ChatRoom.createRoute(chat.id, chat.title))
                }
            )
        }

        // 9. Зашифрованное хранилище / Избранное
        composable(Screen.SavedNotes.route) {
            SavedNotesScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // 10. Информация о группе/канале
        composable(
            route = Screen.GroupInfo.route,
            arguments = listOf(navArgument("groupTitle") { type = NavType.StringType })
        ) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("groupTitle") ?: "Кластер"
            GroupChannelInfoScreen(
                groupTitle = title,
                description = "Закрытый защищенный P2P канал связи. Все сообщения шифруются групповым симметричным ключом и передаются через Mesh-сеть.",
                membersCount = 5,
                members = listOf(
                    GroupMember("1", "Admin_Root", "Создатель", true),
                    GroupMember("2", "0xGhost_Relay", "Узел ретрансляции", true),
                    GroupMember("3", "Cipher_Node_9", "Участник", false),
                    GroupMember("4", "Black_Ice", "Участник", true),
                    GroupMember("5", "Neural_Agent", "Бот/ИИ", true)
                ),
                onBackClick = { navController.popBackStack() },
                onCreateInviteLink = {
                    Toast.makeText(context, "Zero-Knowledge инвайт скопирован", Toast.LENGTH_SHORT).show()
                },
                onLeaveGroup = {
                    navController.popBackStack()
                }
            )
        }

        // 11. Полноэкранный просмотр медиа
        composable(
            route = Screen.MediaViewer.route,
            arguments = listOf(
                navArgument("fileName") { type = NavType.StringType },
                navArgument("fileSize") { type = NavType.StringType },
                navArgument("senderName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val fileName = backStackEntry.arguments?.getString("fileName") ?: "encrypted_media.raw"
            val fileSize = backStackEntry.arguments?.getString("fileSize") ?: "12.4 MB"
            val senderName = backStackEntry.arguments?.getString("senderName") ?: "Node"

            MediaViewerScreen(
                fileName = fileName,
                fileSize = fileSize,
                senderName = senderName,
                timestamp = "Сегодня в 14:20",
                onBackClick = { navController.popBackStack() },
                onExportByteForByte = {
                    Toast.makeText(context, "Экспорт Raw байт-в-байт завершен", Toast.LENGTH_SHORT).show()
                },
                onDeleteLocally = {
                    Toast.makeText(context, "Медиафайл удален из ОЗУ и кэша", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }
            )
        }
    }
}
