package com.konopelko.booksgoals.data.database.dao.goal

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.konopelko.booksgoals.data.database.entity.goal.GoalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {

    @Query("SELECT * FROM goals")
    fun getGoals(): Flow<List<GoalEntity>>

    @Insert
    fun addGoals(vararg goalsList: GoalEntity)

    @Query("DELETE FROM goals WHERE id = :goalId")
    fun deleteGoal(goalId: Int)
}