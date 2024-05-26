package com.konopelko.booksgoals.data.repository.goal

import com.konopelko.booksgoals.data.database.entity.goal.GoalEntity
import com.konopelko.booksgoals.domain.model.book.Book
import com.konopelko.booksgoals.domain.model.goal.Goal
import java.util.Calendar
import kotlin.math.roundToInt

fun GoalEntity.toDomainModel(): Goal = Goal(
    id = id,
    bookId = bookId,
    bookName = bookName,
    bookAuthor = bookAuthorName,
    bookPublishYear = bookPublishYear.toInt(),
    bookPagesAmount = bookPagesAmount,
    bookCoverUrl = bookCoverUrl,
    completedPagesAmount = pagesCompletedAmount,
    creationDate = goalCreationDate,
    daysInProgress = getDaysInProgress(goalCreationDate),
    progress = getGoalProgress(
        bookPagesAmount,
        pagesCompletedAmount
    ),
    expectedPagesPerDay = expectedPagesPerDay,
    expectedFinishDaysAmount = expectedFinishDaysAmount,
    isFrozen = isFrozen
)

fun Goal.toDatabaseModel(): GoalEntity = GoalEntity(
    bookId = bookId,
    bookName = bookName,
    bookAuthorName = bookAuthor,
    bookPublishYear = bookPublishYear.toString(),
    bookPagesAmount = bookPagesAmount,
    pagesCompletedAmount = completedPagesAmount,
    expectedPagesPerDay = expectedPagesPerDay,
    expectedFinishDaysAmount = expectedFinishDaysAmount,
    goalCreationDate = creationDate,
    bookCoverUrl = bookCoverUrl,
    isFrozen = isFrozen
)

private fun getGoalProgress(
    bookPagesAmount: Int,
    pagesCompletedAmount: Int
): Int = ((pagesCompletedAmount / bookPagesAmount.toFloat()) * 100).toInt()


private fun getDaysInProgress(goalCreationDate: String): Int {
    val dateTimeDifference = Calendar.getInstance().timeInMillis - goalCreationDate.toLong()
    val daysAmount = (dateTimeDifference.toFloat() / (1000 * 60 * 60 * 24)).roundToInt()
    return if(daysAmount < 1) 1 else daysAmount
}