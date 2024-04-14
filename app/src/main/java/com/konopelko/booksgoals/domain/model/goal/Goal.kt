package com.konopelko.booksgoals.domain.model.goal

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Goal(
    val id: Int,
    val bookName: String,
    val bookAuthor: String,
    val publishYear: Int,
    val progress: Int
) : Parcelable
