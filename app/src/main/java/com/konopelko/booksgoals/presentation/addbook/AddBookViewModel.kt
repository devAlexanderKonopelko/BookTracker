package com.konopelko.booksgoals.presentation.addbook

import com.konopelko.booksgoals.domain.model.goal.Book
import com.konopelko.booksgoals.presentation.addbook.AddBookIntent.OnAddBookClicked
import com.konopelko.booksgoals.presentation.addbook.AddBookIntent.OnAuthorNameChanged
import com.konopelko.booksgoals.presentation.addbook.AddBookIntent.OnPagesAmountChanged
import com.konopelko.booksgoals.presentation.addbook.AddBookIntent.OnPublishYearChanged
import com.konopelko.booksgoals.presentation.addbook.AddBookIntent.OnTitleChanged
import com.konopelko.booksgoals.presentation.addbook.AddBookUiState.AddBookPartialState
import com.konopelko.booksgoals.presentation.addbook.AddBookUiState.AddBookPartialState.AuthorNameChanged
import com.konopelko.booksgoals.presentation.addbook.AddBookUiState.AddBookPartialState.AuthorNameError
import com.konopelko.booksgoals.presentation.addbook.AddBookUiState.AddBookPartialState.BookSavedSuccessfullyState
import com.konopelko.booksgoals.presentation.addbook.AddBookUiState.AddBookPartialState.BookTitleChanged
import com.konopelko.booksgoals.presentation.addbook.AddBookUiState.AddBookPartialState.BookTitleError
import com.konopelko.booksgoals.presentation.addbook.AddBookUiState.AddBookPartialState.PagesAmountChanged
import com.konopelko.booksgoals.presentation.addbook.AddBookUiState.AddBookPartialState.PagesAmountError
import com.konopelko.booksgoals.presentation.addbook.AddBookUiState.AddBookPartialState.PublishYearChanged
import com.konopelko.booksgoals.presentation.addbook.AddBookUiState.AddBookPartialState.PublishYearError
import com.konopelko.booksgoals.presentation.addbook.AddBookUiState.AddBookPartialState.SavingBookState
import com.konopelko.booksgoals.presentation.common.base.BaseViewModel

class AddBookViewModel(
    initialState: AddBookUiState
) : BaseViewModel<AddBookIntent, AddBookUiState, AddBookPartialState>(
    initialState = initialState
) {
    override fun acceptIntent(intent: AddBookIntent) = when(intent) {
        OnAddBookClicked -> onAddBookClicked()
        is OnTitleChanged -> onBookTitleChanged(intent.bookTitle)
        is OnAuthorNameChanged -> onBookAuthorNameChanged(intent.authorName)
        is OnPublishYearChanged -> onPublishYearChanged(intent.publishYear)
        is OnPagesAmountChanged -> onPagesAmountChanged(intent.pagesAmount)
    }

    private fun onBookTitleChanged(bookTitle: String) {
        updateUiState(
            BookTitleChanged(
                title = bookTitle,
                isValid = bookTitle.isNotEmpty()
            )
        )
    }

    private fun onBookAuthorNameChanged(authorName: String) {
        updateUiState(
            AuthorNameChanged(
                authorName = authorName,
                isValid = authorName.isNotEmpty()
            )
        )
    }

    private fun onPublishYearChanged(publishYear: String) {
        updateUiState(
            PublishYearChanged(
                publishYear = publishYear,
                isValid = publishYear.isNotEmpty()
            )
        )
    }

    private fun onPagesAmountChanged(pagesAmount: String) {
        updateUiState(
            PagesAmountChanged(
                pagesAmount = pagesAmount,
                isValid = pagesAmount.isNotEmpty()
            )
        )
    }

    private fun onAddBookClicked() {
        with(uiState.value) {
            if(book.areFieldsValid()) {
                updateUiState(BookSavedSuccessfullyState)
            }
        }
    }

    override fun mapUiState(
        previousState: AddBookUiState,
        partialState: AddBookPartialState
    ): AddBookUiState = when(partialState) {
        SavingBookState -> previousState.copy(isSaveButtonLoading = true)
        BookSavedSuccessfullyState -> previousState.copy(
            isBookSaved = true,
            isSaveButtonLoading = false
        )
        is BookTitleChanged -> previousState.copy(
            book = previousState.book.copy(title = partialState.title),
            isBookTitleError = partialState.isValid.not(),
            isSaveButtonEnabled = previousState.book.copy(
                title = partialState.title
            ).areFieldsValid() //todo: refactor somehow
        )
        is AuthorNameChanged -> previousState.copy(
            book = previousState.book.copy(authorName = partialState.authorName),
            isAuthorNameError = partialState.isValid.not(),
            isSaveButtonEnabled = previousState.book.copy(
                authorName = partialState.authorName
            ).areFieldsValid() //todo: refactor somehow
        )
        is PublishYearChanged -> previousState.copy(
            book = previousState.book.copy(publishYear = partialState.publishYear),
            isPublishYearError = partialState.isValid.not(),
            isSaveButtonEnabled = previousState.book.copy(
                publishYear = partialState.publishYear
            ).areFieldsValid() //todo: refactor somehow
        )
        is PagesAmountChanged -> previousState.copy(
            book = previousState.book.copy(pagesAmount = partialState.pagesAmount),
            isPagesAmountError = partialState.isValid.not(),
            isSaveButtonEnabled = previousState.book.copy(
                pagesAmount = partialState.pagesAmount
            ).areFieldsValid() //todo: refactor somehow
        )
        is BookTitleError -> previousState.copy(isBookTitleError = partialState.isError)
        is AuthorNameError -> previousState.copy(isAuthorNameError = partialState.isError)
        is PublishYearError -> previousState.copy(isPublishYearError = partialState.isError)
        is PagesAmountError -> previousState.copy(isPagesAmountError = partialState.isError)
    }

}

private fun Book.areFieldsValid(): Boolean =
    title.isNotEmpty() &&
    authorName.isNotEmpty() &&
    publishYear.isNotEmpty() &&
    pagesAmount.isNotEmpty()
