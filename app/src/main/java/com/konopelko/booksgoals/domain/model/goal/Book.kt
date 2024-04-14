package com.konopelko.booksgoals.domain.model.goal

data class Book(
    val title: String = "",
    val authorName: String = "",
    val publishYear: String = "",
    val pagesAmount: String = "",
    val isFinished: Boolean = false
)
