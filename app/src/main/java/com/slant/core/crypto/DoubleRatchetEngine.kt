package com.slant.core.crypto

import java.nio.charset.StandardCharsets

class DoubleRatchetEngine(
    private var rootKey: ByteArray,
    private var localDhPair: KeyPair,
    private var remoteDhPublic: ByteArray? = null
) {
    private var sendingChainKey: ByteArray? = null
    private var receivingChainKey: ByteArray? = null
    private var sequenceNumberSend = 0
    private var sequenceNumberReceive = 0
    private var previousChainLength = 0

    private val infoKdfRk = "Slant_RK_Step".toByteArray(StandardCharsets.UTF_8)
    private val infoKdfCk = "Slant_CK_Step".toByteArray(StandardCharsets.UTF_8)
    private val infoMsgKey = "Slant_MK_Derive".toByteArray(StandardCharsets.UTF_8)

    /**
     * KDF для симметричной цепи: производит следующий Chain Key и одноразовый Message Key
     */
    private fun kdfCk(chainKey: ByteArray): Pair<ByteArray, ByteArray> {
        val derived = CryptoEngine.hkdf(
            inputKeyMaterial = chainKey,
            info = infoKdfCk,
            outputLength = 64
        )
        val nextChainKey = derived.copyOfRange(0, 32)
        val messageKey = derived.copyOfRange(32, 64)
        CryptoEngine.zeroize(derived)
        return Pair(nextChainKey, messageKey)
    }

    /**
     * Асимметричный шаг храповика (DH Ratchet Step)
     */
    private fun dhRatchetStep(remotePublicKey: ByteArray) {
        previousChainLength = sequenceNumberSend
        sequenceNumberSend = 0
        sequenceNumberReceive = 0
        remoteDhPublic = remotePublicKey

        // Имитация Shared Secret (В реальной связке X25519: Curve25519.calculateAgreement)
        val sharedSecret = CryptoEngine.hkdf(
            inputKeyMaterial = localDhPair.privateKey + remotePublicKey,
            info = "Slant_DH_Shared".toByteArray(StandardCharsets.UTF_8),
            outputLength = 32
        )

        // Обновление Root Key и получение Chain Key на прием
        val rkDerivedRecv = CryptoEngine.hkdf(
            inputKeyMaterial = sharedSecret,
            salt = rootKey,
            info = infoKdfRk,
            outputLength = 64
        )
        rootKey = rkDerivedRecv.copyOfRange(0, 32)
        receivingChainKey = rkDerivedRecv.copyOfRange(32, 64)

        // Генерация новой локальной пары и вычисление отправляющей цепи
        val newPrivateKey = CryptoEngine.randomBytes(32)
        val newPublicKey = CryptoEngine.hkdf(newPrivateKey, info = "Slant_PubKey".toByteArray(StandardCharsets.UTF_8), outputLength = 32)
        localDhPair = KeyPair(newPublicKey, newPrivateKey)

        val newSharedSecret = CryptoEngine.hkdf(
            inputKeyMaterial = localDhPair.privateKey + remotePublicKey,
            info = "Slant_DH_Shared".toByteArray(StandardCharsets.UTF_8),
            outputLength = 32
        )

        val rkDerivedSend = CryptoEngine.hkdf(
            inputKeyMaterial = newSharedSecret,
            salt = rootKey,
            info = infoKdfRk,
            outputLength = 64
        )
        rootKey = rkDerivedSend.copyOfRange(0, 32)
        sendingChainKey = rkDerivedSend.copyOfRange(32, 64)

        CryptoEngine.zeroize(sharedSecret, newSharedSecret, rkDerivedRecv, rkDerivedSend)
    }

    /**
     * Шифрование исходящего сообщения
     */
    @Synchronized
    fun encrypt(plaintext: ByteArray): EncryptedEnvelope {
        if (sendingChainKey == null && remoteDhPublic != null) {
            dhRatchetStep(remoteDhPublic!!)
        }

        checkNotNull(sendingChainKey) { "Ratchet sending chain is uninitialized" }

        val (nextCk, messageKey) = kdfCk(sendingChainKey!!)
        sendingChainKey = nextCk

        val header = MessageHeader(
            dhPublicKey = localDhPair.publicKey,
            sequenceNumber = sequenceNumberSend++,
            previousChainLength = previousChainLength
        )

        val ciphertext = CryptoEngine.encryptAesGcm(
            key = messageKey,
            plaintext = plaintext,
            associatedData = header.toByteArray()
        )

        CryptoEngine.zeroize(messageKey)
        return EncryptedEnvelope(header, ciphertext)
    }

    /**
     * Расшифровка входящего пакета
     */
    @Synchronized
    fun decrypt(envelope: EncryptedEnvelope): ByteArray {
        val header = envelope.header

        // Если получен новый публичный ключ, совершаем шаг DH-храповика
        if (remoteDhPublic == null || !remoteDhPublic!!.contentEquals(header.dhPublicKey)) {
            dhRatchetStep(header.dhPublicKey)
        }

        checkNotNull(receivingChainKey) { "Ratchet receiving chain is uninitialized" }

        val (nextCk, messageKey) = kdfCk(receivingChainKey!!)
        receivingChainKey = nextCk
        sequenceNumberReceive++

        val plaintext = CryptoEngine.decryptAesGcm(
            key = messageKey,
            encryptedData = envelope.ciphertext,
            associatedData = header.toByteArray()
        )

        CryptoEngine.zeroize(messageKey)
        return plaintext
    }

    fun destroy() {
        CryptoEngine.zeroize(rootKey, localDhPair.privateKey, sendingChainKey, receivingChainKey)
    }
}
