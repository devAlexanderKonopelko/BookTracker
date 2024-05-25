package com.konopelko.booksgoals.data.repository.goal

import com.konopelko.booksgoals.data.database.dao.book.BookDao
import com.konopelko.booksgoals.data.database.dao.goal.GoalDao
import com.konopelko.booksgoals.data.utils.Result
import com.konopelko.booksgoals.data.utils.databaseCall
import com.konopelko.booksgoals.domain.model.goal.Goal
import com.konopelko.booksgoals.domain.repository.goal.GoalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip

class GoalRepositoryImpl(
    private val goalDao: GoalDao,
    private val bookDao: BookDao
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

    override suspend fun getGoalById(goalId: Int): Result<Goal> = databaseCall {
        goalDao.getGoalById(goalId).toDomainModel()
    }

    override suspend fun updateGoalProgress(goalId: Int, pagesReadAmount: Int): Result<Unit> =
        databaseCall {
            goalDao.updateGoalProgress(
                goalId = goalId,
                pagesCompletedAmount = pagesReadAmount
            )
        }

    override suspend fun updateGoalPagesPerDay(
        goalId: Int,
        pagesPerDay: Int,
        daysToFinish: Int
    ): Result<Unit> =
        databaseCall {
            goalDao.updateGoalExpectedParams(
                goalId = goalId,
                pagesPerDay = pagesPerDay,
                daysToFinish = daysToFinish
            )
        }
}