package com.konopelko.booksgoals.presentation.addgoal

import com.konopelko.booksgoals.presentation.addgoal.model.AddGoalArgs

sealed interface AddGoalIntent {

    object OnCreateGoalClicked : AddGoalIntent

    data class OnArgsReceived(val args: AddGoalArgs?) : AddGoalIntent
    data class OnPagesPerDayChanged(val pagesPerDay: Int) : AddGoalIntent

    sealed interface AddGoalNavigationIntent {

        object NavigateToSearchBooksScreen : AddGoalNavigationIntent
        object NavigateToGoalsScreen : AddGoalNavigationIntent
        object NavigateToGoalDetailsScreen : AddGoalNavigationIntent
    }
}