package com.konopelko.booksgoals.presentation.addgoal

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.konopelko.booksgoals.domain.model.book.Book
import com.konopelko.booksgoals.domain.model.goal.Goal
import com.konopelko.booksgoals.domain.usecase.addbook.AddBookUseCase
import com.konopelko.booksgoals.domain.usecase.addgoal.AddGoalUseCase
import com.konopelko.booksgoals.domain.usecase.updatebookisstarted.UpdateBookIsStartedUseCase
import com.konopelko.booksgoals.domain.usecase.updategoalpagesperday.UpdateGoalExpectedParamsUseCase
import com.konopelko.booksgoals.presentation.addgoal.AddGoalIntent.OnArgsReceived
import com.konopelko.booksgoals.presentation.addgoal.AddGoalIntent.OnCreateGoalClicked
import com.konopelko.booksgoals.presentation.addgoal.AddGoalIntent.OnPagesPerDayChanged
import com.konopelko.booksgoals.presentation.addgoal.AddGoalUiState.AddGoalPartialState
import com.konopelko.booksgoals.presentation.addgoal.AddGoalUiState.AddGoalPartialState.BookSelected
import com.konopelko.booksgoals.presentation.addgoal.AddGoalUiState.AddGoalPartialState.EditGoalState
import com.konopelko.booksgoals.presentation.addgoal.AddGoalUiState.AddGoalPartialState.PagesPerDayChanged
import com.konopelko.booksgoals.presentation.addgoal.AddGoalUiState.AddGoalPartialState.SavingGoalState
import com.konopelko.booksgoals.presentation.addgoal.AddGoalUiState.AddGoalPartialState.ShouldNavigateToGoalDetailsState
import com.konopelko.booksgoals.presentation.addgoal.model.AddGoalArgs
import com.konopelko.booksgoals.presentation.addgoal.model.AddGoalScreenOrigin
import com.konopelko.booksgoals.presentation.addgoal.model.AddGoalScreenOrigin.ADD_WISH_BOOK
import com.konopelko.booksgoals.presentation.addgoal.model.AddGoalScreenOrigin.GOALS
import com.konopelko.booksgoals.presentation.addgoal.model.AddGoalScreenOrigin.GOAL_DETAILS
import com.konopelko.booksgoals.presentation.common.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.math.ceil

class AddGoalViewModel(
    initialState: AddGoalUiState,
    private val addGoalUseCase: AddGoalUseCase,
    private val addBookUseCase: AddBookUseCase,
    private val updateBookIsStartedUseCase: UpdateBookIsStartedUseCase,
    private val updateGoalExpectedParamsUseCase: UpdateGoalExpectedParamsUseCase
) : BaseViewModel<AddGoalIntent, AddGoalUiState, AddGoalPartialState>(
    initialState = initialState
) {

    private var screenOrigin: AddGoalScreenOrigin = GOALS
    private var bookPagesPerDay: Int = 1 // move to uiState
    private var goalId: Int = -1

    override fun acceptIntent(intent: AddGoalIntent) = when (intent) {
        is OnArgsReceived -> onArgsReceived(intent.args)
        is OnPagesPerDayChanged -> onPagesPerDayChanged(intent.pagesPerDay)
        OnCreateGoalClicked -> onCreateGoalClicked()
    }

    override fun mapUiState(
        previousState: AddGoalUiState,
        partialState: AddGoalPartialState
    ): AddGoalUiState = when (partialState) {
        ShouldNavigateToGoalDetailsState -> previousState.copy(shouldNavigateToGoalDetailsScreen = true)
        is BookSelected -> previousState.copy(
            selectedBook = partialState.selectedBook,
            daysToFinishGoal = calculateExpectedFinishDaysAmount(
                pagesAmount = partialState.selectedBook.pagesAmount.toInt(),
                pagesPerDay = bookPagesPerDay
            ),
            isAddGoalButtonEnabled = true,
            screenOrigin = partialState.screenOrigin,
            isSelectBookButtonEnabled = partialState.isSelectBookButtonEnabled
        )

        is EditGoalState -> previousState.copy(
            isSelectBookButtonEnabled = false,
            selectedPagesPerDay = partialState.selectedPagesPerDay,
            selectedBook = partialState.selectedBook,
            daysToFinishGoal = calculateExpectedFinishDaysAmount(
                pagesAmount = partialState.selectedBook.pagesAmount.toInt(),
                pagesPerDay = partialState.selectedPagesPerDay
            ),
            screenOrigin = partialState.screenOrigin
        )

        is PagesPerDayChanged -> previousState.copy(
            daysToFinishGoal = calculateExpectedFinishDaysAmount(
                pagesAmount = previousState.selectedBook.pagesAmount.toInt(),
                pagesPerDay = partialState.newPagesPerDay
            )
        )

        is SavingGoalState -> previousState.copy(
            isSavingGoal = partialState.isLoading,
            isGoalSaved = partialState.isGoalSaved,
        )
    }

    private fun onArgsReceived(args: AddGoalArgs?) {
        Log.e("AddGoalViewModel", "onArgsReceived")
        Log.e("AddGoalViewModel", "args = $args")

        if (args != null) {
            when (args.screenOrigin) {
                GOALS, ADD_WISH_BOOK -> {
                    if (args.selectedBook != null) {
                        updateUiState(
                            BookSelected(
                                selectedBook = args.selectedBook,
                                screenOrigin = args.screenOrigin,
                                isSelectBookButtonEnabled = args.screenOrigin == GOALS
                            )
                        )
                    }
                }
                GOAL_DETAILS -> {
                    if (args.selectedPagesPerDay != null &&
                        args.selectedBook != null
                    ) {
                        updateUiState(
                            EditGoalState(
                                selectedPagesPerDay = args.selectedPagesPerDay,
                                screenOrigin = args.screenOrigin,
                                selectedBook = args.selectedBook
                            )
                        )
                    }
                }
            }

            args.goalId?.let { goalId = it }
            screenOrigin = args.screenOrigin
        }
    }

    private fun onPagesPerDayChanged(newPagesPerDay: Int) {
        bookPagesPerDay = newPagesPerDay
        updateUiState(
            PagesPerDayChanged(
                newPagesPerDay = newPagesPerDay,
                screenOrigin = screenOrigin
            )
        )
    }

    private fun onCreateGoalClicked() {
        updateUiState(SavingGoalState(isLoading = true))

        Log.e("AddGoalViewModel", "pagesPerDay = $bookPagesPerDay")
        Log.e("AddGoalViewModel", "selectedBook = ${uiState.value.selectedBook}")

        with(uiState.value) {

            val currentDateTime = Calendar.getInstance().timeInMillis
//                    val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
//                        .format(currentDateTime)

            val goalToAdd = Goal(
                bookName = selectedBook.title,
                bookAuthor = prepareBookAuthorName(selectedBook),
                bookPublishYear = selectedBook.publishYear.toInt(),
                bookPagesAmount = preparePagesAmount(selectedBook),
                bookCoverUrl = selectedBook.coverUrl,
                creationDate = currentDateTime.toString(),
                expectedPagesPerDay = bookPagesPerDay,
                expectedFinishDaysAmount = daysToFinishGoal
            )

            viewModelScope.launch(Dispatchers.IO) {
                when (screenOrigin) {
                    GOALS -> createGoalWithNewBook(
                        goalToAdd = goalToAdd,
                        selectedBook = selectedBook
                    )

                    ADD_WISH_BOOK -> createGoalFromWishBook(
                        goalToAdd = goalToAdd,
                        selectedBook = selectedBook
                    )

                    GOAL_DETAILS -> updateGoalPagesPerDay()
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

    private fun updateGoalPagesPerDay() {
        viewModelScope.launch(Dispatchers.IO) {
            updateGoalExpectedParamsUseCase(
                goalId = goalId,
                pagesPerDay = bookPagesPerDay,
                daysToFinish = uiState.value.daysToFinishGoal
            ).onSuccess {
                updateUiState(ShouldNavigateToGoalDetailsState)
                Log.e(
                    "AddGoalViewModel",
                    "goal pages per day updated to $bookPagesPerDay. Navigating to GoalDetailsScreen"
                )
            }.onError {
                Log.e(
                    "AddGoalViewModel",
                    "error occurred when updating goal pages per day: ${it.exception}"
                )
            }
        }
    }

    private fun preparePagesAmount(selectedBook: Book?): Int = selectedBook?.let {
        if (it.pagesAmount.toInt() > 0) it.pagesAmount.toInt() else 1
    } ?: 1

    private fun calculateExpectedFinishDaysAmount(
        pagesAmount: Int,
        pagesPerDay: Int
    ): Int = ceil(pagesAmount / pagesPerDay.toDouble()).toInt()

    private fun prepareBookAuthorName(newGoalSelectedBook: Book): String =
        newGoalSelectedBook.authorName

    companion object {
        const val ARGS_ADD_GOAL_KEY = "add_goal_args"
    }
}