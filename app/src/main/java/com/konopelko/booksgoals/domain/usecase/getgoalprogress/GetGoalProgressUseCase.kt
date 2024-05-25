package com.konopelko.booksgoals.domain.usecase.getgoalprogress

import com.konopelko.booksgoals.data.utils.Result
import com.konopelko.booksgoals.domain.model.progress.ProgressMark
import com.konopelko.booksgoals.domain.repository.progress.ProgressRepository

class GetGoalProgressUseCase(
    private val repository: ProgressRepository
) {

    suspend operator fun invoke(goalId: Int): Result<List<ProgressMark>> =
        repository.getGoalProgress(goalId)
}
