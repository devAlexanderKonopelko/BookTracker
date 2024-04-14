package com.konopelko.booksgoals.domain.repository.book

import com.konopelko.booksgoals.data.utils.Result
import com.konopelko.booksgoals.domain.model.book.Book

interface BookRepository {

    suspend fun addBook(book: Book): Result<Unit>

    suspend fun getFinishedBooks(): Result<List<Book>>
}