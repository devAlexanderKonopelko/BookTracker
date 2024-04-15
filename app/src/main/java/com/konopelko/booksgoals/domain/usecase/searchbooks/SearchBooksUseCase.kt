package com.konopelko.booksgoals.domain.usecase.searchbooks

import com.konopelko.booksgoals.data.utils.Result
import com.konopelko.booksgoals.domain.model.book.Book
import com.konopelko.booksgoals.domain.repository.searchbooks.SearchBooksRepository

class SearchBooksUseCase(
    private val searchBooksRepository: SearchBooksRepository
) {

    suspend operator fun invoke(searchText: String): Result<List<Book>> =
        searchBooksRepository.searchBooks(searchText)
}