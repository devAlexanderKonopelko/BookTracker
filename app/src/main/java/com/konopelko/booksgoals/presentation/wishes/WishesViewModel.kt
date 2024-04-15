package com.konopelko.booksgoals.presentation.wishes

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.konopelko.booksgoals.domain.model.book.Book
import com.konopelko.booksgoals.domain.model.book.WishBookMenuOption
import com.konopelko.booksgoals.domain.model.book.WishBookMenuOption.DELETE
import com.konopelko.booksgoals.domain.model.book.WishBookMenuOption.START
import com.konopelko.booksgoals.domain.usecase.deletebook.DeleteBookUseCase
import com.konopelko.booksgoals.domain.usecase.getwishesbooks.GetWishesBooksUseCase
import com.konopelko.booksgoals.presentation.common.base.BaseViewModel
import com.konopelko.booksgoals.presentation.wishes.WishesIntent.HideWishBookDeletedMessage
import com.konopelko.booksgoals.presentation.wishes.WishesIntent.OnArgsReceived
import com.konopelko.booksgoals.presentation.wishes.WishesIntent.OnWishBookMenuOptionClicked
import com.konopelko.booksgoals.presentation.wishes.WishesIntent.ResetNavigateToAddGoalScreen
import com.konopelko.booksgoals.presentation.wishes.WishesUiState.WishesPartialState
import com.konopelko.booksgoals.presentation.wishes.WishesUiState.WishesPartialState.HideWishBookDeletedMessageState
import com.konopelko.booksgoals.presentation.wishes.WishesUiState.WishesPartialState.ResetNavigateToAddGoalState
import com.konopelko.booksgoals.presentation.wishes.WishesUiState.WishesPartialState.StartGoalClickedState
import com.konopelko.booksgoals.presentation.wishes.WishesUiState.WishesPartialState.WishBookDeletedSuccessfully
import com.konopelko.booksgoals.presentation.wishes.WishesUiState.WishesPartialState.WishesBooksLoaded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WishesViewModel(
    initialState: WishesUiState,
    private val getWishesBooksUseCase: GetWishesBooksUseCase,
    private val deleteBookUseCase: DeleteBookUseCase
): BaseViewModel<WishesIntent, WishesUiState, WishesPartialState>(
    initialState = initialState
) {

    init {
        Log.e("WishesViewModel", "init")
        loadWishesBooks()
    }

    override fun acceptIntent(intent: WishesIntent) = when(intent) {
        ResetNavigateToAddGoalScreen -> resetNavigateToAddGoalScreen()
        HideWishBookDeletedMessage -> hideWishBookDeletedMessage()
        is OnArgsReceived -> onArgsReceived(isBookAdded = intent.args)
        is OnWishBookMenuOptionClicked -> onWishBookMenuOptionClicked(
            book = intent.book,
            menuOption = intent.wishBookMenuOption
        )
    }

    private fun onArgsReceived(isBookAdded: Boolean) {
        if(isBookAdded) {
            loadWishesBooks()
        }
    }

    override fun mapUiState(
        previousState: WishesUiState,
        partialState: WishesPartialState
    ): WishesUiState = when(partialState) {
        ResetNavigateToAddGoalState -> previousState.copy(shouldNavigateToAddGoalScreen = false)
        HideWishBookDeletedMessageState -> previousState.copy(showWishBookDeletedMessage = false)
        is WishBookDeletedSuccessfully -> previousState.copy(
            wishesBooks = partialState.books,
            showWishBookDeletedMessage = true
        )
        is StartGoalClickedState -> previousState.copy(
            shouldNavigateToAddGoalScreen = true,
            bookToStartGoalWith = partialState.book
        )
        is WishesBooksLoaded -> previousState.copy(wishesBooks = partialState.books)
    }

    private fun resetNavigateToAddGoalScreen() {
        updateUiState(ResetNavigateToAddGoalState)
    }

    private fun hideWishBookDeletedMessage() {
        updateUiState(HideWishBookDeletedMessageState)
    }

    private fun loadWishesBooks() {
        viewModelScope.launch(Dispatchers.IO) {
            getWishesBooksUseCase().onSuccess { books ->
                updateUiState(WishesBooksLoaded(books))
            }.onError {
                Log.e("WishesViewModel", "error occurred when loading books: ${it.exception}")
            }
        }
    }

    private fun onWishBookMenuOptionClicked(
        book: Book,
        menuOption: WishBookMenuOption
    ) = when(menuOption) {
        START -> updateUiState(StartGoalClickedState(book))
        DELETE -> onDeleteWishBookClicked(book)
    }

    private fun onDeleteWishBookClicked(book: Book) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteBookUseCase(book.id).onSuccess {
                getWishesBooksUseCase().onSuccess { wishesBooks ->
                    updateUiState(WishBookDeletedSuccessfully(wishesBooks))
                }.onError {
                    Log.e("WishesViewModel", "error occurred when deleting book: ${it.exception}")
                }
            }.onError {
                Log.e("WishesViewModel", "error occurred when deleting book: ${it.exception}")
            }
        }
    }

    companion object {
        const val ARGS_WISH_BOOK_ADDED_KEY = "wish_book_added"
    }
}