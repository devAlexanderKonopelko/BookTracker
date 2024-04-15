package com.konopelko.booksgoals.presentation.searchbooks

import com.konopelko.booksgoals.domain.model.book.Book
import com.konopelko.booksgoals.domain.model.booksearch.SearchScreenOrigin

sealed interface SearchBooksIntent {

    data class OnArgsReceived(val args: SearchScreenOrigin) : SearchBooksIntent
    data class OnSearchTextChanged(val text: String) : SearchBooksIntent
    data class OnSearchBooks(val text: String) : SearchBooksIntent
    data class OnBookClicked(val book: Book) : SearchBooksIntent

    sealed interface SearchBooksNavigationIntent {

        data object OnAddNewBookClicked : SearchBooksNavigationIntent
        data object NavigateToWishesScreen : SearchBooksNavigationIntent
        data class NavigateToAddGoalScreen(val book: Book) : SearchBooksNavigationIntent
    }
}