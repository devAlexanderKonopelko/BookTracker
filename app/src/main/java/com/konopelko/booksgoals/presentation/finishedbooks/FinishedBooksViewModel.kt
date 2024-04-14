package com.konopelko.booksgoals.presentation.finishedbooks

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.konopelko.booksgoals.domain.usecase.getfinishedbooks.GetFinishedBooksUseCase
import com.konopelko.booksgoals.presentation.common.base.BaseViewModel
import com.konopelko.booksgoals.presentation.finishedbooks.FinishedBooksUiState.FinishedBooksPartialState
import com.konopelko.booksgoals.presentation.finishedbooks.FinishedBooksUiState.FinishedBooksPartialState.FinishedBooksLoaded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FinishedBooksViewModel(
    initialState: FinishedBooksUiState,
    private val getFinishedBooksUseCase: GetFinishedBooksUseCase
) : BaseViewModel<FinishedBooksIntent, FinishedBooksUiState, FinishedBooksPartialState>(
    initialState = initialState
) {

    init {
        Log.e("BooksViewModel", "init")

        loadFinishedBooks()
    }

    override fun acceptIntent(intent: FinishedBooksIntent) {
        /* no-op */
    }

    override fun mapUiState(
        previousState: FinishedBooksUiState,
        partialState: FinishedBooksPartialState
    ): FinishedBooksUiState = when(partialState) {
        is FinishedBooksLoaded -> previousState.copy(books = partialState.books)
    }

    private fun loadFinishedBooks() {
        viewModelScope.launch(Dispatchers.IO) {
            getFinishedBooksUseCase().onSuccess { finishedBooks ->
                updateUiState(FinishedBooksLoaded(books = finishedBooks))
            }.onError {
                Log.e("BooksViewModel", "error occurred when loading finished books: ${it.exception}")
            }
        }
    }
}