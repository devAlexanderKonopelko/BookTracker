package com.konopelko.booksgoals.domain.usecase.updategoalprogress

import com.konopelko.booksgoals.data.utils.Result
import com.konopelko.booksgoals.domain.repository.goal.GoalRepository

class UpdateGoalProgressUseCase(
    private val repository: GoalRepository
) {

    suspend operator fun invoke(
        goalId: Int,
        pagesReadAmount: Int
    ): Result<Unit> = repository.updateGoalProgress(
        goalId = goalId,
        pagesReadAmount = pagesReadAmount
    )
}