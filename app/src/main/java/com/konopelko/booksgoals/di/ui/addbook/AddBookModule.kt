package com.konopelko.booksgoals.di.ui.addbook

import com.konopelko.booksgoals.presentation.addbook.AddBookUiState
import com.konopelko.booksgoals.presentation.addbook.AddBookViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val addBookModule = module {

    viewModel {
        AddBookViewModel(
            initialState = AddBookUiState()
        )
    }
}