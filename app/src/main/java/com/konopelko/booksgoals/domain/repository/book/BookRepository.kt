package com.konopelko.booksgoals.domain.repository.book

import com.konopelko.booksgoals.data.utils.Result
import com.konopelko.booksgoals.domain.model.goal.Book

interface BookRepository {

    suspend fun addBook(book: Book): Result<Unit>
}