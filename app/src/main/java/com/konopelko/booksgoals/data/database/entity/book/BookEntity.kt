package com.konopelko.booksgoals.data.database.entity.book

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val BOOK_TABLE_NAME = "books"

@Entity(tableName = BOOK_TABLE_NAME)
data class BookEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "title")
    val bookName: String,

    @ColumnInfo(name = "author_name")
    val bookAuthorName: String,

    @ColumnInfo(name = "publish_year")
    val bookPublishYear: String,

    @ColumnInfo(name = "pages_amount")
    val bookPagesAmount: Int,

    @ColumnInfo(name = "is_started")
    val isStarted: Boolean,

    @ColumnInfo(name = "is_finished")
    val isFinished: Boolean
)