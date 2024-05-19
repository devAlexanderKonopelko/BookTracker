package com.konopelko.booksgoals.presentation.addgoal

import com.konopelko.booksgoals.domain.model.book.Book

data class AddGoalUiState(
    val selectedBook: Book = Book(),
    val daysToFinishGoal: Int = 0,
    val isAddGoalButtonEnabled: Boolean = false,
    val isSavingGoal: Boolean = false,
    val isGoalSaved: Boolean = false,
    val isSelectBookButtonEnabled: Boolean = true,
    val shouldNavigateToGoalDetailsScreen: Boolean = false,
    val selectedPagesPerDay: Int = 20
) {

    sealed interface AddGoalPartialState {

        data object ShouldNavigateToGoalDetailsState : AddGoalPartialState

        data class BookSelected(val selectedBook: Book) : AddGoalPartialState
        data class PagesPerDayChanged(val newPagesPerDay: Int) : AddGoalPartialState
        data class EditGoalState(val selectedPagesPerDay: Int) : AddGoalPartialState

        data class SavingGoalState(
            val isLoading: Boolean,
            val isGoalSaved: Boolean = false
        ) : AddGoalPartialState
    }
}
