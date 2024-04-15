package com.konopelko.booksgoals.data.repository.searchbooks

import android.util.Log
import com.konopelko.booksgoals.data.api.BooksApi
import com.konopelko.booksgoals.data.utils.Result
import com.konopelko.booksgoals.data.utils.apiCall
import com.konopelko.booksgoals.domain.model.book.Book
import com.konopelko.booksgoals.domain.repository.searchbooks.SearchBooksRepository
import kotlinx.coroutines.Dispatchers

class SearchBooksRepositoryImpl(
    private val api: BooksApi
): SearchBooksRepository {

    override suspend fun searchBooks(searchText: String): Result<List<Book>> =
        apiCall(Dispatchers.IO) {
            Log.e("API", "searchText = $searchText")
            Log.e("API", "searchQuery = ${prepareSearchQuery(searchText)}")

            api.searchBooks(searchQuery = prepareSearchQuery(searchText))
                .body()
                ?.books?.map {
                    it.toDomainModel()
                } ?: emptyList()
        }

    private fun prepareSearchQuery(searchText: String): String =
        searchText.replace(" ", "+")
}