package com.konopelko.booksgoals.domain.usecase.getgoalprogress

import android.util.Log
import com.konopelko.booksgoals.data.utils.Result
import com.konopelko.booksgoals.domain.repository.progress.ProgressRepository

class   GetGoalAverageReadSpeedUseCase(
    private val repository: ProgressRepository
) {

    suspend operator fun invoke(goalId: Int): Result<Int> =
        repository.getGoalProgress(goalId)
            .map { result ->
                with(result.value) {
                    Log.e("GetGoalAverageReadSpeedUseCase", "list sumOf { it.pagesAmount }: ${sumOf { it.pagesAmount }}")
                    Log.e("GetGoalAverageReadSpeedUseCase", "list size: $size")
                    if(isEmpty()) Result.Success(0)
                    else Result.Success(sumOf { it.pagesAmount } / size)
                }
            }
}