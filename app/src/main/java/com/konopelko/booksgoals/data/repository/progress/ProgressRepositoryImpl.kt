package com.konopelko.booksgoals.data.repository.progress

import com.konopelko.booksgoals.data.database.dao.progress.ProgressDao
import com.konopelko.booksgoals.data.utils.Result
import com.konopelko.booksgoals.data.utils.databaseCall
import com.konopelko.booksgoals.domain.model.progress.ProgressMark
import com.konopelko.booksgoals.domain.repository.progress.ProgressRepository

class ProgressRepositoryImpl(
    private val progressDao: ProgressDao
): ProgressRepository {

    override suspend fun getGoalProgress(goalId: Int): Result<List<ProgressMark>> = databaseCall {
        progressDao.getProgressByGoalId(goalId).map { it.toDomainModel() }
    }

    override suspend fun addGoalProgress(progressMark: ProgressMark): Result<Unit> = databaseCall {
        progressDao.addProgress(progress = progressMark.toDatabaseModel())
    }

    override suspend fun getTotalStatistics(): Result<List<ProgressMark>> = databaseCall {
        progressDao.getTotalStatistics().map { it.toDomainModel() }
    }
}