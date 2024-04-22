package com.konopelko.booksgoals.domain.usecase.updatebookisstarted

import com.konopelko.booksgoals.data.utils.Result
import com.konopelko.booksgoals.domain.repository.book.BookRepository

class UpdateBookIsStartedUseCase(
    private val bookRepository: BookRepository
) {

    suspend operator fun invoke(isStarted: Boolean, bookId: Int): Result<Unit> =
        bookRepository.updateBookIsStarted(isStarted ,bookId)
}