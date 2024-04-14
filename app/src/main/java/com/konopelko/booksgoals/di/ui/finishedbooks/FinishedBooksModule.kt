package com.konopelko.booksgoals.di.ui.finishedbooks

import com.konopelko.booksgoals.domain.usecase.getfinishedbooks.GetFinishedBooksUseCase
import com.konopelko.booksgoals.presentation.finishedbooks.FinishedBooksUiState
import com.konopelko.booksgoals.presentation.finishedbooks.FinishedBooksViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val finishedBooksModule = module {

    factory {
        GetFinishedBooksUseCase(
            bookRepository = get()
        )
    }

    viewModel {
        FinishedBooksViewModel(
            initialState = FinishedBooksUiState(),
            getFinishedBooksUseCase = get()
        )
    }
}