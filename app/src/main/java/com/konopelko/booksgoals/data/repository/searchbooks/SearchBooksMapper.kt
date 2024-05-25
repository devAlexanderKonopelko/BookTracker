package com.konopelko.booksgoals.data.repository.searchbooks

import com.konopelko.booksgoals.data.api.API_HOST_COVERS
import com.konopelko.booksgoals.data.api.response.searchbooks.SearchBooksResponse.BookResponse
import com.konopelko.booksgoals.domain.model.book.Book

fun BookResponse.toDomainModel(): Book = Book(
    title = title,
    authorName = authorName?.joinToString(separator = ", ") ?: "Unknown Author",
    publishYear = publishYear.toString(),
    pagesAmount = pagesAmount.toString(),
    coverUrl = prepareCoverUrl(
        isbn = isbn,
        coverId = coverId,
        coverEditionKey = coverEditionKey
    )
)

private fun prepareCoverUrl(
    isbn: List<String>?,
    coverId: Int?,
    coverEditionKey: String?
): String = when {
    !isbn.isNullOrEmpty() -> API_HOST_COVERS + "isbn/${isbn.first()}-M.jpg"
    coverId != null && coverId > 0 -> API_HOST_COVERS + "id/$coverId-M.jpg"
    !coverEditionKey.isNullOrEmpty() -> API_HOST_COVERS + "olid/$coverEditionKey-M.jpg"
    else -> ""
}
