package com.konopelko.booksgoals.domain.usecase.deletebook

import com.konopelko.booksgoals.data.utils.Result
import com.konopelko.booksgoals.domain.repository.book.BookRepository

class DeleteBookUseCase(
    private val bookRepository: BookRepository
) {

    suspend operator fun invoke(bookId: Int): Result<Unit> =
        bookRepository.deleteBook(bookId)
}