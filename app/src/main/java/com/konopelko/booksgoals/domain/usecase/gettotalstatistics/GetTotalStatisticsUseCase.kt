package com.konopelko.booksgoals.domain.usecase.gettotalstatistics

import com.konopelko.booksgoals.data.utils.Result
import com.konopelko.booksgoals.domain.model.progress.ProgressMark
import com.konopelko.booksgoals.domain.repository.progress.ProgressRepository

class GetTotalStatisticsUseCase(
    private val repository: ProgressRepository
) {

    suspend operator fun invoke(): Result<List<ProgressMark>> =
        repository.getTotalStatistics()
}