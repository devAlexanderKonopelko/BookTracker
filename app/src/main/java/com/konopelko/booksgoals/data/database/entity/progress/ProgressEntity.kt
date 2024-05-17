package com.konopelko.booksgoals.data.database.entity.progress

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val PROGRESS_TABLE_NAME = "progress"

@Entity(tableName = PROGRESS_TABLE_NAME)
data class ProgressEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "goal_id")
    val goalId: Int,

    @ColumnInfo(name = "date")
    val date: String,

    @ColumnInfo(name = "book_is_finished")
    val isBookFinished: Boolean,

    @ColumnInfo(name = "page_count")
    val pagesAmount: Int
)