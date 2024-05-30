package com.konopelko.booksgoals.presentation.addgoal

import com.konopelko.booksgoals.domain.model.book.Book
import com.konopelko.booksgoals.presentation.addgoal.model.AddGoalScreenOrigin
import com.konopelko.booksgoals.presentation.addgoal.model.AddGoalScreenOrigin.GOALS

data class AddGoalUiState(
    val selectedBook: Book = Book(),
    val screenOrigin: AddGoalScreenOrigin = GOALS,
    val daysToFinishGoal: Int = 0,
    val isAddGoalButtonEnabled: Boolean = false,
    val isSavingGoal: Boolean = false,
    val isGoalSaved: Boolean = false,
    val isSelectBookButtonEnabled: Boolean = true,
    val shouldNavigateToGoalDetailsScreen: Boolean = false,
    val selectedPagesPerDay: Int = 1
) {

    sealed interface AddGoalPartialState {

        data object ShouldNavigateToGoalDetailsState : AddGoalPartialState

        data class BookSelected(
            val selectedBook: Book,
            val screenOrigin: AddGoalScreenOrigin,
            val isSelectBookButtonEnabled: Boolean
        ) : AddGoalPartialState

        data class PagesPerDayChanged(
            val newPagesPerDay: Int,
            val screenOrigin: AddGoalScreenOrigin
        ) : AddGoalPartialState

        data class EditGoalState(
            val selectedPagesPerDay: Int,
            val screenOrigin: AddGoalScreenOrigin,
            val selectedBook: Book
        ) : AddGoalPartialState

        data class SavingGoalState(
            val isLoading: Boolean,
            val isGoalSaved: Boolean = false
        ) : AddGoalPartialState
    }
}
