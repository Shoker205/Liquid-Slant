package com.slant.ui.screens.auth

import androidx.lifecycle.viewModelScope
import com.slant.ui.base.BaseViewModel
import com.slant.ui.base.UiEffect
import com.slant.ui.base.UiEvent
import com.slant.ui.base.UiState
import com.slant.ui.components.NeuralState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class AuthUiState(
    val flowMode: AuthFlowMode = AuthFlowMode.LOGIN,
    val identifier: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmVisible: Boolean = false,
    val neuralState: NeuralState = NeuralState.IDLE,
    val isError: Boolean = false,
    val burstTrigger: Long = 0L,
    val isLoading: Boolean = false
) : UiState

sealed interface AuthUiEvent : UiEvent {
    data class ModeChanged(val mode: AuthFlowMode) : AuthUiEvent
    data class IdentifierChanged(val value: String) : AuthUiEvent
    data class FocusChanged(val isFocused: Boolean, val fieldType: String) : AuthUiEvent
    data object TogglePasswordVisibility : AuthUiEvent
    data object ToggleConfirmVisibility : AuthUiEvent
    data class SubmitAuth(val pass: CharArray, val passConfirm: CharArray?) : AuthUiEvent
}

sealed interface AuthUiEffect : UiEffect {
    data object NavigateToMain : AuthUiEffect
    data class ShowToast(val message: String, val isError: Boolean = true) : AuthUiEffect
}

class AuthViewModel : BaseViewModel<AuthUiState, AuthUiEvent, AuthUiEffect>(AuthUiState()) {

    override fun handleEvent(event: AuthUiEvent) {
        when (event) {
            is AuthUiEvent.ModeChanged -> {
                updateState {
                    copy(
                        flowMode = event.mode,
                        neuralState = NeuralState.IDLE,
                        burstTrigger = System.currentTimeMillis()
                    )
                }
            }

            is AuthUiEvent.IdentifierChanged -> {
                updateState {
                    copy(
                        identifier = event.value,
                        burstTrigger = System.currentTimeMillis()
                    )
                }
            }

            is AuthUiEvent.FocusChanged -> {
                val newNeuralState = if (event.isFocused) {
                    when (event.fieldType) {
                        "identifier" -> when (currentState.flowMode) {
                            AuthFlowMode.LOGIN -> NeuralState.USER
                            AuthFlowMode.GENESIS -> NeuralState.GENESIS
                            AuthFlowMode.RECALL -> NeuralState.RECALL
                        }
                        "password" -> NeuralState.PASS
                        else -> NeuralState.IDLE
                    }
                } else {
                    NeuralState.IDLE
                }
                updateState { copy(neuralState = newNeuralState) }
            }

            is AuthUiEvent.TogglePasswordVisibility -> {
                updateState {
                    copy(
                        isPasswordVisible = !isPasswordVisible,
                        burstTrigger = System.currentTimeMillis()
                    )
                }
            }

            is AuthUiEvent.ToggleConfirmVisibility -> {
                updateState {
                    copy(
                        isConfirmVisible = !isConfirmVisible,
                        burstTrigger = System.currentTimeMillis()
                    )
                }
            }

            is AuthUiEvent.SubmitAuth -> {
                processAuthentication(event.pass, event.passConfirm)
            }
        }
    }

    private fun processAuthentication(pass: CharArray, passConfirm: CharArray?) {
        if (currentState.identifier.isBlank()) {
            triggerError("Укажите идентификатор или почту")
            return
        }

        if (pass.isEmpty()) {
            triggerError("Введите мастер-пароль")
            return
        }

        if (currentState.flowMode == AuthFlowMode.GENESIS) {
            if (pass.size < 8) {
                triggerError("Пароль должен содержать минимум 8 знаков")
                return
            }
            if (passConfirm == null || !pass.contentEquals(passConfirm)) {
                triggerError("Пароли не совпадают")
                return
            }
        }

        viewModelScope.launch {
            updateState { copy(isLoading = true, neuralState = NeuralState.LOADING) }
            
            // Имитация деривации ключей Argon2id
            delay(1600)

            // Зануление символов в ОЗУ после деривации
            pass.fill('0')
            passConfirm?.fill('0')

            updateState { copy(isLoading = false, neuralState = NeuralState.IDLE) }
            sendEffect(AuthUiEffect.ShowToast("УЗЕЛ АКТИВИРОВАН", isError = false))
            sendEffect(AuthUiEffect.NavigateToMain)
        }
    }

    private fun triggerError(msg: String) {
        updateState {
            copy(
                isError = true,
                burstTrigger = System.currentTimeMillis()
            )
        }
        sendEffect(AuthUiEffect.ShowToast(msg, isError = true))

        viewModelScope.launch {
            delay(500)
            updateState { copy(isError = false) }
        }
    }
}
