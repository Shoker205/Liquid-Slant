package com.slant.core.crypto

import java.security.MessageDigest

object SafetyFingerprint {

    private val EMOJI_ALPHABET = listOf(
        "🛡️", "⚡", "🔒", "👁️", "🌌", "🔑", "🪐", "💎",
        "🛰️", "⚙️", "🔮", "🧬", "🌊", "🕯️", "☄️", "⚓"
    )

    /**
     * Детерминированный маппинг открытых ключей обоих собеседников в 4 эмодзи
     */
    fun compute4EmojiCode(localPublicKey: ByteArray, remotePublicKey: ByteArray): List<String> {
        // Лексикографическая сортировка для одинакового результата на обоих устройствах
        val sortedKeys = if (compareByteArrays(localPublicKey, remotePublicKey) < 0) {
            localPublicKey + remotePublicKey
        } else {
            remotePublicKey + localPublicKey
        }

        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(sortedKeys)

        val emojis = mutableListOf<String>()
        for (i in 0 until 4) {
            val byteValue = hash[i].toInt() and 0xFF
            val emojiIndex = byteValue % EMOJI_ALPHABET.size
            emojis.add(EMOJI_ALPHABET[emojiIndex])
        }

        return emojis
    }

    private fun compareByteArrays(a: ByteArray, b: ByteArray): Int {
        val minLen = minOf(a.size, b.size)
        for (i in 0 until minLen) {
            val byteA = a[i].toInt() and 0xFF
            val byteB = b[i].toInt() and 0xFF
            if (byteA != byteB) return byteA - byteB
        }
        return a.size - b.size
    }
}
