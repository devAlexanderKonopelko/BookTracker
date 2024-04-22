package com.konopelko.booksgoals.presentation.addgoal

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.konopelko.booksgoals.domain.model.book.Book
import com.konopelko.booksgoals.domain.model.goal.Goal
import com.konopelko.booksgoals.domain.usecase.addbook.AddBookUseCase
import com.konopelko.booksgoals.domain.usecase.addgoal.AddGoalUseCase
import com.konopelko.booksgoals.domain.usecase.updatebookisstarted.UpdateBookIsStartedUseCase
import com.konopelko.booksgoals.presentation.addgoal.AddGoalIntent.OnArgsReceived
import com.konopelko.booksgoals.presentation.addgoal.AddGoalIntent.OnCreateGoalClicked
import com.konopelko.booksgoals.presentation.addgoal.AddGoalIntent.OnPagesPerDayChanged
import com.konopelko.booksgoals.presentation.addgoal.AddGoalUiState.AddGoalPartialState
import com.konopelko.booksgoals.presentation.addgoal.AddGoalUiState.AddGoalPartialState.BookSelected
import com.konopelko.booksgoals.presentation.addgoal.AddGoalUiState.AddGoalPartialState.PagesPerDayChanged
import com.konopelko.booksgoals.presentation.addgoal.AddGoalUiState.AddGoalPartialState.SavingGoalState
import com.konopelko.booksgoals.presentation.addgoal.model.AddGoalArgs
import com.konopelko.booksgoals.presentation.addgoal.model.AddGoalScreenOrigin
import com.konopelko.booksgoals.presentation.addgoal.model.AddGoalScreenOrigin.ADD_WISH_BOOK
import com.konopelko.booksgoals.presentation.addgoal.model.AddGoalScreenOrigin.GOALS
import com.konopelko.booksgoals.presentation.common.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.ceil

//todo: add screen_origin no check is should addBookUseCase be called (from wishes)
class AddGoalViewModel(
    initialState: AddGoalUiState,
    private val addGoalUseCase: AddGoalUseCase,
    private val addBookUseCase: AddBookUseCase,
    private val updateBookIsStartedUseCase: UpdateBookIsStartedUseCase
) : BaseViewModel<AddGoalIntent, AddGoalUiState, AddGoalPartialState>(
    initialState = initialState
) {

    private var screenOrigin: AddGoalScreenOrigin = GOALS
    private var bookPagesPerDay: Int = 20 // move to uiState

    override fun acceptIntent(intent: AddGoalIntent) = when(intent) {
        is OnArgsReceived -> onArgsReceived(intent.args)
        is OnPagesPerDayChanged -> onPagesPerDayChanged(intent.pagesPerDay)
        OnCreateGoalClicked -> onCreateGoalClicked()
    }

    override fun mapUiState(
        previousState: AddGoalUiState,
        partialState: AddGoalPartialState
    ): AddGoalUiState =  when(partialState) {
        is BookSelected -> previousState.copy(
            selectedBook = partialState.selectedBook,
            daysToFinishGoal = calculatePagesPerDay(pagesAmount = partialState.selectedBook.pagesAmount.toInt()),
            isAddGoalButtonEnabled = true
        )
        is PagesPerDayChanged -> previousState.copy(
            daysToFinishGoal = calculatePagesPerDay(
                pagesAmount = previousState.selectedBook?.pagesAmount?.toInt() ?: 0
            ),
        )
        is SavingGoalState -> previousState.copy(
            isSavingGoal = partialState.isLoading,
            isGoalSaved = partialState.isGoalSaved,
        )
    }

    private fun calculatePagesPerDay(pagesAmount: Int): Int =
        ceil(pagesAmount / bookPagesPerDay.toDouble()).toInt()

    //todo: refactor to receive [Book] domain model
    private fun onArgsReceived(args: AddGoalArgs?) {
        Log.e("AddGoalViewModel", "onArgsReceived")
        Log.e("AddGoalViewModel", "args = $args")

        if(args != null) {
            if(args.selectedBook != null) {
                updateUiState(BookSelected(selectedBook = args.selectedBook))
            }
            screenOrigin = args.screenOrigin
        }
    }

    private fun onPagesPerDayChanged(newPagesPerDay: Int) {
        bookPagesPerDay = newPagesPerDay
        updateUiState(PagesPerDayChanged(newPagesPerDay = newPagesPerDay))
    }

    private fun onCreateGoalClicked() {
        if(uiState.value.selectedBook != null) {
            updateUiState(SavingGoalState(isLoading = true))

            Log.e("AddGoalViewModel", "pagesPerDay = $bookPagesPerDay")
            Log.e("AddGoalViewModel", "selectedBook = ${uiState.value.selectedBook}")

            with(uiState.value) {

                selectedBook?.let {
                    val goalToAdd = Goal(
                        bookName = selectedBook.title,
                        bookAuthor = prepareBookAuthorName(selectedBook),
                        bookPublishYear = selectedBook.publishYear.toInt(),
                        bookPagesAmount = preparePagesAmount(selectedBook),
                        expectedPagesPerDay = bookPagesPerDay,
                        expectedFinishDaysAmount = calculateExpectedFinishDaysAmount(
                            expectedPagesPerDay = bookPagesPerDay,
                            booksPagesAmount = selectedBook.pagesAmount.toInt()
                        )
                    )

                    viewModelScope.launch(Dispatchers.IO) {
                        when(screenOrigin) {
                            GOALS -> createGoalWithNewBook(
                                goalToAdd = goalToAdd,
                                selectedBook = selectedBook
                            )
                            ADD_WISH_BOOK -> createGoalFromWishBook(
                                goalToAdd = goalToAdd,
                                selectedBook = selectedBook
                            )
                        }
                    }
                }
            }
        }
    }

    private suspend fun createGoalWithNewBook(
        goalToAdd: Goal,
        selectedBook: Book
    ) {
        addBookUseCase(selectedBook.copy(isStarted = true)).onSuccess { bookId ->
            addGoalUseCase(goalToAdd.copy(bookId = bookId)).onSuccess {
                updateUiState(
                    SavingGoalState(
                        isLoading = false,
                        isGoalSaved = true
                    )
                )
            }.onError {
                Log.e("AddGoalViewModel", "error occurred when saving a goal: ${it.exception}")
            }
        }.onError {
            Log.e("AddGoalViewModel", "error occurred when saving a book: ${it.exception}")
        }
    }

    private suspend fun createGoalFromWishBook(goalToAdd: Goal, selectedBook: Book) {
        updateBookIsStartedUseCase(
            isStarted = true,
            bookId = selectedBook.id
        ).onSuccess {
            addGoalUseCase(goalToAdd.copy(bookId = selectedBook.id)).onSuccess {
                updateUiState(
                    SavingGoalState(
                        isLoading = false,
                        isGoalSaved = true
                    )
                )
            }.onError {
                Log.e("AddGoalViewModel", "error occurred when saving a goal: ${it.exception}")
            }
        }.onError {
            Log.e("AddGoalViewModel", "error occurred when saving a book: ${it.exception}")
        }
    }

    private fun preparePagesAmount(selectedBook: Book?): Int = selectedBook?.let {
        if(it.pagesAmount.toInt() > 0) it.pagesAmount.toInt() else 1
    } ?: 1

    private fun prepareBookAuthorName(newGoalSelectedBook: Book): String =
        newGoalSelectedBook.authorName

    private fun calculateExpectedFinishDaysAmount(
        expectedPagesPerDay: Int,
        booksPagesAmount: Int
    ): Int = booksPagesAmount / expectedPagesPerDay

    companion object {
        const val ARGS_ADD_GOAL_KEY = "add_goal_args"
    }
}