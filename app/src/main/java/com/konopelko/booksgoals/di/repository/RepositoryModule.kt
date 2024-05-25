package com.konopelko.booksgoals.di.repository

import com.konopelko.booksgoals.data.repository.book.BookRepositoryImpl
import com.konopelko.booksgoals.data.repository.goal.GoalRepositoryImpl
import com.konopelko.booksgoals.data.repository.progress.ProgressRepositoryImpl
import com.konopelko.booksgoals.data.repository.searchbooks.SearchBooksRepositoryImpl
import com.konopelko.booksgoals.domain.repository.book.BookRepository
import com.konopelko.booksgoals.domain.repository.goal.GoalRepository
import com.konopelko.booksgoals.domain.repository.progress.ProgressRepository
import com.konopelko.booksgoals.domain.repository.searchbooks.SearchBooksRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<SearchBooksRepository> {
        SearchBooksRepositoryImpl(api = get())
    }

    single<GoalRepository> {
        GoalRepositoryImpl(
            goalDao = get(),
            bookDao = get()
        )
    }

    single<BookRepository> {
        BookRepositoryImpl(bookDao = get())
    }

    single<ProgressRepository> {
        ProgressRepositoryImpl(progressDao = get())
    }
}