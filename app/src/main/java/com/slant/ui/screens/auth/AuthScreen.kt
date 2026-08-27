package com.slant.ui.screens.auth

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.slant.ui.theme.SlantDimText
import com.slant.ui.theme.SlantGlassBase
import com.slant.ui.theme.SlantOledBlack
import com.slant.ui.theme.SlantPureWhite
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
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmVisible by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val snackbarState = remember { SnackbarHostState() }

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
        if (identifier.isBlank()) {
            notifyUser("Введите идентификатор узла или почту")
            return
        }

        when (flowMode) {
            AuthFlowMode.LOGIN -> {
                if (password.isBlank()) {
                    notifyUser("Введите мастер-пароль")
                    return
                }
            }
            AuthFlowMode.GENESIS -> {
                if (password.length < 8) {
                    notifyUser("Пароль должен содержать минимум 8 символов")
                    return
                }
                if (password != confirmPassword) {
                    notifyUser("Пароли не совпадают")
                    return
                }
            }
            AuthFlowMode.RECALL -> {}
        }

        neuralState = NeuralState.LOADING
        scope.launch {
            delay(1800)
            neuralState = NeuralState.IDLE
            notifyUser("УЗЕЛ АКТИВИРОВАН", error = false)
            onAuthComplete()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SlantOledBlack)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Маскот Neural Fluid
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
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
                        shape = RoundedCornerShape(32.dp),
                        backgroundColor = SlantGlassBase,
                        alpha = 0.70f,
                        borderWidth = 1.dp
                    )
                    .padding(horizontal = 20.dp, vertical = 24.dp)
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
                                        contentDescription = "Back",
                                        tint = SlantPureWhite
                                    )
                                }
                            } else {
                                Spacer(modifier = Modifier.size(32.dp))
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = when (currentMode) {
                                        AuthFlowMode.LOGIN -> "SLANT"
                                        AuthFlowMode.GENESIS -> "GENESIS"
                                        AuthFlowMode.RECALL -> "RECALL"
                                    },
                                    color = SlantPureWhite,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 4.sp
                                )
                                Text(
                                    text = when (currentMode) {
                                        AuthFlowMode.LOGIN -> "Авторизация узла"
                                        AuthFlowMode.GENESIS -> "Генерация личности"
                                        AuthFlowMode.RECALL -> "Восстановление доступа"
                                    },
                                    color = SlantDimText,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium,
                                    letterSpacing = 1.sp
                                )
                            }

                            Spacer(modifier = Modifier.size(32.dp))
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Поле логина / почты
                        SlantGlassTextField(
                            value = identifier,
                            onValueChange = {
                                identifier = it
                                triggerBurst()
                            },
                            placeholder = if (currentMode == AuthFlowMode.GENESIS) "@USERNAME ИЛИ ПОЧТА" else "ИДЕНТИФИКАТОР",
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

                        Spacer(modifier = Modifier.height(12.dp))

                        // Поля мастер-паролей
                        if (currentMode != AuthFlowMode.RECALL) {
                            SlantGlassTextField(
                                value = password,
                                onValueChange = {
                                    password = it
                                    triggerBurst()
                                },
                                placeholder = "МАСТЕР-ПАРОЛЬ",
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
                                Spacer(modifier = Modifier.height(10.dp))
                                SlantGlassStrengthMeter(password = password)
                                Spacer(modifier = Modifier.height(10.dp))

                                SlantGlassTextField(
                                    value = confirmPassword,
                                    onValueChange = {
                                        confirmPassword = it
                                        triggerBurst()
                                    },
                                    placeholder = "ПОДТВЕРЖДЕНИЕ ПАРОЛЯ",
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

                        Spacer(modifier = Modifier.height(20.dp))

                        // Главная кнопка действия (Material 3 Liquid Glass Button)
                        Button(
                            onClick = { submitForm() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("auth_submit_button"),
                            shape = RoundedCornerShape(22.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SlantPureWhite,
                                contentColor = SlantOledBlack
                            ),
                            enabled = neuralState != NeuralState.LOADING
                        ) {
                            Text(
                                text = when {
                                    neuralState == NeuralState.LOADING -> "СИНХРОНИЗАЦИЯ..."
                                    currentMode == AuthFlowMode.LOGIN -> "ВОЙТИ В УЗЕЛ"
                                    currentMode == AuthFlowMode.GENESIS -> "СОЗДАТЬ КЛЮЧИ"
                                    else -> "ОТПРАВИТЬ СЕМЯ"
                                },
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp,
                                letterSpacing = 2.sp
                            )
                        }

                        // Ссылки переключения режимов
                        if (currentMode == AuthFlowMode.LOGIN) {
                            Spacer(modifier = Modifier.height(14.dp))
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
                                        text = "Забыли пароль?",
                                        color = SlantDimText,
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
                                        text = "Создать личность",
                                        color = SlantPureWhite,
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
                    .padding(bottom = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "ENGINEERED BY SLANTTECH",
                    color = SlantDimText.copy(alpha = 0.5f),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "blog.sl-me.online",
                    color = SlantPureWhite.copy(alpha = 0.4f),
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
