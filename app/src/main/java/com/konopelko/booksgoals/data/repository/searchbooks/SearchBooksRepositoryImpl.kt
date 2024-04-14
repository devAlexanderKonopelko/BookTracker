package com.konopelko.booksgoals.data.repository.searchbooks

import android.util.Log
import com.konopelko.booksgoals.data.api.BooksApi
import com.konopelko.booksgoals.data.api.response.searchbooks.SearchBooksResponse
import com.konopelko.booksgoals.domain.repository.searchbooks.SearchBooksRepository

class SearchBooksRepositoryImpl(
    private val api: BooksApi
): SearchBooksRepository {

    override suspend fun searchBooks(searchText: String): SearchBooksResponse {
        Log.e("API", "searchText = $searchText")
        Log.e("API", "searchQuery = ${prepareSearchQuery(searchText)}")
        return api.searchBooks(searchQuery = prepareSearchQuery(searchText)).body()!!
    }

    private fun prepareSearchQuery(searchText: String): String =
        searchText.replace(" ", "+")
}