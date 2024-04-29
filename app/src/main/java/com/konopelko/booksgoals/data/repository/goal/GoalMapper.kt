package com.konopelko.booksgoals.data.repository.goal

import com.konopelko.booksgoals.data.database.entity.goal.GoalEntity
import com.konopelko.booksgoals.domain.model.goal.Goal

fun GoalEntity.toDomainModel(): Goal = Goal(
    id = id,
    bookId = bookId,
    bookName = bookName,
    bookAuthor = bookAuthorName,
    bookPublishYear = bookPublishYear.toInt(),
    bookPagesAmount = bookPagesAmount,
    completedPagesAmount = pagesCompletedAmount,
    daysInProgress = goalInProgressDaysAmount,
    progress = getGoalProgress(
        bookPagesAmount,
        pagesCompletedAmount
    ),
    averageReadSpeed = averageReadSpeed.toInt(),
    expectedPagesPerDay = expectedPagesPerDay,
    expectedFinishDaysAmount = expectedFinishDaysAmount
)

fun Goal.toDatabaseModel(): GoalEntity = GoalEntity(
    bookId = bookId,
    bookName = bookName,
    bookAuthorName = bookAuthor,
    bookPublishYear = bookPublishYear.toString(),
    bookPagesAmount = bookPagesAmount,
    pagesCompletedAmount = completedPagesAmount,
    goalInProgressDaysAmount = daysInProgress,
    expectedPagesPerDay = expectedPagesPerDay,
    expectedFinishDaysAmount = expectedFinishDaysAmount
)

private fun getGoalProgress(
    bookPagesAmount: Int,
    pagesCompletedAmount: Int
): Int = ((pagesCompletedAmount / bookPagesAmount).toFloat() * 100).toInt()