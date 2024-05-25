package com.konopelko.booksgoals.di.ui.goalstatistics

import com.konopelko.booksgoals.domain.usecase.getgoal.GetGoalUseCase
import com.konopelko.booksgoals.domain.usecase.getgoalprogress.GetGoalProgressUseCase
import com.konopelko.booksgoals.presentation.goalstatistics.GoalStatisticsUiState
import com.konopelko.booksgoals.presentation.goalstatistics.GoalStatisticsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.factory
import org.koin.dsl.module

val goalStatisticsModule = module {
    factory {
        GetGoalProgressUseCase(repository = get())
    }
    factory {
        GetGoalUseCase(repository = get())
    }

    viewModel {
        GoalStatisticsViewModel(
            initialState = GoalStatisticsUiState(),
            getGoalProgressUseCase = get(),
            getGoalUseCase = get()
        )
    }
}