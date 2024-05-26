package com.konopelko.booksgoals.presentation.goals

import android.os.Parcelable
import com.konopelko.booksgoals.domain.model.goal.Goal
import kotlinx.parcelize.Parcelize

@Parcelize
data class GoalsUiState(
    val goals: List<Goal> = emptyList(),
    val showGoalCompletedMessage: Boolean = false
): Parcelable {

    sealed interface PartialGoalsState {
        data object GoalCompletedMessageHidden : PartialGoalsState

        data class GoalsUpdated(val goals: List<Goal>) : PartialGoalsState
        data class GoalCompletedSuccessfullyState(val goalId: Int) : PartialGoalsState
        data class GoalIsFrozenChanged(val goalId: Int, val isFrozen: Boolean) : PartialGoalsState
    }
}
