package com.konopelko.booksgoals.di.ui.addbook

import com.konopelko.booksgoals.domain.usecase.addbook.AddBookUseCase
import com.konopelko.booksgoals.presentation.addbook.AddBookUiState
import com.konopelko.booksgoals.presentation.addbook.AddBookViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val addBookModule = module {

    factory {
        AddBookUseCase(
            bookRepository = get()
        )
    }

    viewModel {
        AddBookViewModel(
            initialState = AddBookUiState(),
            addBookUseCase = get()
        )
    }
}