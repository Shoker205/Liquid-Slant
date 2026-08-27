package com.slant.ui.screens.auth

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slant.ui.components.LivingNeuralCanvas
import com.slant.ui.components.NeuralState
import com.slant.ui.components.SlantGlassStrengthMeter
import com.slant.ui.components.SlantGlassTextField
import com.slant.ui.theme.SlantAppStateManager
import com.slant.ui.theme.SlantDimText
import com.slant.ui.theme.SlantGlassBase
import com.slant.ui.theme.SlantLanguage
import com.slant.ui.theme.SlantOledBlack
import com.slant.ui.theme.SlantPureWhite
import com.slant.ui.theme.SlantStrings
import com.slant.ui.theme.SlantThemeMode
import com.slant.ui.theme.SlantThemePalette
import com.slant.ui.theme.liquidGlass
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class AuthFlowMode {
    LOGIN, GENESIS, RECALL
}

@Composable
fun AuthScreen(
    onAuthComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var flowMode by remember { mutableStateOf(AuthFlowMode.LOGIN) }
    var neuralState by remember { mutableStateOf(NeuralState.IDLE) }
    var burstTrigger by remember { mutableLongStateOf(0L) }
    var isError by remember { mutableStateOf(false) }

    // Данные полей
    var identifier by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var seedPhrase by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmVisible by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val snackbarState = remember { SnackbarHostState() }

    val isDark = SlantAppStateManager.isDark
    val currentLang = SlantAppStateManager.language.value
    val currentPalette = SlantAppStateManager.themePalette.value

    fun triggerBurst(error: Boolean = false) {
        isError = error
        burstTrigger = System.currentTimeMillis()
        if (error) {
            scope.launch {
                delay(500)
                isError = false
            }
        }
    }

    fun notifyUser(msg: String, error: Boolean = true) {
        triggerBurst(error)
        scope.launch { snackbarState.showSnackbar(msg) }
    }

    fun submitForm() {
        when (flowMode) {
            AuthFlowMode.LOGIN -> {
                if (identifier.isBlank()) {
                    notifyUser(SlantStrings.enterNodeIdError)
                    return
                }
                if (password.isBlank()) {
                    notifyUser(SlantStrings.enterPasswordError)
                    return
                }
            }
            AuthFlowMode.GENESIS -> {
                if (identifier.isBlank()) {
                    notifyUser(SlantStrings.enterNodeIdError)
                    return
                }
                if (password.length < 8) {
                    notifyUser(SlantStrings.passwordLengthError)
                    return
                }
                if (password != confirmPassword) {
                    notifyUser(SlantStrings.passwordMismatchError)
                    return
                }
            }
            AuthFlowMode.RECALL -> {
                if (seedPhrase.isBlank()) {
                    notifyUser(SlantStrings.seedLengthError)
                    return
                }
            }
        }

        neuralState = NeuralState.LOADING
        scope.launch {
            delay(1800)
            neuralState = NeuralState.IDLE
            notifyUser(SlantStrings.nodeActivated, error = false)
            onAuthComplete()
        }
    }

    val textColor = if (isDark) SlantPureWhite else MaterialTheme.colorScheme.onBackground
    val dimColor = if (isDark) SlantDimText else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(if (isDark) SlantOledBlack else MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Верхняя плашка переключателей: Язык, Тема (День/Ночь), Палитра (Ч/Б / Monet)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .liquidGlass(
                        shape = RoundedCornerShape(20.dp),
                        alpha = 0.65f,
                        borderWidth = 1.dp
                    )
                    .padding(horizontal = 10.dp, vertical = 6.dp)
                    .testTag("auth_top_settings_bar"),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Кнопка переключения языка RU / EN
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isDark) Color(0x33FFFFFF) else MaterialTheme.colorScheme.primaryContainer)
                        .clickable {
                            SlantAppStateManager.toggleLanguage()
                            triggerBurst()
                        }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                        .testTag("auth_lang_toggle"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Language,
                        contentDescription = "Language",
                        tint = textColor,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = if (currentLang == SlantLanguage.RU) "RU" else "EN",
                        color = textColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Кнопка переключения День / Ночь
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isDark) Color(0x33FFFFFF) else MaterialTheme.colorScheme.primaryContainer)
                        .clickable {
                            SlantAppStateManager.toggleThemeMode()
                            triggerBurst()
                        }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                        .testTag("auth_theme_mode_toggle"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isDark) Icons.Rounded.DarkMode else Icons.Rounded.LightMode,
                        contentDescription = "Theme Mode",
                        tint = textColor,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = if (isDark) SlantStrings.themeNight else SlantStrings.themeDay,
                        color = textColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Кнопка переключения Ч/Б vs Material You Monet
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isDark) Color(0x33FFFFFF) else MaterialTheme.colorScheme.primaryContainer)
                        .clickable {
                            SlantAppStateManager.toggleThemePalette()
                            triggerBurst()
                        }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                        .testTag("auth_palette_toggle"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Palette,
                        contentDescription = "Theme Palette",
                        tint = textColor,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = if (currentPalette == SlantThemePalette.MONOCHROME) SlantStrings.paletteMono else SlantStrings.paletteMonet,
                        color = textColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Маскот Living Neural Fluid
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentAlignment = Alignment.Center
            ) {
                LivingNeuralCanvas(
                    state = neuralState,
                    isPassRevealed = isPasswordVisible || isConfirmVisible,
                    burstTrigger = burstTrigger,
                    isError = isError
                )
            }

            // Основная карточка Liquid Glass
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .liquidGlass(
                        shape = RoundedCornerShape(28.dp),
                        alpha = 0.75f,
                        borderWidth = 1.dp
                    )
                    .padding(horizontal = 18.dp, vertical = 20.dp)
            ) {
                AnimatedContent(
                    targetState = flowMode,
                    transitionSpec = { fadeIn(tween(250)) togetherWith fadeOut(tween(200)) },
                    label = "AuthModeTransition"
                ) { currentMode ->
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Заголовок
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            if (currentMode != AuthFlowMode.LOGIN) {
                                IconButton(
                                    onClick = {
                                        flowMode = AuthFlowMode.LOGIN
                                        neuralState = NeuralState.IDLE
                                        triggerBurst()
                                    },
                                    modifier = Modifier
                                        .size(32.dp)
                                        .testTag("auth_back_button")
                                    ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                        contentDescription = SlantStrings.back,
                                        tint = textColor
                                    )
                                }
                            } else {
                                Spacer(modifier = Modifier.size(32.dp))
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = when (currentMode) {
                                        AuthFlowMode.LOGIN -> SlantStrings.appName
                                        AuthFlowMode.GENESIS -> "GENESIS"
                                        AuthFlowMode.RECALL -> "RECALL"
                                    },
                                    color = textColor,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 4.sp
                                )
                                Text(
                                    text = when (currentMode) {
                                        AuthFlowMode.LOGIN -> SlantStrings.authTabLogin
                                        AuthFlowMode.GENESIS -> SlantStrings.authTabGenesis
                                        AuthFlowMode.RECALL -> SlantStrings.authTabRecall
                                    },
                                    color = dimColor,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium,
                                    letterSpacing = 1.sp
                                )
                            }

                            Spacer(modifier = Modifier.size(32.dp))
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        if (currentMode == AuthFlowMode.RECALL) {
                            // Ввод мнемоники
                            SlantGlassTextField(
                                value = seedPhrase,
                                onValueChange = {
                                    seedPhrase = it
                                    triggerBurst()
                                },
                                placeholder = SlantStrings.seedPhrasePlaceholder,
                                leadingIcon = Icons.Rounded.Key,
                                onFocusChanged = { focused ->
                                    neuralState = if (focused) NeuralState.RECALL else NeuralState.IDLE
                                },
                                modifier = Modifier.testTag("auth_field_seed")
                            )
                        } else {
                            // Поле логина / идентификатора узла
                            SlantGlassTextField(
                                value = identifier,
                                onValueChange = {
                                    identifier = it
                                    triggerBurst()
                                },
                                placeholder = SlantStrings.nodeIdPlaceholder,
                                leadingIcon = if (currentMode == AuthFlowMode.GENESIS) Icons.Rounded.Key else Icons.Rounded.Email,
                                keyboardType = KeyboardType.Email,
                                onFocusChanged = { focused ->
                                    if (focused) {
                                        neuralState = when (currentMode) {
                                            AuthFlowMode.LOGIN -> NeuralState.USER
                                            AuthFlowMode.GENESIS -> NeuralState.GENESIS
                                            AuthFlowMode.RECALL -> NeuralState.RECALL
                                        }
                                    } else if (neuralState != NeuralState.PASS) {
                                        neuralState = NeuralState.IDLE
                                    }
                                },
                                modifier = Modifier.testTag("auth_field_identifier")
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Поля пароля
                            SlantGlassTextField(
                                value = password,
                                onValueChange = {
                                    password = it
                                    triggerBurst()
                                },
                                placeholder = SlantStrings.masterPasswordPlaceholder,
                                leadingIcon = Icons.Rounded.Lock,
                                isPassword = true,
                                isPasswordVisible = isPasswordVisible,
                                onVisibilityToggle = {
                                    isPasswordVisible = !isPasswordVisible
                                    triggerBurst()
                                },
                                onFocusChanged = { focused ->
                                    neuralState = if (focused) NeuralState.PASS else NeuralState.IDLE
                                },
                                modifier = Modifier.testTag("auth_field_password")
                            )

                            if (currentMode == AuthFlowMode.GENESIS) {
                                Spacer(modifier = Modifier.height(8.dp))
                                SlantGlassStrengthMeter(password = password)
                                Spacer(modifier = Modifier.height(8.dp))

                                SlantGlassTextField(
                                    value = confirmPassword,
                                    onValueChange = {
                                        confirmPassword = it
                                        triggerBurst()
                                    },
                                    placeholder = SlantStrings.confirmPasswordPlaceholder,
                                    leadingIcon = Icons.Rounded.Shield,
                                    isPassword = true,
                                    isPasswordVisible = isConfirmVisible,
                                    onVisibilityToggle = {
                                        isConfirmVisible = !isConfirmVisible
                                        triggerBurst()
                                    },
                                    onFocusChanged = { focused ->
                                        neuralState = if (focused) NeuralState.PASS else NeuralState.IDLE
                                    },
                                    modifier = Modifier.testTag("auth_field_confirm_password")
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Главная кнопка действия
                        val buttonContainer = if (isDark) SlantPureWhite else MaterialTheme.colorScheme.primary
                        val buttonText = if (isDark) SlantOledBlack else MaterialTheme.colorScheme.onPrimary

                        Button(
                            onClick = { submitForm() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("auth_submit_button"),
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = buttonContainer,
                                contentColor = buttonText
                            ),
                            enabled = neuralState != NeuralState.LOADING
                        ) {
                            Text(
                                text = when {
                                    neuralState == NeuralState.LOADING -> if (currentLang == SlantLanguage.RU) "СИНХРОНИЗАЦИЯ..." else "SYNCING..."
                                    currentMode == AuthFlowMode.LOGIN -> SlantStrings.loginButton
                                    currentMode == AuthFlowMode.GENESIS -> SlantStrings.genesisButton
                                    else -> SlantStrings.recallButton
                                },
                                fontWeight = FontWeight.Black,
                                fontSize = 11.5.sp,
                                letterSpacing = 1.5.sp
                            )
                        }

                        // Ссылки переключения режимов
                        if (currentMode == AuthFlowMode.LOGIN) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                TextButton(
                                    onClick = {
                                        flowMode = AuthFlowMode.RECALL
                                        triggerBurst()
                                    },
                                    modifier = Modifier.testTag("auth_forgot_password_btn")
                                ) {
                                    Text(
                                        text = if (currentLang == SlantLanguage.RU) "Восстановить seed?" else "Restore via Seed?",
                                        color = dimColor,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                TextButton(
                                    onClick = {
                                        flowMode = AuthFlowMode.GENESIS
                                        triggerBurst()
                                    },
                                    modifier = Modifier.testTag("auth_create_identity_btn")
                                ) {
                                    Text(
                                        text = if (currentLang == SlantLanguage.RU) "Создать личность" else "Create Identity",
                                        color = textColor,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Подпись SlantTech и ссылка
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "ENGINEERED BY SLANTTECH",
                    color = dimColor.copy(alpha = 0.5f),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "blog.sl-me.online",
                    color = textColor.copy(alpha = 0.6f),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.5.sp,
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://blog.sl-me.online"))
                        context.startActivity(intent)
                    }
                )
            }
        }

        SnackbarHost(
            hostState = snackbarState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
        )
    }
}
