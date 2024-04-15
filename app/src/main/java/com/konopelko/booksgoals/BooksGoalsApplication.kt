package com.konopelko.booksgoals

import android.app.Application
import com.konopelko.booksgoals.di.database.databaseModule
import com.konopelko.booksgoals.di.network.networkModule
import com.konopelko.booksgoals.di.repository.repositoryModule
import com.konopelko.booksgoals.di.ui.addbook.addBookModule
import com.konopelko.booksgoals.di.ui.addgoal.addGoalModule
import com.konopelko.booksgoals.di.ui.finishedbooks.finishedBooksModule
import com.konopelko.booksgoals.di.ui.home.homeModule
import com.konopelko.booksgoals.di.ui.searchbooks.searchBooksModule
import com.konopelko.booksgoals.di.ui.wishes.wishesModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BooksGoalsApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@BooksGoalsApplication)

            modules(
                // base modules
                networkModule,
                databaseModule,
                repositoryModule,

                // ui related modules
                homeModule,
                searchBooksModule,
                addGoalModule,
                addBookModule,
                finishedBooksModule,
                wishesModule
            )
        }
    }
}