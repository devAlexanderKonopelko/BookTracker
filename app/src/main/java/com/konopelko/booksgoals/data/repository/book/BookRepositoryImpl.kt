package com.konopelko.booksgoals.data.repository.book

import com.konopelko.booksgoals.data.database.dao.book.BookDao
import com.konopelko.booksgoals.data.utils.Result
import com.konopelko.booksgoals.data.utils.databaseCall
import com.konopelko.booksgoals.domain.model.book.Book
import com.konopelko.booksgoals.domain.repository.book.BookRepository

class BookRepositoryImpl(
    private val bookDao: BookDao
) : BookRepository {

    override suspend fun addBook(book: Book): Result<Int> = databaseCall {
        bookDao.addBook(book.toDatabaseModel()).toInt()
    }

    override suspend fun getFinishedBooks(): Result<List<Book>> = databaseCall {
        bookDao.getFinishedBooks().map { it.toDomainModel() }
    }

    override suspend fun getWishesBooks(): Result<List<Book>> = databaseCall {
        bookDao.getNotStartedUnfinishedBooks().map { it.toDomainModel() }
    }

    override suspend fun deleteBook(bookId: Int): Result<Unit> = databaseCall {
        bookDao.deleteBook(bookId)
    }

    override suspend fun updateBookIsStarted(
        isStarted: Boolean,
        bookId: Int
    ): Result<Unit> = databaseCall {
        bookDao.updateBookIsStarted(
            isStarted = isStarted,
            bookId = bookId
        )
    }

    override suspend fun updateBookIsFinished(
        isFinished: Boolean,
        bookId: Int,
        finishDate: String
    ): Result<Unit> = databaseCall {
        bookDao.updateBookIsFinished(
            isFinished = isFinished,
            bookId = bookId,
            finishDate = finishDate
        )
    }
}