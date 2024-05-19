package com.konopelko.booksgoals.presentation.goaldetails

import com.konopelko.booksgoals.domain.model.goal.Goal

sealed interface GoalDetailsIntent {

    data object OnAddProgressClicked : GoalDetailsIntent
    data object OnBookStatisticsClicked : GoalDetailsIntent
    data object OnCloseProgressMarkDialog : GoalDetailsIntent

    data class OnArgsReceived(val goalId: Int) : GoalDetailsIntent
    data class OnSaveProgressClicked(val pagesAmount: Int) : GoalDetailsIntent

    sealed interface GoalDetailsNavigationIntent {

        data class NavigateToEditGoal(val goal: Goal) : GoalDetailsNavigationIntent
    }
}