package com.konopelko.booksgoals.presentation.goals

import com.konopelko.booksgoals.domain.model.goal.Goal
import com.konopelko.booksgoals.presentation.goals.model.GoalMenuOption

sealed interface GoalsIntent {

    data object HideGoalCompletedMessage : GoalsIntent

    data class OnArgsReceived(val args: Boolean) : GoalsIntent
    data class OnGoalOptionClicked(
        val goalMenuOption: GoalMenuOption,
        val goal: Goal
    ) : GoalsIntent

    sealed interface GoalsNavigationIntent {

        object NavigateToAddGoalScreen : GoalsNavigationIntent
    }
}