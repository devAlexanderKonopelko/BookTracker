package com.konopelko.booksgoals.data.api.query.searchbooks

internal const val FIELD_TITLE = "title"
internal const val FIELD_AUTHOR_NAME = "author_name"
internal const val FIELD_PUBLISH_YEAR = "first_publish_year"
internal const val FIELD_NUMBER_OF_PAGES = "number_of_pages_median"
internal const val FIELD_ISBN = "isbn"
internal const val FIELD_COVER_ID = "cover_i"
internal const val FIELD_COVER_EDITION_KEY = "cover_edition_key"

val searchBooksQueryFields = listOf(
    FIELD_TITLE,
    FIELD_AUTHOR_NAME,
    FIELD_PUBLISH_YEAR,
    FIELD_NUMBER_OF_PAGES,
    FIELD_ISBN,
    FIELD_COVER_ID,
    FIELD_COVER_EDITION_KEY
).joinToString(separator = ",")