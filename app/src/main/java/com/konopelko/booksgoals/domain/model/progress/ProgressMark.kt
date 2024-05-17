package com.konopelko.booksgoals.domain.model.progress

data class ProgressMark(
    val goalId: Int,
    val date: String,
    val isBookFinished: Boolean,
    val pagesAmount: Int
)
