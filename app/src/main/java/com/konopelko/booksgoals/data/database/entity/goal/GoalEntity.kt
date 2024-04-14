package com.konopelko.booksgoals.data.database.entity.goal

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val GOAL_TABLE_NAME = "goals"

@Entity(tableName = GOAL_TABLE_NAME)
data class GoalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "book_name")
    val bookName: String,

    @ColumnInfo(name = "book_author_name")
    val bookAuthorName: String,

    @ColumnInfo(name = "book_publish_year")
    val bookPublishYear: String,

    @ColumnInfo(name = "book_pages_amount")
    val bookPagesAmount: Int,

    @ColumnInfo(name = "pages_completed_amount")
    val pagesCompletedAmount: Int = 0,

    @ColumnInfo(name = "goal_in_progress_days_amount")
    val goalInProgressDaysAmount: Int = 1,

    @ColumnInfo(name = "average_read_speed")
    val averageReadSpeed: Float = 0f,

    @ColumnInfo(name = "expected_pages_per_day")
    val expectedPagesPerDay: Int,

    @ColumnInfo(name = "expected_finish_days_amount")
    val expectedFinishDaysAmount: Float
)
