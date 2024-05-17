package com.konopelko.booksgoals.domain.usecase.addprogressmark

import com.konopelko.booksgoals.data.utils.Result
import com.konopelko.booksgoals.domain.model.progress.ProgressMark
import com.konopelko.booksgoals.domain.repository.progress.ProgressRepository

class AddProgressMarkUseCase(
    private val repository: ProgressRepository
) {
    suspend operator fun invoke(progressMark: ProgressMark): Result<Unit> =
        repository.addGoalProgress(progressMark)
}