package com.konopelko.booksgoals.data.repository.book

import com.konopelko.booksgoals.data.database.dao.book.BookDao
import com.konopelko.booksgoals.data.database.entity.book.BookEntity
import com.konopelko.booksgoals.data.utils.Result
import com.konopelko.booksgoals.data.utils.databaseCall
import com.konopelko.booksgoals.domain.model.goal.Book
import com.konopelko.booksgoals.domain.repository.book.BookRepository

class BookRepositoryImpl(
    private val bookDao: BookDao
) : BookRepository {

    override suspend fun addBook(book: Book): Result<Unit> = databaseCall {
        bookDao.addBook(book.toDatabaseModel())
    }
}