package com.konopelko.booksgoals.data.repository.book

import com.konopelko.booksgoals.data.database.dao.book.BookDao
import com.konopelko.booksgoals.data.utils.Result
import com.konopelko.booksgoals.data.utils.databaseCall
import com.konopelko.booksgoals.domain.model.book.Book
import com.konopelko.booksgoals.domain.repository.book.BookRepository

class BookRepositoryImpl(
    private val bookDao: BookDao
) : BookRepository {

    override suspend fun addBook(book: Book): Result<Unit> = databaseCall {
        bookDao.addBook(book.toDatabaseModel())
    }

    override suspend fun getFinishedBooks(): Result<List<Book>> = databaseCall {
        bookDao.getFinishedBooks().map { it.toDomainModel() }
    }

    override suspend fun getWishesBooks(): Result<List<Book>> = databaseCall {
        bookDao.getUnfinishedBooks().map { it.toDomainModel() }
    }

    override suspend fun deleteBook(bookId: Int): Result<Unit> = databaseCall {
        bookDao.deleteBook(bookId)
    }
}