package com.konopelko.booksgoals.domain.model.book

data class Book(
    val title: String = "",
    val authorName: String = "",
    val publishYear: String = "",
    val pagesAmount: String = "",
    val isFinished: Boolean = false
)
