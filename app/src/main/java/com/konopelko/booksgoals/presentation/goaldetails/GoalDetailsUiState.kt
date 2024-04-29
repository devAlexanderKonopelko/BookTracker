package com.konopelko.booksgoals.presentation.goaldetails

import com.konopelko.booksgoals.domain.model.goal.Goal

data class GoalDetailsUiState(
    val goal: Goal = Goal()
) {

    sealed interface GoalDetailsPartialState {

        data class GoalDataLoaded(val goal: Goal) : GoalDetailsPartialState
    }
}
