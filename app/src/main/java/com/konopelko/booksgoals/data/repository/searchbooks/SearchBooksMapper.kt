package com.konopelko.booksgoals.data.repository.searchbooks

import com.konopelko.booksgoals.data.api.response.searchbooks.SearchBooksResponse.BookResponse
import com.konopelko.booksgoals.domain.model.book.Book

fun BookResponse.toDomainModel() : Book = Book(
    title = title,
    authorName = authorName?.joinToString(separator = ", ") ?: "Unknown Author",
    publishYear = publishYear.toString(),
    pagesAmount = pagesAmount.toString()
)