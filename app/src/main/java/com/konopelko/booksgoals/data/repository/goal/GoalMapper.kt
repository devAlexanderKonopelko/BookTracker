package com.konopelko.booksgoals.data.repository.goal

import com.konopelko.booksgoals.data.database.entity.goal.GoalEntity
import com.konopelko.booksgoals.domain.model.goal.Goal

fun GoalEntity.toDomainModel(): Goal = Goal(
    id = id,
    bookName = bookName,
    bookAuthor = bookAuthorName,
    bookPublishYear = bookPublishYear.toInt(),
    bookPagesAmount = bookPagesAmount,
    progress = getGoalProgress(
        bookPagesAmount,
        pagesCompletedAmount
    )
)

private fun getGoalProgress(
    bookPagesAmount: Int,
    pagesCompletedAmount: Int
): Int = ((pagesCompletedAmount / bookPagesAmount).toFloat() * 100).toInt()