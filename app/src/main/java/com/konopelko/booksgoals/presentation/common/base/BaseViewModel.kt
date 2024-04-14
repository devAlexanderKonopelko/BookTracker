package com.konopelko.booksgoals.presentation.common.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class BaseViewModel<INTENT, UI_STATE, PARTIAL_UI_STATE>(
    initialState: UI_STATE
) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<UI_STATE> = _uiState.asStateFlow()

    protected fun updateUiState(partialState: PARTIAL_UI_STATE) {
        _uiState.update {
            mapUiState(
                previousState = _uiState.value,
                partialState = partialState
            )
        }
    }

    abstract fun acceptIntent(intent: INTENT)

    abstract fun mapUiState(
        previousState: UI_STATE,
        partialState: PARTIAL_UI_STATE
    ): UI_STATE
}