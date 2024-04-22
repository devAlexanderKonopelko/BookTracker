package com.konopelko.booksgoals.domain.usecase.updatebookisfinished

import com.konopelko.booksgoals.data.utils.Result
import com.konopelko.booksgoals.domain.repository.book.BookRepository

class UpdateBookIsFinishedUseCase(
    private val repository: BookRepository
) {

    suspend operator fun invoke(isFinished: Boolean, bookId: Int): Result<Unit> =
        repository.updateBookIsFinished(isFinished ,bookId)
}