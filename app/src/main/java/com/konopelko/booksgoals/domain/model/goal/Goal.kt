package com.konopelko.booksgoals.domain.model.goal

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Goal(
    val id: Int = 0,
    val bookName: String = "",
    val bookAuthor: String = "",
    val publishYear: Int = 0,
    val progress: Int = 0
) : Parcelable
