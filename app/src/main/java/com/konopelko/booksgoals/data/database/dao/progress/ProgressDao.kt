package com.konopelko.booksgoals.data.database.dao.progress

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.konopelko.booksgoals.data.database.entity.progress.ProgressEntity

@Dao
interface ProgressDao {

    @Insert
    fun addProgress(progress: ProgressEntity)

    @Query("SELECT * FROM progress WHERE goal_id = :goalId")
    fun getProgressByGoalId(goalId: Int): List<ProgressEntity>

    @Query("SELECT * FROM progress")
    fun getTotalStatistics(): List<ProgressEntity>
}