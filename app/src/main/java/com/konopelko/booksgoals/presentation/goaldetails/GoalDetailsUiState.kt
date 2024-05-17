package com.konopelko.booksgoals.presentation.goaldetails

import com.konopelko.booksgoals.domain.model.goal.Goal

data class GoalDetailsUiState(
    val goal: Goal = Goal(),
    val showMarkProgressDialog: Boolean = false
) {

    sealed interface GoalDetailsPartialState {

        data class AverageReadSpeedChanged(val readSpeed: Int) : GoalDetailsPartialState
        data class PagesCompletedChanged(val pagesCompleted: Int) : GoalDetailsPartialState
        data class ProgressMarkDialogVisibilityChanged(val isVisible: Boolean) :
            GoalDetailsPartialState

        data class GoalDataLoaded(val goal: Goal) : GoalDetailsPartialState
    }
}
