package com.konopelko.booksgoals.presentation.finishedbooks

import com.konopelko.booksgoals.domain.model.book.Book

data class FinishedBooksUiState(
    val books: List<Book> = emptyList()
) {

    sealed interface FinishedBooksPartialState {

        data class FinishedBooksLoaded(val books: List<Book>) : FinishedBooksPartialState
    }
}
