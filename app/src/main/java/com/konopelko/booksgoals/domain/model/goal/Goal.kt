package com.konopelko.booksgoals.domain.model.goal

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Goal(
    val id: Int = 0,
    val bookId: Int = 0,
    val bookName: String = "",
    val bookAuthor: String = "",
    val bookPublishYear: Int = 0,
    val bookPagesAmount: Int = 0,
    val completedPagesAmount: Int = 0,
    val averageReadSpeed: Int = 0,
    val daysInProgress: Int = 1,
    val expectedPagesPerDay: Int = 0,
    val expectedFinishDaysAmount: Int = 0,
    val progress: Int = 0
) : Parcelable
