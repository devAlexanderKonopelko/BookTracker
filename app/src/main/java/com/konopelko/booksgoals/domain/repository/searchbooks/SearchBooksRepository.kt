package com.konopelko.booksgoals.domain.repository.searchbooks

import com.konopelko.booksgoals.data.utils.Result
import com.konopelko.booksgoals.domain.model.book.Book

interface SearchBooksRepository {

    suspend fun searchBooks(searchText: String): Result<List<Book>>
}