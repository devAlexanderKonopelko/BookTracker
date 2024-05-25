package com.konopelko.booksgoals

import android.app.Application
import com.konopelko.booksgoals.di.database.databaseModule
import com.konopelko.booksgoals.di.network.networkModule
import com.konopelko.booksgoals.di.repository.repositoryModule
import com.konopelko.booksgoals.di.ui.addbook.addBookModule
import com.konopelko.booksgoals.di.ui.addgoal.addGoalModule
import com.konopelko.booksgoals.di.ui.finishedbooks.finishedBooksModule
import com.konopelko.booksgoals.di.ui.goaldetails.goalDetailsModule
import com.konopelko.booksgoals.di.ui.goalstatistics.goalStatisticsModule
import com.konopelko.booksgoals.di.ui.home.homeModule
import com.konopelko.booksgoals.di.ui.searchbooks.searchBooksModule
import com.konopelko.booksgoals.di.ui.totalstatistics.totalStatisticsModule
import com.konopelko.booksgoals.di.ui.wishes.wishesModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

//-------------------------------
/*
 Книги нужны для:
 - Отображения в [Wishes] - неначатые, незавершённые книги. +
 - Отображения в [FinishedBooks] - завершённые книги. +

 При добавлении книги в Wishes (через Search/AddBook):
 - добавляем книгу в бд +

 При создании [Goal] через AddGoal:
 - Если создаём цель через [Goals -> AddGoal]: добавляем книгу в бд с [isStarted] = true +
 - Если создаём цель через [Wishes -> StartGoal]: обновляем [isStarted] = true книги в бд +

 При завершении [Goal] через Goals:
 - Обновляем [isFinished] книги в true (книга переходит в [FinishedBooks]) +

 При удалении [Goal] через Goals:
 - Удаляем цель из бд +
 - Удаляем книгу из бд? //todo

 */
//-------------------------------
//todo
/*
 Данные добавляем в бд [Statistics]. Каждый раз, когда юзер:
 - отмечает прочитанные страницы
 - завершает цель (добавить недостающие страницы, если завершает досрочно)

 Statistics
 [Date, IsGoalFinished, GoalId, PagesAmount, IsBookFinished]

 При отображении данные берём из Statictics:
 (в цикле): Statistics -> QUERY (SELECT IsBookFinished = 1 WHERE [Date] in [range of values])
 (в цикле): Statistics -> QUERY (SELECT PagesAmount WHERE [Date] in [range of values])
 [range of values] - неделя/месяц/год




//todo
 Добавить кнопку-ссылку на апи.

 //todo
 - Дефолтная картинка на книги без обложки
 - Там, где нет автора - показывать что-то типа "Неизвестный автор"
 - Увеличить page на SearchBooks до 100
 - Обновить весь UI

 //todo
 Обновить бд с учётом нормализации.

 */
//-------------------------------

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
                wishesModule,
                goalDetailsModule,
                goalStatisticsModule,
                totalStatisticsModule
            )
        }
    }
}