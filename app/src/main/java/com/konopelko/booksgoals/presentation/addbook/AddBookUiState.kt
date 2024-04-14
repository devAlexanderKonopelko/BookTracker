package com.konopelko.booksgoals.presentation.addbook

//todo: refactor fields to be inside one data class model
data class AddBookUiState(
    val bookTitle: String = "",
    val authorName: String = "",
    val publishYear: String = "",
    val pagesAmount: String = "",
    val isBookTitleError: Boolean = false,
    val isAuthorNameError: Boolean = false,
    val isPublishYearError: Boolean = false,
    val isPagesAmountError: Boolean = false,
    val isBookSaved: Boolean = false,
    val isSaveButtonEnabled: Boolean = false
) {

    sealed interface AddBookPartialState {

        data object BookSavedSuccessfullyState : AddBookPartialState

        data class BookTitleChanged(
            val title: String,
            val isValid: Boolean
        ) : AddBookPartialState

        data class AuthorNameChanged(
            val authorName: String,
            val isValid: Boolean
        ) : AddBookPartialState

        data class PublishYearChanged(
            val publishYear: String,
            val isValid: Boolean
        ) : AddBookPartialState

        data class PagesAmountChanged(
            val pagesAmount: String,
            val isValid: Boolean
        ) : AddBookPartialState

        data class BookTitleError(val isError: Boolean) : AddBookPartialState
        data class AuthorNameError(val isError: Boolean) : AddBookPartialState
        data class PublishYearError(val isError: Boolean) : AddBookPartialState
        data class PagesAmountError(val isError: Boolean) : AddBookPartialState
    }
}
