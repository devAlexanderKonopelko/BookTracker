package com.konopelko.booksgoals.data.repository.progress

import com.konopelko.booksgoals.data.database.entity.progress.ProgressEntity
import com.konopelko.booksgoals.domain.model.progress.ProgressMark

internal fun ProgressEntity.toDomainModel(): ProgressMark = ProgressMark(
    goalId = goalId,
    date = date,
    isBookFinished = isBookFinished,
    pagesAmount = pagesAmount
)

internal fun ProgressMark.toDatabaseModel(): ProgressEntity = ProgressEntity(
    goalId = goalId,
    date = date,
    isBookFinished = isBookFinished,
    pagesAmount = pagesAmount
)