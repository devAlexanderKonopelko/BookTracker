package com.konopelko.booksgoals.domain.usecase.getgoals

import com.konopelko.booksgoals.domain.model.goal.Goal
import com.konopelko.booksgoals.domain.repository.goal.GoalRepository
import kotlinx.coroutines.flow.Flow

class GetGoalsUseCase(
    private val goalRepository: GoalRepository
) {

    suspend operator fun invoke(): Flow<List<Goal>> = goalRepository.getGoals()
}