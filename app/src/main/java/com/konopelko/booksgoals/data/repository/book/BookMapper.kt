package com.konopelko.booksgoals.data.repository.book

import com.konopelko.booksgoals.data.database.entity.book.BookEntity
import com.konopelko.booksgoals.domain.model.book.Book

fun Book.toDatabaseModel(): BookEntity = BookEntity(
    bookName = title,
    bookAuthorName = authorName,
    bookPublishYear = publishYear,
    bookPagesAmount = pagesAmount.toInt(),
    coverUrl = coverUrl,
    isFinished = isFinished,
    isStarted = isStarted,
    finishDate = finishedDate
)

fun BookEntity.toDomainModel(): Book = Book(
    id = id,
    title = bookName,
    authorName = bookAuthorName,
    publishYear = bookPublishYear,
    coverUrl = coverUrl,
    pagesAmount = bookPagesAmount.toString(),
    isFinished = isFinished,
    isStarted = isStarted,
    finishedDate = finishDate
)