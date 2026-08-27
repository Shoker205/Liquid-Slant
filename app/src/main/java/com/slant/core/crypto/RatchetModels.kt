package com.slant.core.crypto

data class KeyPair(
    val publicKey: ByteArray,
    val privateKey: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as KeyPair
        return publicKey.contentEquals(other.publicKey) && privateKey.contentEquals(other.privateKey)
    }

    override fun hashCode(): Int = 31 * publicKey.contentHashCode() + privateKey.contentHashCode()
}

/**
 * Заголовок сообщения, передаваемый открыто (не содержит конфиденциальных данных)
 */
data class MessageHeader(
    val dhPublicKey: ByteArray,
    val sequenceNumber: Int,
    val previousChainLength: Int
) {
    fun toByteArray(): ByteArray {
        val buffer = java.nio.ByteBuffer.allocate(dhPublicKey.size + 8)
        buffer.put(dhPublicKey)
        buffer.putInt(sequenceNumber)
        buffer.putInt(previousChainLength)
        return buffer.array()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as MessageHeader
        return dhPublicKey.contentEquals(other.dhPublicKey) &&
                sequenceNumber == other.sequenceNumber &&
                previousChainLength == other.previousChainLength
    }

    override fun hashCode(): Int {
        var result = dhPublicKey.contentHashCode()
        result = 31 * result + sequenceNumber
        result = 31 * result + previousChainLength
        return result
    }
}

data class EncryptedEnvelope(
    val header: MessageHeader,
    val ciphertext: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as EncryptedEnvelope
        return header == other.header && ciphertext.contentEquals(other.ciphertext)
    }

    override fun hashCode(): Int {
        var result = header.hashCode()
        result = 31 * result + ciphertext.contentHashCode()
        return result
    }
}
