package com.konopelko.booksgoals.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.konopelko.booksgoals.data.database.dao.book.BookDao
import com.konopelko.booksgoals.data.database.dao.goal.GoalDao
import com.konopelko.booksgoals.data.database.entity.book.BookEntity
import com.konopelko.booksgoals.data.database.entity.goal.GoalEntity

const val DATABASE_NAME = "books_goals_database"
const val DATABASE_VERSION = 1

@Database(
    entities = [
        GoalEntity::class,
        BookEntity::class
    ],
    version = DATABASE_VERSION,
    exportSchema = false
)
abstract class BooksGoalsDatabase : RoomDatabase() {

    abstract fun goalDao(): GoalDao

    abstract fun bookDao(): BookDao
}