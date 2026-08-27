package com.slant.ui.screens.chat

import com.slant.ui.screens.main.DeliveryStatus

enum class MessageType {
    TEXT, VOICE, FILE
}

data class FileAttachment(
    val fileName: String,
    val fileSizeFormatted: String,
    val mimeType: String,
    val isRawByteUncompressed: Boolean = true
)

data class VoiceAttachment(
    val durationSeconds: Int,
    val waveformAmplitudes: List<Float>,
    val isPlaying: Boolean = false,
    val playbackProgress: Float = 0f
)

data class SlantMessage(
    val id: String,
    val senderId: String,
    val text: String? = null,
    val timestamp: String,
    val isOutgoing: Boolean,
    val deliveryStatus: DeliveryStatus = DeliveryStatus.READ,
    val messageType: MessageType = MessageType.TEXT,
    val fileAttachment: FileAttachment? = null,
    val voiceAttachment: VoiceAttachment? = null,
    val isDoubleRatchetSecured: Boolean = true,
    val ratchetStep: Int = 1
)
