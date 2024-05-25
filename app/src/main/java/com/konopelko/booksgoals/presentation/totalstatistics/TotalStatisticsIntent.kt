package com.konopelko.booksgoals.presentation.totalstatistics

import com.konopelko.booksgoals.presentation.goalstatistics.model.StatisticsScale
import com.konopelko.booksgoals.presentation.totalstatistics.model.TotalStatisticsTab

sealed interface TotalStatisticsIntent {

    data class StatisticsTabChanged(val tab: TotalStatisticsTab): TotalStatisticsIntent
    data class SelectedScaleChanged(val scale: StatisticsScale) : TotalStatisticsIntent

    sealed interface TotalStatisticsNavigationIntent {

    }
}