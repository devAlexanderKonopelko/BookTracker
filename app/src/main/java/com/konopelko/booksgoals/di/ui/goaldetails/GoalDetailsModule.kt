package com.konopelko.booksgoals.di.ui.goaldetails

import com.konopelko.booksgoals.domain.usecase.addprogressmark.AddProgressMarkUseCase
import com.konopelko.booksgoals.domain.usecase.getgoal.GetGoalUseCase
import com.konopelko.booksgoals.domain.usecase.getgoalaveragereadspeed.GetGoalAverageReadSpeedUseCase
import com.konopelko.booksgoals.domain.usecase.updategoalprogress.UpdateGoalProgressUseCase
import com.konopelko.booksgoals.presentation.goaldetails.GoalDetailsUiState
import com.konopelko.booksgoals.presentation.goaldetails.GoalDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val goalDetailsModule = module {

    factory {
        GetGoalUseCase(repository = get())
    }

    factory {
        GetGoalAverageReadSpeedUseCase(repository = get())
    }

    factory {
        UpdateGoalProgressUseCase(repository = get())
    }

    factory {
        AddProgressMarkUseCase(repository = get())
    }

    viewModel {
        GoalDetailsViewModel(
            initialState = GoalDetailsUiState(),
            getGoalUseCase = get(),
            getGoalAverageReadSpeedUseCase = get(),
            updateGoalProgressUseCase = get(),
            addProgressMarkUseCase = get()
        )
    }
}