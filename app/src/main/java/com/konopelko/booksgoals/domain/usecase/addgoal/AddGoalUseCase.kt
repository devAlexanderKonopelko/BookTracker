package com.konopelko.booksgoals.domain.usecase.addgoal

import com.konopelko.booksgoals.data.database.entity.goal.GoalEntity
import com.konopelko.booksgoals.data.utils.Result
import com.konopelko.booksgoals.domain.repository.goal.GoalRepository

class AddGoalUseCase(
    private val goalRepository: GoalRepository
) {

    suspend operator fun invoke(vararg goals: GoalEntity): Result<Unit> =
        goalRepository.addGoal(goals = goals)
}