package com.konopelko.booksgoals.presentation.addgoal

import com.konopelko.booksgoals.data.api.response.searchbooks.SearchBooksResponse.BookResponse

sealed interface AddGoalIntent {

    object OnCreateGoalClicked : AddGoalIntent

    data class OnArgsReceived(val args: BookResponse?) : AddGoalIntent
    data class OnPagesPerDayChanged(val pagesPerDay: Int) : AddGoalIntent

    sealed interface AddGoalNavigationIntent {

        object NavigateToSearchBooksScreen : AddGoalNavigationIntent
        object NavigateToGoalsScreen : AddGoalNavigationIntent
    }
}