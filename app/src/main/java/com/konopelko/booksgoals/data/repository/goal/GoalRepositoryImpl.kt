package com.konopelko.booksgoals.data.repository.goal

import com.konopelko.booksgoals.data.database.dao.goal.GoalDao
import com.konopelko.booksgoals.data.utils.Result
import com.konopelko.booksgoals.data.utils.databaseCall
import com.konopelko.booksgoals.domain.model.goal.Goal
import com.konopelko.booksgoals.domain.repository.goal.GoalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GoalRepositoryImpl(
    private val goalDao: GoalDao
): GoalRepository {

    //todo: refactor, remove [Flow] and use databaseCall()
    override suspend fun getGoals(): Flow<List<Goal>> =
        goalDao.getGoals().map { goals ->
            goals.map { it.toDomainModel() }
        }

    override suspend fun addGoal(goal: Goal): Result<Unit> = databaseCall {
        goalDao.addGoal(goal = goal.toDatabaseModel())
    }

    override suspend fun deleteGoal(goalId: Int): Result<Unit> = databaseCall {
        goalDao.deleteGoal(goalId = goalId)
    }
}