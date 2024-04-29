package com.konopelko.booksgoals.presentation.goaldetails

sealed interface GoalDetailsIntent {

    data object OnAddProgressClicked : GoalDetailsIntent
    data object OnEditGoalClicked : GoalDetailsIntent
    data object OnBookStatisticsClicked: GoalDetailsIntent

    data class OnArgsReceived(val goalId: Int): GoalDetailsIntent

    sealed interface GoalDetailsNavigationIntent {

    }
}