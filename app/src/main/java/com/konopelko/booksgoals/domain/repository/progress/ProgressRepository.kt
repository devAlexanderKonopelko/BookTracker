package com.konopelko.booksgoals.domain.repository.progress

import com.konopelko.booksgoals.data.utils.Result
import com.konopelko.booksgoals.domain.model.progress.ProgressMark

interface ProgressRepository {

    suspend fun getGoalProgress(goalId: Int): Result<List<ProgressMark>>

    suspend fun addGoalProgress(progressMark: ProgressMark): Result<Unit>
}