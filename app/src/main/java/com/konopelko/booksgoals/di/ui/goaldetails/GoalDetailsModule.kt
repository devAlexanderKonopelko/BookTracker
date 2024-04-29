package com.konopelko.booksgoals.di.ui.goaldetails

import com.konopelko.booksgoals.domain.usecase.getgoal.GetGoalUseCase
import com.konopelko.booksgoals.presentation.goaldetails.GoalDetailsUiState
import com.konopelko.booksgoals.presentation.goaldetails.GoalDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val goalDetailsModule = module {

    factory {
        GetGoalUseCase(repository = get())
    }

    viewModel {
        GoalDetailsViewModel(
            initialState = GoalDetailsUiState(),
            getGoalUseCase = get()
        )
    }
}