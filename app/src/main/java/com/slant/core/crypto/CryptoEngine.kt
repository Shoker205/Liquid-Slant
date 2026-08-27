package com.slant.core.crypto

import java.security.SecureRandom
import java.util.Arrays
import javax.crypto.Cipher
import javax.crypto.Mac
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

object CryptoEngine {
    private const val HMAC_ALGO = "HmacSHA256"
    private const val AES_GCM_ALGO = "AES/GCM/NoPadding"
    private const val GCM_TAG_LENGTH_BITS = 128
    private const val GCM_IV_LENGTH_BYTES = 12

    private val secureRandom = SecureRandom()

    fun randomBytes(size: Int): ByteArray {
        val bytes = ByteArray(size)
        secureRandom.nextBytes(bytes)
        return bytes
    }

    /**
     * Гарантированное зануление чувствительных байтов в оперативной памяти
     */
    fun zeroize(vararg arrays: ByteArray?) {
        for (arr in arrays) {
            if (arr != null) {
                Arrays.fill(arr, 0.toByte())
            }
        }
    }

    /**
     * HKDF (HMAC-based Extract-and-Expand Key Derivation Function, RFC 5869)
     */
    fun hkdf(
        inputKeyMaterial: ByteArray,
        salt: ByteArray = ByteArray(32),
        info: ByteArray = ByteArray(0),
        outputLength: Int = 64
    ): ByteArray {
        // Step 1: Extract
        val macExtract = Mac.getInstance(HMAC_ALGO)
        macExtract.init(SecretKeySpec(salt, HMAC_ALGO))
        val prk = macExtract.doFinal(inputKeyMaterial)

        // Step 2: Expand
        val macExpand = Mac.getInstance(HMAC_ALGO)
        macExpand.init(SecretKeySpec(prk, HMAC_ALGO))

        val result = ByteArray(outputLength)
        var t = ByteArray(0)
        var generatedBytes = 0
        var counter = 1.toByte()

        try {
            while (generatedBytes < outputLength) {
                macExpand.reset()
                macExpand.update(t)
                macExpand.update(info)
                macExpand.update(counter)
                t = macExpand.doFinal()

                val toCopy = minOf(t.size, outputLength - generatedBytes)
                System.arraycopy(t, 0, result, generatedBytes, toCopy)
                generatedBytes += toCopy
                counter++
            }
        } finally {
            zeroize(prk, t)
        }

        return result
    }

    /**
     * Аутентифицированное шифрование AES-256-GCM с проверкой целостности
     */
    fun encryptAesGcm(key: ByteArray, plaintext: ByteArray, associatedData: ByteArray? = null): ByteArray {
        val iv = randomBytes(GCM_IV_LENGTH_BYTES)
        val cipher = Cipher.getInstance(AES_GCM_ALGO)
        val keySpec = SecretKeySpec(key, "AES")
        val gcmSpec = GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv)

        cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec)
        if (associatedData != null) {
            cipher.updateAAD(associatedData)
        }

        val ciphertext = cipher.doFinal(plaintext)
        val combined = ByteArray(iv.size + ciphertext.size)
        System.arraycopy(iv, 0, combined, 0, iv.size)
        System.arraycopy(ciphertext, 0, combined, iv.size, ciphertext.size)

        return combined
    }

    /**
     * Расшифровка AES-256-GCM
     */
    fun decryptAesGcm(key: ByteArray, encryptedData: ByteArray, associatedData: ByteArray? = null): ByteArray {
        require(encryptedData.size > GCM_IV_LENGTH_BYTES) { "Invalid ciphertext length" }

        val iv = ByteArray(GCM_IV_LENGTH_BYTES)
        val ciphertext = ByteArray(encryptedData.size - GCM_IV_LENGTH_BYTES)
        System.arraycopy(encryptedData, 0, iv, 0, GCM_IV_LENGTH_BYTES)
        System.arraycopy(encryptedData, GCM_IV_LENGTH_BYTES, ciphertext, 0, ciphertext.size)

        val cipher = Cipher.getInstance(AES_GCM_ALGO)
        val keySpec = SecretKeySpec(key, "AES")
        val gcmSpec = GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv)

        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec)
        if (associatedData != null) {
            cipher.updateAAD(associatedData)
        }

        return cipher.doFinal(ciphertext)
    }
}
