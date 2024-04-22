package com.konopelko.booksgoals.domain.usecase.addgoal

import com.konopelko.booksgoals.data.utils.Result
import com.konopelko.booksgoals.domain.model.goal.Goal
import com.konopelko.booksgoals.domain.repository.goal.GoalRepository

class AddGoalUseCase(
    private val goalRepository: GoalRepository
) {

    suspend operator fun invoke(goal: Goal): Result<Unit> =
        goalRepository.addGoal(goal = goal)
}