package com.konopelko.booksgoals.domain.usecase.updategoalpagesperday

import com.konopelko.booksgoals.data.utils.Result
import com.konopelko.booksgoals.domain.repository.goal.GoalRepository

class UpdateGoalExpectedParamsUseCase(
    private val repository: GoalRepository
) {

    suspend operator fun invoke(
        goalId: Int,
        pagesPerDay: Int,
        daysToFinish: Int
    ): Result<Unit> =
        repository.updateGoalPagesPerDay(
            goalId = goalId,
            pagesPerDay = pagesPerDay,
            daysToFinish = daysToFinish
        )
}