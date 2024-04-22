package com.konopelko.booksgoals.presentation.addgoal

import com.konopelko.booksgoals.domain.model.book.Book

data class AddGoalUiState(
    val selectedBook: Book? = null,
    val daysToFinishGoal: Int = 0,
    val isAddGoalButtonEnabled: Boolean = false,
    val isSavingGoal: Boolean = false,
    val isGoalSaved: Boolean = false
) {

    sealed interface AddGoalPartialState {

        data class BookSelected(val selectedBook: Book) : AddGoalPartialState
        data class PagesPerDayChanged(val newPagesPerDay: Int) : AddGoalPartialState

        data class SavingGoalState(
            val isLoading: Boolean,
            val isGoalSaved: Boolean = false
        ) : AddGoalPartialState
    }
}
