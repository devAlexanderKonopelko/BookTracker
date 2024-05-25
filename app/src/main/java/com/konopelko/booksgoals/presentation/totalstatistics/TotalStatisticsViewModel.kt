package com.konopelko.booksgoals.presentation.totalstatistics

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.konopelko.booksgoals.domain.model.progress.ProgressMark
import com.konopelko.booksgoals.domain.usecase.gettotalstatistics.GetTotalStatisticsUseCase
import com.konopelko.booksgoals.domain.utils.progressmaks.prepareProgressMarksInScale
import com.konopelko.booksgoals.presentation.common.base.BaseViewModel
import com.konopelko.booksgoals.presentation.goalstatistics.model.ProgressMarkUiModel
import com.konopelko.booksgoals.presentation.goalstatistics.model.StatisticsScale
import com.konopelko.booksgoals.presentation.goalstatistics.model.StatisticsScale.MONTH
import com.konopelko.booksgoals.presentation.goalstatistics.model.StatisticsScale.WEEK
import com.konopelko.booksgoals.presentation.goalstatistics.model.StatisticsScale.YEAR
import com.konopelko.booksgoals.presentation.totalstatistics.TotalStatisticsIntent.SelectedScaleChanged
import com.konopelko.booksgoals.presentation.totalstatistics.TotalStatisticsIntent.StatisticsTabChanged
import com.konopelko.booksgoals.presentation.totalstatistics.TotalStatisticsUiState.PartialState
import com.konopelko.booksgoals.presentation.totalstatistics.TotalStatisticsUiState.PartialState.InitialDataLoaded
import com.konopelko.booksgoals.presentation.totalstatistics.model.TotalStatisticsData
import com.konopelko.booksgoals.presentation.totalstatistics.model.TotalStatisticsTab
import com.konopelko.booksgoals.presentation.totalstatistics.model.TotalStatisticsTab.BOOKS
import com.konopelko.booksgoals.presentation.totalstatistics.model.TotalStatisticsTab.PAGES
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class TotalStatisticsViewModel(
    initialState: TotalStatisticsUiState,
    private val getTotalStatisticsUseCase: GetTotalStatisticsUseCase
) : BaseViewModel<TotalStatisticsIntent, TotalStatisticsUiState, PartialState>(
    initialState = initialState
) {

    private var totalStatisticsList: List<ProgressMark> = emptyList()

    private val monthDays = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)

    init {
        Log.e("TotalStatisticsViewModel", "init")
        loadInitialData()
    }

    override fun acceptIntent(intent: TotalStatisticsIntent) = when (intent) {
        is StatisticsTabChanged -> onStatisticsTabChanged(intent.tab)
        is SelectedScaleChanged -> onSelectedScaleChanged(intent.scale)
    }

    override fun mapUiState(
        previousState: TotalStatisticsUiState,
        partialState: PartialState
    ): TotalStatisticsUiState = when (partialState) {
        is PartialState.StatisticsTabChanged -> previousState.copy(
            selectedTab = partialState.selectedTab,
            visibleProgressMarks = partialState.visibleProgressMarks,
            totalStatisticsData = prepareTotalStatisticsData(
                visibleProgressMarks = partialState.visibleProgressMarks,
                selectedTab = partialState.selectedTab,
                scale = previousState.selectedScale
            )
        )

        is PartialState.StatisticsScaleChanged -> previousState.copy(
            selectedScale = partialState.selectedScale,
            visibleProgressMarks = partialState.visibleProgressMarks,
            totalStatisticsData = prepareTotalStatisticsData(
                visibleProgressMarks = partialState.visibleProgressMarks,
                selectedTab = uiState.value.selectedTab,
                scale = partialState.selectedScale
            )
        )

        is InitialDataLoaded -> previousState.copy(
            visibleProgressMarks = partialState.visibleProgressMarks,
            totalStatisticsData = prepareTotalStatisticsData(
                visibleProgressMarks = partialState.visibleProgressMarks,
                selectedTab = uiState.value.selectedTab,
                scale = uiState.value.selectedScale
            )
        )
    }

    private fun loadInitialData() {
        viewModelScope.launch(Dispatchers.IO) {
            getTotalStatisticsUseCase().onSuccess { progressMarks ->
                totalStatisticsList = progressMarks

                val visibleProgressMarks = prepareProgressMarksList(
                    selectedTab = uiState.value.selectedTab,
                    selectedScale = uiState.value.selectedScale,
                    progressMarks = progressMarks
                )

                updateUiState(InitialDataLoaded(visibleProgressMarks = visibleProgressMarks))
            }.onError {
                Log.e("TotalStatisticsViewModel", "error loading total statistics: ${it.exception}")
            }
        }
    }

    private fun prepareProgressMarksList(
        selectedTab: TotalStatisticsTab,
        selectedScale: StatisticsScale,
        progressMarks: List<ProgressMark>
    ): List<ProgressMarkUiModel> = when (selectedTab) {
        PAGES -> progressMarks.prepareProgressMarksInScale(selectedScale)
        BOOKS -> progressMarks.prepareProgressMarksInScale(
            scale = selectedScale,
            isBooksFinishedMarks = true
        )
    }

    private fun prepareTotalStatisticsData(
        visibleProgressMarks: List<ProgressMarkUiModel>,
        selectedTab: TotalStatisticsTab,
        scale: StatisticsScale
    ): TotalStatisticsData = TotalStatisticsData(
        statisticsTab = selectedTab,
        statisticsScale = scale,
        totalUnitsRead = visibleProgressMarks.sumOf { it.progress },
        averageReadSpeed = calculateAverageReadSpeed(
            visibleProgressMarks = visibleProgressMarks,
            scale = scale
        ),
        goalsAchieved = calculateGoalsAchieved(
            visibleProgressMarks = visibleProgressMarks,
            selectedTab = selectedTab,
            scale = scale
        )
    )

    private fun calculateAverageReadSpeed(
        visibleProgressMarks: List<ProgressMarkUiModel>,
        scale: StatisticsScale
    ): Int = visibleProgressMarks.sumOf{ it.progress } / when(scale) {
        WEEK -> 7 // average pages or books per day
        MONTH -> monthDays // average pages or books per day
        YEAR -> 12 // average pages or books per month
    }

    private fun calculateGoalsAchieved(
        visibleProgressMarks: List<ProgressMarkUiModel>,
        selectedTab: TotalStatisticsTab,
        scale: StatisticsScale
    ): Int = when(selectedTab) {
        PAGES -> prepareProgressMarksList(
            // intentionally set BOOKS to get isBookFinished values represented as [progress] values
            selectedTab = BOOKS,
            selectedScale = scale,
            progressMarks = totalStatisticsList
        ).sumOf { it.progress }
        BOOKS -> visibleProgressMarks.sumOf { it.progress }
    }

    private fun onStatisticsTabChanged(tab: TotalStatisticsTab) {
        updateUiState(
            PartialState.StatisticsTabChanged(
                selectedTab = tab,
                visibleProgressMarks = prepareProgressMarksList(
                    selectedTab = tab,
                    selectedScale = uiState.value.selectedScale,
                    progressMarks = totalStatisticsList
                )
            )
        )
    }

    private fun onSelectedScaleChanged(scale: StatisticsScale) {
        updateUiState(
            PartialState.StatisticsScaleChanged(
                selectedScale = scale,
                visibleProgressMarks = prepareProgressMarksList(
                    selectedTab = uiState.value.selectedTab,
                    selectedScale = scale,
                    progressMarks = totalStatisticsList
                )
            )
        )
    }
}