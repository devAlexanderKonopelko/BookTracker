package com.konopelko.booksgoals.presentation.addbook

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.konopelko.booksgoals.domain.model.goal.Book
import com.konopelko.booksgoals.domain.usecase.addbook.AddBookUseCase
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
import com.konopelko.booksgoals.presentation.common.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddBookViewModel(
    initialState: AddBookUiState,
    private val addBookUseCase: AddBookUseCase
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
                isValid = bookTitle.isNotEmpty() //todo: use in mapUiState
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
            if(areFieldsValid()) {
                val book = Book(
                    title = bookTitle,
                    authorName = authorName,
                    publishYear = publishYear,
                    pagesAmount = pagesAmount
                )

                viewModelScope.launch(Dispatchers.IO) {
                    addBookUseCase(book).onSuccess {
                        updateUiState(BookSavedSuccessfullyState)
                    }.onError {
                        Log.e("AddBookViewModel", "error occurred while saving a book: ${it.exception}")
                    }
                }
            }
        }
    }

    override fun mapUiState(
        previousState: AddBookUiState,
        partialState: AddBookPartialState
    ): AddBookUiState = when(partialState) {
        BookSavedSuccessfullyState -> previousState.copy(isBookSaved = true)
        is BookTitleChanged -> previousState.copy(bookTitle = partialState.title)
        is AuthorNameChanged -> previousState.copy(authorName = partialState.authorName)
        is PublishYearChanged -> previousState.copy(publishYear = partialState.publishYear)
        is PagesAmountChanged -> previousState.copy(pagesAmount = partialState.pagesAmount)
        is BookTitleError -> previousState.copy(isBookTitleError = partialState.isError)
        is AuthorNameError -> previousState.copy(isAuthorNameError = partialState.isError)
        is PublishYearError -> previousState.copy(isPublishYearError = partialState.isError)
        is PagesAmountError -> previousState.copy(isPagesAmountError = partialState.isError)
    }

}

private fun AddBookUiState.areFieldsValid(): Boolean =
    isBookTitleError.not() &&
    isAuthorNameError.not() &&
    isPublishYearError.not() &&
    isPagesAmountError.not()
