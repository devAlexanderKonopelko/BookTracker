package com.konopelko.booksgoals.presentation.goals

import com.konopelko.booksgoals.domain.model.goal.GoalMenuOption

sealed interface GoalsIntent {

    data class OnArgsReceived(val args: Boolean) : GoalsIntent
    data class OnGoalOptionClicked(
        val goalMenuOption: GoalMenuOption,
        val goalId: Int
    ) : GoalsIntent

    sealed interface GoalsNavigationIntent {

        object NavigateToAddGoalScreen : GoalsNavigationIntent
    }
}