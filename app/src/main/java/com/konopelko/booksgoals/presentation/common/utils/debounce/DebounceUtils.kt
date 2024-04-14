package com.konopelko.booksgoals.presentation.common.utils.debounce

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay

const val INPUT_DEBOUNCE_VALUE_IN_MILLIS = 500L

@Composable
fun DebounceEffect(
    input: String,
    debounceValue: Long = INPUT_DEBOUNCE_VALUE_IN_MILLIS,
    operation: (String) -> Unit
) {
    LaunchedEffect(input) {
        delay(debounceValue)
        operation(input)
    }
}
