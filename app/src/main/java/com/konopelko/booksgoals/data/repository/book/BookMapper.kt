package com.konopelko.booksgoals.data.repository.book

import com.konopelko.booksgoals.data.database.entity.book.BookEntity
import com.konopelko.booksgoals.domain.model.goal.Book

fun Book.toDatabaseModel(): BookEntity = BookEntity(
    bookName = title,
    bookAuthorName = authorName,
    bookPublishYear = publishYear,
    bookPagesAmount = pagesAmount.toInt()
)