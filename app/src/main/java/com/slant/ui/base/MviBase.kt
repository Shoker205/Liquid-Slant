package com.slant.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.Arrays

interface UiState
interface UiEvent
interface UiEffect

abstract class BaseViewModel<State : UiState, Event : UiEvent, Effect : UiEffect>(
    initialState: State
) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<State> = _uiState.asStateFlow()

    private val _effect = Channel<Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    protected val currentState: State
        get() = _uiState.value

    abstract fun handleEvent(event: Event)

    protected fun updateState(reducer: State.() -> State) {
        _uiState.value = _uiState.value.reducer()
    }

    protected fun sendEffect(effect: Effect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}

/**
 * Обертка для хранения паролей и ключей с гарантированным занулением в ОЗУ
 */
class SecureByteArray(bytes: ByteArray) {
    private val buffer: ByteArray = bytes.clone()
    var isDestroyed: Boolean = false
        private set

    fun access(block: (ByteArray) -> Unit) {
        check(!isDestroyed) { "Secure buffer already zeroized" }
        block(buffer)
    }

    fun zeroize() {
        if (!isDestroyed) {
            Arrays.fill(buffer, 0.toByte())
            isDestroyed = true
        }
    }
}
