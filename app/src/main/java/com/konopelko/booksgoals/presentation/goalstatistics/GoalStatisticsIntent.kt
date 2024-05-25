package com.konopelko.booksgoals.presentation.goalstatistics

import com.konopelko.booksgoals.presentation.goalstatistics.model.StatisticsScale

sealed interface GoalStatisticsIntent {

    data class ArgsReceived(val goalId: Int): GoalStatisticsIntent
    data class SelectedScaleChanged(val scale: StatisticsScale) : GoalStatisticsIntent

    sealed interface GoalStatisticsNavigationIntent {

    }
}