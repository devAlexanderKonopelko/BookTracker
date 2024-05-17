package com.konopelko.booksgoals.domain.repository.goal

import com.konopelko.booksgoals.data.utils.Result
import com.konopelko.booksgoals.domain.model.goal.Goal
import kotlinx.coroutines.flow.Flow

interface GoalRepository {

    //todo: use result
    suspend fun getGoals(): Flow<List<Goal>>

    suspend fun addGoal(goal: Goal): Result<Unit>

    suspend fun deleteGoal(goalId: Int): Result<Unit>

    suspend fun getGoalById(goalId: Int): Result<Goal>

    suspend fun updateGoalProgress(
        goalId: Int,
        pagesReadAmount: Int
    ): Result<Unit>
}