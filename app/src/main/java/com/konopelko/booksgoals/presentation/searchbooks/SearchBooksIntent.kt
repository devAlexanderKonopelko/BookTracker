package com.konopelko.booksgoals.presentation.searchbooks

import com.konopelko.booksgoals.data.api.response.searchbooks.SearchBooksResponse.BookResponse

sealed interface SearchBooksIntent {

    data class OnSearchTextChanged(val text: String) : SearchBooksIntent
    data class OnSearchBooks(val text: String) : SearchBooksIntent

    sealed interface SearchBooksNavigationIntent {

        data object OnAddNewBookClicked : SearchBooksNavigationIntent

        data class OnBookClicked(val book: BookResponse) : SearchBooksNavigationIntent
    }
}