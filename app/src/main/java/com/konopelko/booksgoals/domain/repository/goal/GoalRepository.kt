package com.konopelko.booksgoals.domain.repository.goal

import com.konopelko.booksgoals.data.database.entity.goal.GoalEntity
import com.konopelko.booksgoals.data.utils.Result
import com.konopelko.booksgoals.domain.model.goal.Goal
import kotlinx.coroutines.flow.Flow

interface GoalRepository {

    //todo: use result
    suspend fun getGoals(): Flow<List<Goal>>

    suspend fun addGoal(vararg goals: GoalEntity): Result<Unit>

    suspend fun deleteGoal(goalId: Int): Result<Unit>
}