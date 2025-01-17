package com.konopelko.booksgoals.domain.model.book

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Book(
    val id: Int = -1,
    val title: String = "",
    val authorName: String = "",
    val publishYear: String = "", //todo: make Int
    val pagesAmount: String = "", //todo: make Int
    val coverUrl: String = "",
    val isStarted: Boolean = false,
    val isFinished: Boolean = false,
    val finishedDate: String = ""
): Parcelable
