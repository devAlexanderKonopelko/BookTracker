package com.konopelko.booksgoals.di.ui.totalstatistics

import com.konopelko.booksgoals.domain.usecase.gettotalstatistics.GetTotalStatisticsUseCase
import com.konopelko.booksgoals.presentation.totalstatistics.TotalStatisticsUiState
import com.konopelko.booksgoals.presentation.totalstatistics.TotalStatisticsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val totalStatisticsModule = module {

    factory {
        GetTotalStatisticsUseCase(repository = get())
    }

    viewModel {
        TotalStatisticsViewModel(
            initialState = TotalStatisticsUiState(),
            getTotalStatisticsUseCase = get()
        )
    }
}