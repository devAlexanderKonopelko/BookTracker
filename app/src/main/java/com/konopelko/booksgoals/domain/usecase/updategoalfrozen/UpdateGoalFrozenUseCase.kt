package com.konopelko.booksgoals.domain.usecase.updategoalfrozen

import com.konopelko.booksgoals.data.utils.Result
import com.konopelko.booksgoals.domain.repository.goal.GoalRepository

class UpdateGoalFrozenUseCase(
    private val repository: GoalRepository
) {

    suspend operator fun invoke(
        goalId: Int,
        isFrozen: Boolean
    ): Result<Unit> = repository.updateGoalFrozen(goalId, isFrozen)
}