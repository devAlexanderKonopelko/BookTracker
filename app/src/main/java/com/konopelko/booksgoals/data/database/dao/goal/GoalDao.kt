package com.konopelko.booksgoals.data.database.dao.goal

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.konopelko.booksgoals.data.database.entity.goal.GoalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {

    @Query("SELECT * FROM goals")
    fun getGoals(): Flow<List<GoalEntity>> //todo: refactor, remove [Flow]

    @Query("SELECT * FROM goals WHERE id = :goalId")
    fun getGoalById(goalId: Int): GoalEntity

    @Insert
    fun addGoal(goal: GoalEntity)

    @Query("UPDATE goals SET pages_completed_amount = :pagesCompletedAmount WHERE id = :goalId")
    fun updateGoalProgress(
        goalId: Int,
        pagesCompletedAmount: Int
    )

    @Query(
        "UPDATE goals " +
        "SET expected_pages_per_day = :pagesPerDay, " +
        "expected_finish_days_amount = :daysToFinish " +
        "WHERE id = :goalId"
    )
    fun updateGoalExpectedParams(
        goalId: Int,
        pagesPerDay: Int,
        daysToFinish: Int
    )

    @Query("DELETE FROM goals WHERE id = :goalId")
    fun deleteGoal(goalId: Int)
}