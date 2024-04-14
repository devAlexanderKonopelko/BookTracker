package com.konopelko.booksgoals.data.api.query.searchbooks

internal const val FIELD_TITLE = "title"
internal const val FIELD_AUTHOR_NAME = "author_name"
internal const val FIELD_PUBLISH_YEAR = "first_publish_year"
internal const val FIELD_NUMBER_OF_PAGES = "number_of_pages_median"

val searchBooksQueryFields = listOf(
    FIELD_TITLE,
    FIELD_AUTHOR_NAME,
    FIELD_PUBLISH_YEAR,
    FIELD_NUMBER_OF_PAGES
).joinToString(separator = ",")