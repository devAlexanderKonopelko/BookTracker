package com.konopelko.booksgoals.data.api

import com.konopelko.booksgoals.data.api.query.common.FIELD_PAGE
import com.konopelko.booksgoals.data.api.query.common.FIELD_PAGE_LIMIT
import com.konopelko.booksgoals.data.api.query.common.FIELD_PAGE_LIMIT_VALUE
import com.konopelko.booksgoals.data.api.query.searchbooks.searchBooksQueryFields
import com.konopelko.booksgoals.data.api.response.searchbooks.SearchBooksResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BooksApi {

    @GET("search.json?")
    suspend fun searchBooks(
        //todo: implement paging
//        @Query(FIELD_PAGE) pageQuery: Int = 1,
        @Query(FIELD_PAGE_LIMIT) limitQuery: Int = FIELD_PAGE_LIMIT_VALUE,
        @Query("q") searchQuery: String,
        @Query("fields") fieldsQuery: String = searchBooksQueryFields,
    ): Response<SearchBooksResponse>
}