package com.konopelko.booksgoals.di.ui.searchbooks

import com.konopelko.booksgoals.domain.usecase.addbook.AddBookUseCase
import com.konopelko.booksgoals.domain.usecase.searchbooks.SearchBooksUseCase
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksUiState
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchBooksModule = module {

    factory {
        SearchBooksUseCase(
            searchBooksRepository = get()
        )
    }

    factory {
        AddBookUseCase(
            bookRepository = get()
        )
    }

    viewModel {
        SearchBooksViewModel(
            initialState = SearchBooksUiState(),
            searchBooksUseCase = get(),
            addBookUseCase = get()
        )
    }
}