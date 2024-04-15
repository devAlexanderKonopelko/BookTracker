package com.konopelko.booksgoals.presentation.addgoal

import com.konopelko.booksgoals.domain.model.book.Book

sealed interface AddGoalIntent {

    object OnCreateGoalClicked : AddGoalIntent

    data class OnArgsReceived(val args: Book?) : AddGoalIntent
    data class OnPagesPerDayChanged(val pagesPerDay: Int) : AddGoalIntent

    sealed interface AddGoalNavigationIntent {

        object NavigateToSearchBooksScreen : AddGoalNavigationIntent
        object NavigateToGoalsScreen : AddGoalNavigationIntent
    }
}