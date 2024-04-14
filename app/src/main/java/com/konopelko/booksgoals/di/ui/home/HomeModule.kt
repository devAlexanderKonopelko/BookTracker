package com.konopelko.booksgoals.di.ui.home

import com.konopelko.booksgoals.domain.usecase.deletegoal.DeleteGoalUseCase
import com.konopelko.booksgoals.domain.usecase.getgoals.GetGoalsUseCase
import com.konopelko.booksgoals.presentation.goals.GoalsUiState
import com.konopelko.booksgoals.presentation.goals.GoalsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {

    factory {
        GetGoalsUseCase(
            goalRepository = get()
        )
    }

    factory {
        DeleteGoalUseCase(
            repository = get()
        )
    }

    viewModel {
        GoalsViewModel(
            initialState = GoalsUiState(),
            getGoalsUseCase = get(),
            deleteGoalUseCase = get()
        )
    }
}