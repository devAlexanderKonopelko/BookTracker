package com.konopelko.booksgoals.domain.repository.book

import com.konopelko.booksgoals.data.utils.Result
import com.konopelko.booksgoals.domain.model.book.Book

interface BookRepository {

    suspend fun addBook(book: Book): Result<Int>

    suspend fun getFinishedBooks(): Result<List<Book>>

    suspend fun getWishesBooks(): Result<List<Book>>

    suspend fun deleteBook(bookId: Int): Result<Unit>

    suspend fun updateBookIsStarted(isStarted: Boolean, bookId: Int): Result<Unit>

    suspend fun updateBookIsFinished(isFinished: Boolean, bookId: Int, finishDate: String): Result<Unit>
}