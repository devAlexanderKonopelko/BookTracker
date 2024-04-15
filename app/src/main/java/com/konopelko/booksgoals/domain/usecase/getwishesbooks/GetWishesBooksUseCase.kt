package com.konopelko.booksgoals.domain.usecase.getwishesbooks

import com.konopelko.booksgoals.data.utils.Result
import com.konopelko.booksgoals.domain.model.book.Book
import com.konopelko.booksgoals.domain.repository.book.BookRepository

class GetWishesBooksUseCase(
    private val bookRepository: BookRepository
) {

    suspend operator fun invoke(): Result<List<Book>> =
        bookRepository.getWishesBooks()
}