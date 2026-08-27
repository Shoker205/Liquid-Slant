package com.slant.ui.screens.chat

import androidx.lifecycle.viewModelScope
import com.slant.ui.base.BaseViewModel
import com.slant.ui.base.UiEffect
import com.slant.ui.base.UiEvent
import com.slant.ui.base.UiState
import com.slant.ui.screens.main.DeliveryStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class ChatRoomUiState(
    val peerName: String = "",
    val connectionStatus: String = "P2P Direct • Ratchet Active",
    val messages: List<SlantMessage> = emptyList(),
    val inputText: String = "",
    val isRecordingVoice: Boolean = false,
    val verificationEmojis: List<String> = listOf("🛡️", "⚡", "🔒", "👁️"),
    val currentRatchetIndex: Int = 14
) : UiState

sealed interface ChatRoomUiEvent : UiEvent {
    data class InputTextChanged(val text: String) : ChatRoomUiEvent
    data object SendTextMessage : ChatRoomUiEvent
    data class SendVoiceMessage(val duration: Int) : ChatRoomUiEvent
    data object RequestKeyVerification : ChatRoomUiEvent
}

sealed interface ChatRoomUiEffect : UiEffect {
    data object PlaySendHaptic : ChatRoomUiEffect
    data class ShowToast(val msg: String) : ChatRoomUiEffect
}

class ChatRoomViewModel(
    peerName: String
) : BaseViewModel<ChatRoomUiState, ChatRoomUiEvent, ChatRoomUiEffect>(
    ChatRoomUiState(peerName = peerName)
) {

    init {
        loadInitialMessages()
    }

    override fun handleEvent(event: ChatRoomUiEvent) {
        when (event) {
            is ChatRoomUiEvent.InputTextChanged -> {
                updateState { copy(inputText = event.text) }
            }

            is ChatRoomUiEvent.SendTextMessage -> {
                val text = currentState.inputText.trim()
                if (text.isEmpty()) return

                val newIndex = currentState.currentRatchetIndex + 1
                val newMessage = SlantMessage(
                    id = "msg_${System.currentTimeMillis()}",
                    senderId = "me",
                    text = text,
                    timestamp = "04:20",
                    isOutgoing = true,
                    deliveryStatus = DeliveryStatus.SENDING,
                    ratchetStep = newIndex
                )

                updateState {
                    copy(
                        messages = listOf(newMessage) + messages,
                        inputText = "",
                        currentRatchetIndex = newIndex
                    )
                }
                sendEffect(ChatRoomUiEffect.PlaySendHaptic)

                // Имитация подтверждения доставки пакета через WebSocket / P2P сокет
                viewModelScope.launch {
                    delay(600)
                    updateState {
                        copy(
                            messages = messages.map {
                                if (it.id == newMessage.id) it.copy(deliveryStatus = DeliveryStatus.READ) else it
                            }
                        )
                    }
                }
            }

            is ChatRoomUiEvent.SendVoiceMessage -> {
                val newIndex = currentState.currentRatchetIndex + 1
                val voiceMsg = SlantMessage(
                    id = "voice_${System.currentTimeMillis()}",
                    senderId = "me",
                    timestamp = "04:21",
                    isOutgoing = true,
                    messageType = MessageType.VOICE,
                    voiceAttachment = VoiceAttachment(
                        durationSeconds = event.duration,
                        waveformAmplitudes = listOf(0.3f, 0.6f, 0.9f, 0.4f, 0.7f, 0.2f, 0.8f, 0.5f, 0.9f)
                    ),
                    deliveryStatus = DeliveryStatus.SENT,
                    ratchetStep = newIndex
                )

                updateState {
                    copy(
                        messages = listOf(voiceMsg) + messages,
                        currentRatchetIndex = newIndex
                    )
                }
                sendEffect(ChatRoomUiEffect.PlaySendHaptic)
            }

            is ChatRoomUiEvent.RequestKeyVerification -> {
                sendEffect(ChatRoomUiEffect.ShowToast("Сверьте 4 эмодзи с собеседником при личной встрече"))
            }
        }
    }

    private fun loadInitialMessages() {
        val initial = listOf(
            SlantMessage(
                id = "init_1",
                senderId = "peer",
                text = "Сессия инициализирована через X3DH. Double Ratchet активен.",
                timestamp = "04:15",
                isOutgoing = false,
                ratchetStep = 13
            )
        )
        updateState { copy(messages = initial) }
    }
}
