package com.konopelko.booksgoals.domain.usecase.searchbooks

import com.konopelko.booksgoals.data.api.response.searchbooks.SearchBooksResponse
import com.konopelko.booksgoals.domain.repository.searchbooks.SearchBooksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchBooksUseCase(
    private val searchBooksRepository: SearchBooksRepository
) {

    operator fun invoke(searchText: String): Flow<SearchBooksResponse> = flow {
        emit(searchBooksRepository.searchBooks(searchText))
    }
}