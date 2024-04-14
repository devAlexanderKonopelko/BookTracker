package com.konopelko.booksgoals.domain.usecase.deletegoal

import com.konopelko.booksgoals.data.utils.Result
import com.konopelko.booksgoals.domain.repository.goal.GoalRepository

class DeleteGoalUseCase(
    private val repository: GoalRepository
) {

    suspend operator fun invoke(goalId: Int): Result<Unit> =
        repository.deleteGoal(goalId)
}