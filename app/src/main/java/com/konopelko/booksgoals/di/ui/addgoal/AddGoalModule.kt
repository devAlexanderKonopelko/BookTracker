package com.konopelko.booksgoals.di.ui.addgoal

import com.konopelko.booksgoals.domain.usecase.addbook.AddBookUseCase
import com.konopelko.booksgoals.domain.usecase.addgoal.AddGoalUseCase
import com.konopelko.booksgoals.domain.usecase.updatebookisstarted.UpdateBookIsStartedUseCase
import com.konopelko.booksgoals.presentation.addgoal.AddGoalUiState
import com.konopelko.booksgoals.presentation.addgoal.AddGoalViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val addGoalModule = module {

    factory {
        AddGoalUseCase(goalRepository = get())
    }

    factory {
        UpdateBookIsStartedUseCase(bookRepository = get())
    }

    factory {
        AddBookUseCase(bookRepository = get())
    }

    viewModel {
        AddGoalViewModel(
            initialState = AddGoalUiState(),
            addGoalUseCase = get(),
            addBookUseCase = get(),
            updateBookIsStartedUseCase = get()
        )
    }
}