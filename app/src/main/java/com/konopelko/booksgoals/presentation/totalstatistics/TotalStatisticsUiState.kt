package com.konopelko.booksgoals.presentation.totalstatistics

import com.konopelko.booksgoals.presentation.goalstatistics.model.ProgressMarkUiModel
import com.konopelko.booksgoals.presentation.goalstatistics.model.StatisticsScale
import com.konopelko.booksgoals.presentation.totalstatistics.model.TotalStatisticsData
import com.konopelko.booksgoals.presentation.totalstatistics.model.TotalStatisticsTab

data class TotalStatisticsUiState(
    val selectedTab: TotalStatisticsTab = TotalStatisticsTab.PAGES,
    val selectedScale: StatisticsScale = StatisticsScale.WEEK,
    val visibleProgressMarks: List<ProgressMarkUiModel> = emptyList(),
    val totalStatisticsData: TotalStatisticsData = TotalStatisticsData()
) {

    sealed interface PartialState {

        data class InitialDataLoaded(val visibleProgressMarks: List<ProgressMarkUiModel>) : PartialState

        data class StatisticsTabChanged(
            val selectedTab: TotalStatisticsTab,
            val visibleProgressMarks: List<ProgressMarkUiModel>
        ) : PartialState

        data class StatisticsScaleChanged(
            val selectedScale: StatisticsScale,
            val visibleProgressMarks: List<ProgressMarkUiModel>
        ) : PartialState
    }
}
