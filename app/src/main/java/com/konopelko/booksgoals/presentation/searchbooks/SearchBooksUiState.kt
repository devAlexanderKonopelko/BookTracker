package com.konopelko.booksgoals.presentation.searchbooks

import com.konopelko.booksgoals.data.api.response.searchbooks.SearchBooksResponse.BookResponse

data class SearchBooksUiState(
    val searchText: String = "",
    val searchResults: List<BookResponse> = emptyList(),
    val isSearching: Boolean = false
) {

    sealed interface PartialSearchBooksState {

        object SearchInProgress : PartialSearchBooksState

        data class SearchTextChanged(val text: String) : PartialSearchBooksState
        data class SearchResultsReceived(val searchResultBooks: List<BookResponse>) : PartialSearchBooksState
    }
}
