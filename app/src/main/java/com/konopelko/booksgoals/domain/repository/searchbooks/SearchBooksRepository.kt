package com.konopelko.booksgoals.domain.repository.searchbooks

import com.konopelko.booksgoals.data.api.response.searchbooks.SearchBooksResponse

interface SearchBooksRepository {

    suspend fun searchBooks(searchText: String): SearchBooksResponse
}