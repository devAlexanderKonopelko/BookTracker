package com.konopelko.booksgoals.domain.usecase.addbook

import com.konopelko.booksgoals.data.utils.Result
import com.konopelko.booksgoals.domain.model.goal.Book
import com.konopelko.booksgoals.domain.repository.book.BookRepository

class AddBookUseCase(
    private val bookRepository: BookRepository
) {

    suspend operator fun invoke(book: Book): Result<Unit> =
        bookRepository.addBook(book)
}