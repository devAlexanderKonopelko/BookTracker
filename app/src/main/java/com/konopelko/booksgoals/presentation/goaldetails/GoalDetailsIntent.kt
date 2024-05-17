package com.konopelko.booksgoals.presentation.goaldetails

sealed interface GoalDetailsIntent {

    data object OnAddProgressClicked : GoalDetailsIntent
    data object OnEditGoalClicked : GoalDetailsIntent
    data object OnBookStatisticsClicked : GoalDetailsIntent
    data object OnCloseProgressMarkDialog : GoalDetailsIntent

    data class OnArgsReceived(val goalId: Int) : GoalDetailsIntent
    data class OnSaveProgressClicked(val pagesAmount: Int) : GoalDetailsIntent

    sealed interface GoalDetailsNavigationIntent {

    }
}