package com.konopelko.booksgoals.presentation.wishes

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.konopelko.booksgoals.domain.model.book.Book
import com.konopelko.booksgoals.presentation.wishes.model.WishBookMenuOption
import com.konopelko.booksgoals.presentation.wishes.model.WishBookMenuOption.DELETE
import com.konopelko.booksgoals.presentation.wishes.model.WishBookMenuOption.START
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
import com.konopelko.booksgoals.presentation.wishes.WishesUiState.WishesPartialState.SelectBookForGoalState
import com.konopelko.booksgoals.presentation.wishes.WishesUiState.WishesPartialState.StartGoalClickedState
import com.konopelko.booksgoals.presentation.wishes.WishesUiState.WishesPartialState.WishBookDeletedSuccessfully
import com.konopelko.booksgoals.presentation.wishes.WishesUiState.WishesPartialState.WishesBooksLoaded
import com.konopelko.booksgoals.presentation.wishes.model.WishesArgs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//todo: display books that are [isStarted = false] && [isFinished = false]
class WishesViewModel(
    initialState: WishesUiState,
    private val getWishesBooksUseCase: GetWishesBooksUseCase,
    private val deleteBookUseCase: DeleteBookUseCase
): BaseViewModel<WishesIntent, WishesUiState, WishesPartialState>(
    initialState = initialState
) {
    private var isSelectBookForGoal = false

    init {
        Log.e("WishesViewModel", "init")
        loadWishesBooks()
    }

    override fun acceptIntent(intent: WishesIntent) = when(intent) {
        ResetNavigateToAddGoalScreen -> resetNavigateToAddGoalScreen()
        HideWishBookDeletedMessage -> hideWishBookDeletedMessage()
        is OnArgsReceived -> onArgsReceived(intent.args)
        is OnWishBookMenuOptionClicked -> onWishBookMenuOptionClicked(
            book = intent.book,
            menuOption = intent.wishBookMenuOption
        )
    }

    override fun mapUiState(
        previousState: WishesUiState,
        partialState: WishesPartialState
    ): WishesUiState = when(partialState) {
        ResetNavigateToAddGoalState -> previousState.copy(shouldNavigateToAddGoalScreen = false)
        HideWishBookDeletedMessageState -> previousState.copy(showWishBookDeletedMessage = false)
        SelectBookForGoalState -> previousState.copy(isSelectBookForGoal = true)
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

    private fun onArgsReceived(args: WishesArgs) {
        if(args.isWishBookAdded) {
            loadWishesBooks()
        }
        if(args.isSelectBookForGoal) {
            updateUiState(SelectBookForGoalState)
        }
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
        START -> onStartGoalClicked(book)
        DELETE -> onDeleteWishBookClicked(book)
    }

    private fun onStartGoalClicked(book: Book) {
        updateUiState(StartGoalClickedState(book))
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
        const val ARGS_KEY = "wishes_args"
    }
}