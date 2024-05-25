package com.konopelko.booksgoals.data.api.response.searchbooks

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.konopelko.booksgoals.data.api.query.searchbooks.FIELD_AUTHOR_NAME
import com.konopelko.booksgoals.data.api.query.searchbooks.FIELD_COVER_EDITION_KEY
import com.konopelko.booksgoals.data.api.query.searchbooks.FIELD_COVER_ID
import com.konopelko.booksgoals.data.api.query.searchbooks.FIELD_ISBN
import com.konopelko.booksgoals.data.api.query.searchbooks.FIELD_NUMBER_OF_PAGES
import com.konopelko.booksgoals.data.api.query.searchbooks.FIELD_PUBLISH_YEAR
import com.konopelko.booksgoals.data.api.query.searchbooks.FIELD_TITLE
import kotlinx.parcelize.Parcelize

data class SearchBooksResponse(
    @SerializedName("docs")
    val books: List<BookResponse>
) {

    //todo: replace [BookResponse] usage in presentation with [Book] domain model
    @Parcelize
    data class BookResponse(
        @SerializedName(FIELD_TITLE)
        val title: String,

        @SerializedName(FIELD_AUTHOR_NAME)
        val authorName: List<String>? = emptyList(),

        @SerializedName(FIELD_PUBLISH_YEAR)
        val publishYear: Int = 0,

        @SerializedName(FIELD_NUMBER_OF_PAGES)
        val pagesAmount: Int = 1,

        @SerializedName(FIELD_ISBN)
        val isbn: List<String>? = emptyList(),

        @SerializedName(FIELD_COVER_ID)
        val coverId: Int? = 0,

        @SerializedName(FIELD_COVER_EDITION_KEY)
        val coverEditionKey: String? = ""
    ): Parcelable
}
