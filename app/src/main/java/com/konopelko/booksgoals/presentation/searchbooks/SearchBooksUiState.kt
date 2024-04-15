package com.konopelko.booksgoals.presentation.searchbooks

import com.konopelko.booksgoals.domain.model.book.Book


data class SearchBooksUiState(
    val searchText: String = "",
    val searchResults: List<Book> = emptyList(),
    val isSearching: Boolean = false,

    //todo: refactor somehow NOT to use uiState
    val shouldNavigateToAddGoalScreen: Boolean = false,
    val bookToCreateGoal: Book = Book(),
    val shouldNavigateToWishesScreen: Boolean = false
) {

    sealed interface PartialSearchBooksState {

        data object SearchInProgress : PartialSearchBooksState
        data object BookSavedSuccessfully : PartialSearchBooksState

        data class SearchTextChanged(val text: String) : PartialSearchBooksState
        data class SearchResultsReceived(val searchResultBooks: List<Book>) : PartialSearchBooksState
        data class NavigateToAddGoalState(val book: Book) : PartialSearchBooksState
    }
}
