package com.konopelko.booksgoals.domain.usecase.getgoal

import com.konopelko.booksgoals.data.utils.Result
import com.konopelko.booksgoals.domain.model.goal.Goal
import com.konopelko.booksgoals.domain.repository.goal.GoalRepository

class GetGoalUseCase(
    private val repository: GoalRepository
) {

    suspend operator fun invoke(goalId: Int): Result<Goal> =
        repository.getGoalById(goalId)
}