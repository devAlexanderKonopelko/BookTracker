package com.konopelko.booksgoals.presentation.searchbooks

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.konopelko.booksgoals.domain.model.book.Book
import com.konopelko.booksgoals.presentation.searchbooks.model.SearchScreenOrigin
import com.konopelko.booksgoals.presentation.searchbooks.model.SearchScreenOrigin.ADD_GOAL
import com.konopelko.booksgoals.presentation.searchbooks.model.SearchScreenOrigin.ADD_WISH_BOOK
import com.konopelko.booksgoals.domain.usecase.addbook.AddBookUseCase
import com.konopelko.booksgoals.domain.usecase.searchbooks.SearchBooksUseCase
import com.konopelko.booksgoals.presentation.common.base.BaseViewModel
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksIntent.OnArgsReceived
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksIntent.OnBookClicked
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksIntent.OnSearchBooks
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksIntent.OnSearchTextChanged
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksUiState.PartialSearchBooksState
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksUiState.PartialSearchBooksState.BookSavedSuccessfully
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksUiState.PartialSearchBooksState.NavigateToAddGoalState
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksUiState.PartialSearchBooksState.SearchInProgress
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksUiState.PartialSearchBooksState.SearchResultsReceived
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksUiState.PartialSearchBooksState.SearchTextChanged
import com.konopelko.booksgoals.presentation.searchbooks.model.SearchBooksArgs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchBooksViewModel(
    initialState: SearchBooksUiState,
    private val searchBooksUseCase: SearchBooksUseCase,
    private val addBookUseCase: AddBookUseCase
) : BaseViewModel<SearchBooksIntent, SearchBooksUiState, PartialSearchBooksState>(
    initialState = initialState
) {

    //todo: move to uiState
    internal var screenOrigin: SearchScreenOrigin = ADD_GOAL

    override fun acceptIntent(intent: SearchBooksIntent) = when (intent) {
        is OnArgsReceived -> onArgsReceived(intent.args)
        is OnSearchTextChanged -> onSearchTextChanged(intent.text)
        is OnSearchBooks -> onSearchBooks(intent.text)
        is OnBookClicked -> onBookClicked(intent.book)
    }

    override fun mapUiState(
        previousState: SearchBooksUiState,
        partialState: PartialSearchBooksState
    ): SearchBooksUiState = when (partialState) {
        BookSavedSuccessfully -> previousState.copy(shouldNavigateToWishesScreen = true)
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
        is NavigateToAddGoalState -> previousState.copy(
            bookToCreateGoal = partialState.book,
            shouldNavigateToAddGoalScreen = true
        )
    }

    private fun onArgsReceived(args: SearchBooksArgs) {
        Log.e("SearchBooksViewModel", "args received: $args")
        screenOrigin = args.screenOrigin
    }

    private fun onBookClicked(book: Book) {
        when(screenOrigin) {
            ADD_GOAL -> updateUiState(NavigateToAddGoalState(book))
            ADD_WISH_BOOK -> onAddWishBookClicked(book)
        }
    }

    private fun onAddWishBookClicked(book: Book) {
        viewModelScope.launch(Dispatchers.IO) {
            addBookUseCase(book).onSuccess {
                updateUiState(BookSavedSuccessfully)
            }.onError {
                Log.e("SearchBooksViewModel", "error occurred when saving a book: ${it.exception}")
            }
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
            searchBooksUseCase(text).onSuccess { books ->
                Log.e("SearchBooksViewModel", "updateUiState called")
                updateUiState(SearchResultsReceived(searchResultBooks = books))
            }.onError {
                Log.e("SearchBooksViewModel", "error occurred when searching books")
                it.exception.printStackTrace()
            }
        }
    }

    private fun onSearchTextChanged(text: String) {
        updateUiState(SearchTextChanged(text))
    }

    companion object {
        const val ARGS_SCREEN_ORIGIN_KEY = "screen_origin"
    }
}