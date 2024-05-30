package com.konopelko.booksgoals.presentation.addgoal

import com.konopelko.booksgoals.presentation.addgoal.model.AddGoalArgs
import com.konopelko.booksgoals.presentation.addgoal.model.SelectBookOption

sealed interface AddGoalIntent {

    data object OnCreateGoalClicked : AddGoalIntent

    data class OnArgsReceived(val args: AddGoalArgs?) : AddGoalIntent
    data class OnPagesPerDayChanged(val pagesPerDay: Int) : AddGoalIntent

    sealed interface AddGoalNavigationIntent {

        data object NavigateToSearchBooksScreen : AddGoalNavigationIntent
        data object NavigateToAddBookScreen : AddGoalNavigationIntent
        data object NavigateToWishesScreen : AddGoalNavigationIntent
        data object NavigateToGoalsScreen : AddGoalNavigationIntent
        data object NavigateToGoalDetailsScreen : AddGoalNavigationIntent
    }
}