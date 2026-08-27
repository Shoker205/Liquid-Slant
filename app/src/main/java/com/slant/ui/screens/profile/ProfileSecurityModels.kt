package com.slant.ui.screens.profile

data class StorageBreakdown(
    val mediaBytes: Long,
    val databaseBytes: Long,
    val keysAndIndexBytes: Long,
    val maxCacheLimitGb: Int = 16
) {
    val totalUsedBytes: Long get() = mediaBytes + databaseBytes + keysAndIndexBytes
    val mediaFraction: Float get() = if (totalUsedBytes > 0) mediaBytes.toFloat() / totalUsedBytes else 0f
    val databaseFraction: Float get() = if (totalUsedBytes > 0) databaseBytes.toFloat() / totalUsedBytes else 0f
    val keysFraction: Float get() = if (totalUsedBytes > 0) keysAndIndexBytes.toFloat() / totalUsedBytes else 0f
}

data class SecurityPreferences(
    val isScreenshotBlocked: Boolean = true,
    val isRecentsBlurEnabled: Boolean = true,
    val autoLockTimeoutMinutes: Int = 5,
    val isDohRelayEnabled: Boolean = true,
    val activeRelayDomain: String = "sl-me.ru"
)
