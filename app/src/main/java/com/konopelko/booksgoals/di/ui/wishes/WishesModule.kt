package com.konopelko.booksgoals.di.ui.wishes

import com.konopelko.booksgoals.domain.usecase.deletebook.DeleteBookUseCase
import com.konopelko.booksgoals.domain.usecase.getwishesbooks.GetWishesBooksUseCase
import com.konopelko.booksgoals.presentation.wishes.WishesUiState
import com.konopelko.booksgoals.presentation.wishes.WishesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val wishesModule = module {

    factory {
        GetWishesBooksUseCase(
            bookRepository = get()
        )
    }

    factory {
        DeleteBookUseCase(
            bookRepository = get()
        )
    }

    viewModel {
        WishesViewModel(
            initialState = WishesUiState(),
            getWishesBooksUseCase = get(),
            deleteBookUseCase = get()
        )
    }
}