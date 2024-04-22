package com.konopelko.booksgoals.presentation.addbook

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.konopelko.booksgoals.domain.model.book.Book
import com.konopelko.booksgoals.presentation.searchbooks.model.SearchScreenOrigin
import com.konopelko.booksgoals.presentation.searchbooks.model.SearchScreenOrigin.ADD_GOAL
import com.konopelko.booksgoals.presentation.searchbooks.model.SearchScreenOrigin.ADD_WISH_BOOK
import com.konopelko.booksgoals.domain.usecase.addbook.AddBookUseCase
import com.konopelko.booksgoals.presentation.addbook.AddBookIntent.OnAddBookClicked
import com.konopelko.booksgoals.presentation.addbook.AddBookIntent.OnArgsReceived
import com.konopelko.booksgoals.presentation.addbook.AddBookIntent.OnAuthorNameChanged
import com.konopelko.booksgoals.presentation.addbook.AddBookIntent.OnPagesAmountChanged
import com.konopelko.booksgoals.presentation.addbook.AddBookIntent.OnPublishYearChanged
import com.konopelko.booksgoals.presentation.addbook.AddBookIntent.OnTitleChanged
import com.konopelko.booksgoals.presentation.addbook.AddBookUiState.AddBookPartialState
import com.konopelko.booksgoals.presentation.addbook.AddBookUiState.AddBookPartialState.AuthorNameChanged
import com.konopelko.booksgoals.presentation.addbook.AddBookUiState.AddBookPartialState.AuthorNameError
import com.konopelko.booksgoals.presentation.addbook.AddBookUiState.AddBookPartialState.BookAddedSuccessfullyState
import com.konopelko.booksgoals.presentation.addbook.AddBookUiState.AddBookPartialState.BookTitleChanged
import com.konopelko.booksgoals.presentation.addbook.AddBookUiState.AddBookPartialState.BookTitleError
import com.konopelko.booksgoals.presentation.addbook.AddBookUiState.AddBookPartialState.PagesAmountChanged
import com.konopelko.booksgoals.presentation.addbook.AddBookUiState.AddBookPartialState.PagesAmountError
import com.konopelko.booksgoals.presentation.addbook.AddBookUiState.AddBookPartialState.PublishYearChanged
import com.konopelko.booksgoals.presentation.addbook.AddBookUiState.AddBookPartialState.PublishYearError
import com.konopelko.booksgoals.presentation.addbook.AddBookUiState.AddBookPartialState.SavingBookState
import com.konopelko.booksgoals.presentation.addbook.AddBookUiState.AddBookPartialState.ScreenOriginChangedState
import com.konopelko.booksgoals.presentation.common.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//todo: ----------------------------
//todo: handle adding a wish book
//todo: ----------------------------

class AddBookViewModel(
    initialState: AddBookUiState,
    private val addBookUseCase: AddBookUseCase
) : BaseViewModel<AddBookIntent, AddBookUiState, AddBookPartialState>(
    initialState = initialState
) {
    override fun acceptIntent(intent: AddBookIntent) = when(intent) {
        OnAddBookClicked -> onAddBookClicked()
        is OnArgsReceived -> onArgsReceived(intent.screenOrigin)
        is OnTitleChanged -> onBookTitleChanged(intent.bookTitle)
        is OnAuthorNameChanged -> onBookAuthorNameChanged(intent.authorName)
        is OnPublishYearChanged -> onPublishYearChanged(intent.publishYear)
        is OnPagesAmountChanged -> onPagesAmountChanged(intent.pagesAmount)
    }

    override fun mapUiState(
        previousState: AddBookUiState,
        partialState: AddBookPartialState
    ): AddBookUiState = when(partialState) {
        SavingBookState -> previousState.copy(isSaveButtonLoading = true)
        BookAddedSuccessfullyState -> previousState.copy(
            isBookAdded = true,
            isSaveButtonLoading = false
        )
        is ScreenOriginChangedState -> previousState.copy(screenOrigin = partialState.origin)
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

    private fun onArgsReceived(screenOrigin: SearchScreenOrigin) {
        updateUiState(ScreenOriginChangedState(screenOrigin))
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
                when(screenOrigin) {
                    ADD_GOAL -> onAddBookToGoal()
                    ADD_WISH_BOOK -> onAddBookToWishes()
                }
            }
        }
    }

    private fun onAddBookToGoal() {
        updateUiState(BookAddedSuccessfullyState)
    }

    private fun onAddBookToWishes() {
        viewModelScope.launch(Dispatchers.IO) {
            addBookUseCase(uiState.value.book).onSuccess {
                updateUiState(BookAddedSuccessfullyState)
            }.onError {
                Log.e("AddBookViewModel", "error occurred when saving a book: ${it.exception}")
            }
        }
    }

    companion object {
        const val ARGS_SCREEN_ORIGIN_KEY = "screen_origin"
    }
}

private fun Book.areFieldsValid(): Boolean =
    title.isNotEmpty() &&
    authorName.isNotEmpty() &&
    publishYear.isNotEmpty() &&
    pagesAmount.isNotEmpty()
