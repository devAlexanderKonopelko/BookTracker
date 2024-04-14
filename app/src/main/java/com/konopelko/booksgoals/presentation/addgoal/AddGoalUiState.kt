package com.konopelko.booksgoals.presentation.addgoal

import com.konopelko.booksgoals.data.api.response.searchbooks.SearchBooksResponse.BookResponse

data class AddGoalUiState(
    val selectedBook: BookResponse? = null,
    val daysToFinishGoal: Int = 0,
    val isAddGoalButtonEnabled: Boolean = false,
    val isSavingGoal: Boolean = false,
    val isGoalSaved: Boolean = false
) {

    sealed interface AddGoalPartialState {

        data class BookSelected(val book: BookResponse) : AddGoalPartialState
        data class PagesPerDayChanged(val newPagesPerDay: Int) : AddGoalPartialState

        data class SavingGoalState(
            val isLoading: Boolean,
            val isGoalSaved: Boolean = false
        ) : AddGoalPartialState
    }
}
