package com.konopelko.booksgoals

import android.app.Application
import com.konopelko.booksgoals.di.database.databaseModule
import com.konopelko.booksgoals.di.network.networkModule
import com.konopelko.booksgoals.di.repository.repositoryModule
import com.konopelko.booksgoals.di.ui.addbook.addBookModule
import com.konopelko.booksgoals.di.ui.addgoal.addGoalModule
import com.konopelko.booksgoals.di.ui.home.homeModule
import com.konopelko.booksgoals.di.ui.searchbooks.searchBooksModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BooksGoalsApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@BooksGoalsApplication)

            modules(
                networkModule,
                databaseModule,
                repositoryModule,
                homeModule,
                searchBooksModule,
                addGoalModule,
                addBookModule
            )
        }
    }
}