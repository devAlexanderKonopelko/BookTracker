package com.konopelko.booksgoals.domain.usecase.getfinishedbooks

import com.konopelko.booksgoals.data.utils.Result
import com.konopelko.booksgoals.domain.model.book.Book
import com.konopelko.booksgoals.domain.repository.book.BookRepository

class GetFinishedBooksUseCase(
    private val bookRepository: BookRepository
) {

    suspend operator fun invoke(): Result<List<Book>> =
        bookRepository.getFinishedBooks()
}