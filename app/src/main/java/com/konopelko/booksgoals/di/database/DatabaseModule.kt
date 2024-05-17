package com.konopelko.booksgoals.di.database

import androidx.room.Room
import com.konopelko.booksgoals.data.database.BooksGoalsDatabase
import com.konopelko.booksgoals.data.database.DATABASE_NAME
import com.konopelko.booksgoals.data.database.dao.goal.GoalDao
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {

    single {
        Room.databaseBuilder(
            context = androidApplication(),
            klass = BooksGoalsDatabase::class.java,
            name = DATABASE_NAME
        ).build()
    }

    single {
        get<BooksGoalsDatabase>().goalDao()
    }

    single {
        get<BooksGoalsDatabase>().bookDao()
    }

    single {
        get<BooksGoalsDatabase>().progressDao()
    }
}