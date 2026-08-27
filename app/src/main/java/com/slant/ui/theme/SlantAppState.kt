package com.slant.ui.theme

import android.content.Context
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

enum class SlantLanguage(val code: String, val displayName: String, val nativeName: String) {
    RU("ru", "Русский", "Русский"),
    EN("en", "English", "English")
}

enum class SlantThemeMode(val titleRu: String, val titleEn: String) {
    DARK("Ночная (Тёмная)", "Night (Dark)"),
    LIGHT("Дневная (Светлая)", "Day (Light)")
}

enum class SlantThemePalette(val titleRu: String, val titleEn: String) {
    MONOCHROME("Стандарт (Ч/Б OLED)", "Standard (B&W OLED)"),
    MATERIAL_YOU("Системная (Material You Monet)", "System (Material You Monet)")
}

object SlantAppStateManager {
    private val _language = mutableStateOf(SlantLanguage.RU)
    val language: State<SlantLanguage> = _language

    private val _themeMode = mutableStateOf(SlantThemeMode.DARK)
    val themeMode: State<SlantThemeMode> = _themeMode

    private val _themePalette = mutableStateOf(SlantThemePalette.MONOCHROME)
    val themePalette: State<SlantThemePalette> = _themePalette

    fun setLanguage(lang: SlantLanguage) {
        _language.value = lang
    }

    fun setThemeMode(mode: SlantThemeMode) {
        _themeMode.value = mode
    }

    fun setThemePalette(palette: SlantThemePalette) {
        _themePalette.value = palette
    }

    fun toggleLanguage() {
        _language.value = if (_language.value == SlantLanguage.RU) SlantLanguage.EN else SlantLanguage.RU
    }

    fun toggleThemeMode() {
        _themeMode.value = if (_themeMode.value == SlantThemeMode.DARK) SlantThemeMode.LIGHT else SlantThemeMode.DARK
    }

    fun toggleThemePalette() {
        _themePalette.value = if (_themePalette.value == SlantThemePalette.MONOCHROME) SlantThemePalette.MATERIAL_YOU else SlantThemePalette.MONOCHROME
    }

    val isDark: Boolean
        get() = _themeMode.value == SlantThemeMode.DARK

    val isMonet: Boolean
        get() = _themePalette.value == SlantThemePalette.MATERIAL_YOU
}

/**
 * Словарь локализации SlantStrings для динамического перевода всего приложения (RU / EN)
 */
object SlantStrings {
    private val lang: SlantLanguage
        get() = SlantAppStateManager.language.value

    // Общие элементы
    val appName: String get() = "SLANT"
    val back: String get() = if (lang == SlantLanguage.RU) "Назад" else "Back"
    val cancel: String get() = if (lang == SlantLanguage.RU) "Отмена" else "Cancel"
    val save: String get() = if (lang == SlantLanguage.RU) "Сохранить" else "Save"
    val search: String get() = if (lang == SlantLanguage.RU) "Поиск" else "Search"
    val done: String get() = if (lang == SlantLanguage.RU) "Готово" else "Done"
    val settings: String get() = if (lang == SlantLanguage.RU) "Настройки" else "Settings"
    val profile: String get() = if (lang == SlantLanguage.RU) "Профиль" else "Profile"
    val active: String get() = if (lang == SlantLanguage.RU) "Активен" else "Active"
    val copy: String get() = if (lang == SlantLanguage.RU) "Скопировано в буфер" else "Copied to clipboard"

    // Докбар
    val dockChats: String get() = if (lang == SlantLanguage.RU) "Чаты" else "Chats"
    val dockGroups: String get() = if (lang == SlantLanguage.RU) "Группы" else "Groups"
    val dockChannels: String get() = if (lang == SlantLanguage.RU) "Каналы" else "Channels"
    val dockBots: String get() = if (lang == SlantLanguage.RU) "Боты" else "Bots"

    // Категории главного экрана
    val categoryAll: String get() = if (lang == SlantLanguage.RU) "Все" else "All"
    val categoryP2p: String get() = if (lang == SlantLanguage.RU) "P2P" else "P2P"
    val categoryMesh: String get() = if (lang == SlantLanguage.RU) "Mesh" else "Mesh"
    val categoryEncrypted: String get() = if (lang == SlantLanguage.RU) "Зашифрованные" else "Encrypted"
    val categoryChannels: String get() = if (lang == SlantLanguage.RU) "Каналы" else "Channels"

    // Быстрый доступ
    val quickSearch: String get() = if (lang == SlantLanguage.RU) "Поиск" else "Search"
    val quickVault: String get() = if (lang == SlantLanguage.RU) "Хранилище" else "Vault"
    val quickFolders: String get() = if (lang == SlantLanguage.RU) "Папки" else "Folders"
    val quickCluster: String get() = if (lang == SlantLanguage.RU) "Кластер" else "Cluster"
    val quickMedia: String get() = if (lang == SlantLanguage.RU) "Медиа RAW" else "RAW Media"
    val quickCall: String get() = if (lang == SlantLanguage.RU) "P2P Звонок" else "P2P Call"
    val quickSettings: String get() = if (lang == SlantLanguage.RU) "Настройки" else "Settings"
    val quickAntiForensics: String get() = if (lang == SlantLanguage.RU) "Anti-Forensics" else "Anti-Forensics"
    val quickAuth: String get() = if (lang == SlantLanguage.RU) "Вход / Seed" else "Login / Seed"

    // Папки
    val chatFoldersHeader: String get() = if (lang == SlantLanguage.RU) "ПАПКИ ЧАТОВ" else "CHAT FOLDERS"
    val folderAllChats: String get() = if (lang == SlantLanguage.RU) "Все чаты" else "All Chats"
    val folderPersonalP2p: String get() = if (lang == SlantLanguage.RU) "Личные P2P" else "Direct P2P"
    val folderMeshClusters: String get() = if (lang == SlantLanguage.RU) "Mesh Кластеры" else "Mesh Clusters"
    val folderChannelsInfo: String get() = if (lang == SlantLanguage.RU) "Каналы и Инфо" else "Channels & Info"

    // Окно входа (AuthScreen)
    val authSubtitle: String get() = if (lang == SlantLanguage.RU) "СУВЕРЕННЫЙ P2P МЕССЕНДЖЕР" else "SOVEREIGN P2P MESSENGER"
    val authTabLogin: String get() = if (lang == SlantLanguage.RU) "ВХОД В УЗЕЛ" else "NODE LOGIN"
    val authTabGenesis: String get() = if (lang == SlantLanguage.RU) "СОЗДАТЬ УЗЕЛ" else "GENESIS NODE"
    val authTabRecall: String get() = if (lang == SlantLanguage.RU) "SEED ФРАЗА" else "SEED RECALL"
    
    val nodeIdPlaceholder: String get() = if (lang == SlantLanguage.RU) "Идентификатор узла (Node ID / Alias)" else "Node ID or Alias"
    val masterPasswordPlaceholder: String get() = if (lang == SlantLanguage.RU) "Мастер-пароль шифрования" else "Master Encryption Password"
    val confirmPasswordPlaceholder: String get() = if (lang == SlantLanguage.RU) "Подтверждение пароля" else "Confirm Master Password"
    val seedPhrasePlaceholder: String get() = if (lang == SlantLanguage.RU) "12/24 слова мнемонической фразы..." else "12/24 words seed phrase..."
    
    val loginButton: String get() = if (lang == SlantLanguage.RU) "ПОДКЛЮЧИТЬСЯ К УЗЛУ" else "CONNECT TO NODE"
    val genesisButton: String get() = if (lang == SlantLanguage.RU) "ГЕНЕРИРОВАТЬ ЭНТРОПИЮ" else "GENERATE ENTROPY"
    val recallButton: String get() = if (lang == SlantLanguage.RU) "ВОССТАНОВИТЬ КЛЮЧИ" else "RESTORE IDENTITY"
    
    val nodeActivated: String get() = if (lang == SlantLanguage.RU) "УЗЕЛ АКТИВИРОВАН" else "NODE ACTIVATED"
    val enterNodeIdError: String get() = if (lang == SlantLanguage.RU) "Введите идентификатор узла" else "Enter Node ID or Alias"
    val enterPasswordError: String get() = if (lang == SlantLanguage.RU) "Введите мастер-пароль" else "Enter Master Password"
    val passwordLengthError: String get() = if (lang == SlantLanguage.RU) "Пароль должен содержать минимум 8 символов" else "Password must be at least 8 characters"
    val passwordMismatchError: String get() = if (lang == SlantLanguage.RU) "Пароли не совпадают" else "Passwords do not match"
    val seedLengthError: String get() = if (lang == SlantLanguage.RU) "Введите 12 или 24 слова" else "Please enter 12 or 24 words"

    val themeDay: String get() = if (lang == SlantLanguage.RU) "Дневная" else "Day"
    val themeNight: String get() = if (lang == SlantLanguage.RU) "Ночная" else "Night"
    val paletteMono: String get() = if (lang == SlantLanguage.RU) "Ч/Б OLED" else "B&W OLED"
    val paletteMonet: String get() = if (lang == SlantLanguage.RU) "Material You" else "Material You"
    val languageLabel: String get() = if (lang == SlantLanguage.RU) "Язык" else "Language"
    val themeLabel: String get() = if (lang == SlantLanguage.RU) "Тема" else "Theme"
    val paletteLabel: String get() = if (lang == SlantLanguage.RU) "Палитра" else "Palette"

    val securityZeroRam: String get() = if (lang == SlantLanguage.RU) "Zero-RAM & Anti-Forensics активны" else "Zero-RAM & Anti-Forensics active"
    val securityNotice: String get() = if (lang == SlantLanguage.RU) 
        "Приватные ключи генерируются локально по протоколу Ed25519. Данные не покидают устройство." 
        else "Private keys are generated locally via Ed25519. Data never leaves your device."

    // Экран выбора языка (LanguageSelectionScreen)
    val languageTitle: String get() = if (lang == SlantLanguage.RU) "ЯЗЫК ИНТЕРФЕЙСА" else "INTERFACE LANGUAGE"
    val languageSubtitle: String get() = if (lang == SlantLanguage.RU) "Выберите язык для отображения приложения" else "Select the application display language"
    val languageApplied: String get() = if (lang == SlantLanguage.RU) "Язык интерфейса изменён" else "Interface language changed"

    // Экран настроек оформления и тем
    val appearanceTitle: String get() = if (lang == SlantLanguage.RU) "ОФОРМЛЕНИЕ И ТЕМА" else "APPEARANCE & THEME"
    val themeModeSection: String get() = if (lang == SlantLanguage.RU) "РЕЖИМ ТЕМЫ" else "THEME MODE"
    val themeDayTitle: String get() = if (lang == SlantLanguage.RU) "Дневная тема (Светлая)" else "Day Mode (Light)"
    val themeDayDesc: String get() = if (lang == SlantLanguage.RU) "Высококонтрастная светлая стеклянная подложка" else "High contrast light frosted glass"
    val themeNightTitle: String get() = if (lang == SlantLanguage.RU) "Ночная тема (Тёмная OLED)" else "Night Mode (Dark OLED)"
    val themeNightDesc: String get() = if (lang == SlantLanguage.RU) "Глубокий чёрный #000000 с зеркальными бликами" else "Deep black #000000 with specular highlights"

    val themePaletteSection: String get() = if (lang == SlantLanguage.RU) "ЦВЕТОВАЯ ПАЛИТРА" else "COLOR PALETTE"
    val paletteMonoTitle: String get() = if (lang == SlantLanguage.RU) "Стандарт (Монохром ч/б)" else "Standard (Monochrome B&W)"
    val paletteMonoDesc: String get() = if (lang == SlantLanguage.RU) "Фирменный стиль SlantTech: чистый белый и OLED чёрный" else "SlantTech signature: pure white & OLED black"
    val paletteMonetTitle: String get() = if (lang == SlantLanguage.RU) "Системная (Material You / Monet)" else "System (Material You / Monet)"
    val paletteMonetDesc: String get() = if (lang == SlantLanguage.RU) "Динамические акценты цветов вашей системы Android" else "Dynamic accent colors from your Android system"

    // Профиль и настройки
    val profileTitle: String get() = if (lang == SlantLanguage.RU) "ПРОФИЛЬ УЗЛА" else "NODE PROFILE"
    val menuSlantTech: String get() = if (lang == SlantLanguage.RU) "Настройки SlantTech" else "SlantTech Settings"
    val menuCustomProfile: String get() = if (lang == SlantLanguage.RU) "Кастомизация профиля" else "Profile Customization"
    val menuAppearance: String get() = if (lang == SlantLanguage.RU) "Тема и оформление" else "Theme & Appearance"
    val menuNodes: String get() = if (lang == SlantLanguage.RU) "Узлы и ретрансляторы" else "Nodes & Relays"
    val menuAccount: String get() = if (lang == SlantLanguage.RU) "Аккаунт и личность" else "Account & Identity"
    val menuChatSettings: String get() = if (lang == SlantLanguage.RU) "Настройки чатов" else "Chat Settings"
    val menuPrivacy: String get() = if (lang == SlantLanguage.RU) "Конфиденциальность" else "Privacy & Security"
    val menuNotifications: String get() = if (lang == SlantLanguage.RU) "Уведомления" else "Notifications"
    val menuStorage: String get() = if (lang == SlantLanguage.RU) "Данные и память" else "Data & Storage"
    val menuFolders: String get() = if (lang == SlantLanguage.RU) "Папки с чатами" else "Chat Folders"
    val menuDevices: String get() = if (lang == SlantLanguage.RU) "Устройства и сессии" else "Devices & Sessions"
    val menuPower: String get() = if (lang == SlantLanguage.RU) "Энергосбережение" else "Power Saving"
    val menuLanguage: String get() = if (lang == SlantLanguage.RU) "Язык" else "Language"
    val menuSupport: String get() = if (lang == SlantLanguage.RU) "Поддержка SlantTech" else "SlantTech Support"
    val menuFaq: String get() = if (lang == SlantLanguage.RU) "Архитектура протокола" else "Protocol Architecture"
    val menuPrivacyPolicy: String get() = if (lang == SlantLanguage.RU) "Политика Zero-Knowledge" else "Zero-Knowledge Policy"

    // Кастомизация профиля
    val customProfileHeader: String get() = if (lang == SlantLanguage.RU) "КАСТОМИЗАЦИЯ ПРОФИЛЯ" else "PROFILE CUSTOMIZATION"
    val avatarStyleHeader: String get() = if (lang == SlantLanguage.RU) "СТИЛЬ АВАТАРА УЗЛА" else "AVATAR NODE STYLE"
    val publicNameHeader: String get() = if (lang == SlantLanguage.RU) "ПУБЛИЧНОЕ ИМЯ УЗЛА" else "PUBLIC NODE NAME"
    val identityStatusHeader: String get() = if (lang == SlantLanguage.RU) "СТАТУС ЛИЧНОСТИ" else "IDENTITY STATUS"
    val saveChangesButton: String get() = if (lang == SlantLanguage.RU) "СОХРАНИТЬ ИЗМЕНЕНИЯ" else "SAVE CHANGES"
    
    // Настройки чата
    val chatSettingsHeader: String get() = if (lang == SlantLanguage.RU) "НАСТРОЙКИ ЧАТОВ" else "CHAT SETTINGS"
    val fontSizeLabel: String get() = if (lang == SlantLanguage.RU) "Размер шрифта сообщений" else "Message Font Size"
    val swipeToReplyLabel: String get() = if (lang == SlantLanguage.RU) "Свайп для ответа" else "Swipe to Reply"
    val swipeToReplySub: String get() = if (lang == SlantLanguage.RU) "Быстрый жест цитирования сообщения" else "Quick swipe gesture to quote a message"
    val sendByEnterLabel: String get() = if (lang == SlantLanguage.RU) "Отправка по Enter" else "Send by Enter"
    val sendByEnterSub: String get() = if (lang == SlantLanguage.RU) "Перенос строки через Shift + Enter" else "Line break via Shift + Enter"
    val autoPlayVideosLabel: String get() = if (lang == SlantLanguage.RU) "Автозапуск кружков (Video Notes)" else "Autoplay Video Notes"
    val autoPlayVideosSub: String get() = if (lang == SlantLanguage.RU) "Воспроизведение без сохранения в медиа-кэш" else "Stream playback without persistent cache"
}
