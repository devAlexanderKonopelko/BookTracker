package com.konopelko.booksgoals.presentation.goals

import android.os.Parcelable
import com.konopelko.booksgoals.domain.model.goal.Goal
import kotlinx.parcelize.Parcelize

@Parcelize
data class GoalsUiState(
    val goals: List<Goal> = emptyList()
): Parcelable {

    sealed interface PartialGoalsState {

        data class GoalsUpdated(val goals: List<Goal>) : PartialGoalsState
    }
}
