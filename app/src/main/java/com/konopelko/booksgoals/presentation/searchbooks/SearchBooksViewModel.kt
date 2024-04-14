package com.konopelko.booksgoals.presentation.searchbooks

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.konopelko.booksgoals.domain.usecase.searchbooks.SearchBooksUseCase
import com.konopelko.booksgoals.presentation.common.base.BaseViewModel
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksIntent.OnSearchBooks
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksIntent.OnSearchTextChanged
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksUiState.PartialSearchBooksState
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksUiState.PartialSearchBooksState.SearchInProgress
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksUiState.PartialSearchBooksState.SearchResultsReceived
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksUiState.PartialSearchBooksState.SearchTextChanged
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchBooksViewModel(
    initialState: SearchBooksUiState,
    private val searchBooksUseCase: SearchBooksUseCase,
) : BaseViewModel<SearchBooksIntent, SearchBooksUiState, PartialSearchBooksState>(
    initialState = initialState
) {

    override fun acceptIntent(intent: SearchBooksIntent) = when (intent) {
        is OnSearchTextChanged -> onSearchTextChanged(intent.text)
        is OnSearchBooks -> onSearchBooks(intent.text)
    }

    override fun mapUiState(
        previousState: SearchBooksUiState,
        partialState: PartialSearchBooksState
    ): SearchBooksUiState = when (partialState) {
        SearchInProgress -> previousState.copy(isSearching = true)
        is SearchTextChanged -> {
            Log.e("SearchBooksViewModel", "SearchTextChanged called")
            previousState.copy(searchText = partialState.text)
        }

        is SearchResultsReceived -> {
            Log.e("SearchBooksViewModel", "SearchResultsReceived called")
            previousState.copy(
                searchResults = partialState.searchResultBooks,
                isSearching = false
            )
        }
    }

    private fun onSearchBooks(text: String) {
        Log.e("SearchBooksViewModel", "onSearchBooks called")

        if (text.isNotEmpty()) {
            if (uiState.value.isSearching.not()) {
                updateUiState(SearchInProgress)
                searchBooks(text)
            }
        } else {
            updateUiState(SearchResultsReceived(searchResultBooks = emptyList()))
        }
    }

    private fun searchBooks(text: String) {
        viewModelScope.launch {
            searchBooksUseCase(text)
                .catch {
                    it.printStackTrace()
                }
                .collectLatest { response ->
                    val results = response.books
                    Log.e("SearchBooksViewModel", "updateUiState called")
                    updateUiState(
                        SearchResultsReceived(
                            searchResultBooks = results
                        )
                    )
                }

        }
    }

    private fun onSearchTextChanged(text: String) {
        updateUiState(SearchTextChanged(text))
    }
}