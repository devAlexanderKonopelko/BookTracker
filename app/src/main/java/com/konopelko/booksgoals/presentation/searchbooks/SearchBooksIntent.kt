package com.konopelko.booksgoals.presentation.searchbooks

import com.konopelko.booksgoals.domain.model.book.Book
import com.konopelko.booksgoals.presentation.searchbooks.model.SearchBooksArgs
import com.konopelko.booksgoals.presentation.searchbooks.model.SearchScreenOrigin

sealed interface SearchBooksIntent {

    data class OnArgsReceived(val args: SearchBooksArgs) : SearchBooksIntent
    data class OnSearchTextChanged(val text: String) : SearchBooksIntent
    data class OnSearchBooks(val text: String) : SearchBooksIntent
    data class OnBookClicked(val book: Book) : SearchBooksIntent

    sealed interface SearchBooksNavigationIntent {

        data object NavigateToWishesScreen : SearchBooksNavigationIntent
        data class NavigateToAddGoalScreen(val book: Book) : SearchBooksNavigationIntent
        data class OnAddNewBookClicked(val origin: SearchScreenOrigin) : SearchBooksNavigationIntent
    }
}