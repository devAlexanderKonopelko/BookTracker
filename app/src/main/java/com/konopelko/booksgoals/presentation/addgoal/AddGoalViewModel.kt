package com.konopelko.booksgoals.presentation.addgoal

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.konopelko.booksgoals.data.api.response.searchbooks.SearchBooksResponse.BookResponse
import com.konopelko.booksgoals.data.database.entity.goal.GoalEntity
import com.konopelko.booksgoals.domain.usecase.addgoal.AddGoalUseCase
import com.konopelko.booksgoals.presentation.addgoal.AddGoalIntent.OnArgsReceived
import com.konopelko.booksgoals.presentation.addgoal.AddGoalIntent.OnCreateGoalClicked
import com.konopelko.booksgoals.presentation.addgoal.AddGoalIntent.OnPagesPerDayChanged
import com.konopelko.booksgoals.presentation.addgoal.AddGoalUiState.AddGoalPartialState
import com.konopelko.booksgoals.presentation.addgoal.AddGoalUiState.AddGoalPartialState.BookSelected
import com.konopelko.booksgoals.presentation.addgoal.AddGoalUiState.AddGoalPartialState.PagesPerDayChanged
import com.konopelko.booksgoals.presentation.addgoal.AddGoalUiState.AddGoalPartialState.SavingGoalState
import com.konopelko.booksgoals.presentation.common.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.ceil

class AddGoalViewModel(
    initialState: AddGoalUiState,
    private val addGoalUseCase: AddGoalUseCase
) : BaseViewModel<AddGoalIntent, AddGoalUiState, AddGoalPartialState>(
    initialState = initialState
) {

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
            selectedBook = partialState.book,
            daysToFinishGoal = calculatePagesPerDay(pagesAmount = partialState.book.pagesAmount),
            isAddGoalButtonEnabled = true
        )
        is PagesPerDayChanged -> previousState.copy(
            daysToFinishGoal = calculatePagesPerDay(
                pagesAmount = previousState.selectedBook?.pagesAmount ?: 0
            ),
        )
        is SavingGoalState -> previousState.copy(
            isSavingGoal = partialState.isLoading,
            isGoalSaved = partialState.isGoalSaved,
        )
    }

    private fun calculatePagesPerDay(pagesAmount: Int): Int =
        ceil(pagesAmount / bookPagesPerDay.toDouble()).toInt()

    private fun onArgsReceived(args: BookResponse?) {
        Log.e("AddGoalViewModel", "onArgsReceived")
        Log.e("AddGoalViewModel", "args = $args")

        if(args != null) {
            updateUiState(BookSelected(book = args))
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

                //todo: move to usecase/repo
                val goalToAdd = GoalEntity(
                    bookName = selectedBook?.title ?: "",
                    bookAuthorName = prepareBookAuthorName(selectedBook) ?: "",
                    bookPublishYear = selectedBook?.publishYear?.toString() ?: "", //todo: make publishYear Int
                    bookPagesAmount = preparePagesAmount(selectedBook),
                    expectedPagesPerDay = bookPagesPerDay,
                    expectedFinishDaysAmount = calculateExpectedFinishDaysAmount(
                        expectedPagesPerDay = bookPagesPerDay,
                        booksPagesAmount = selectedBook?.pagesAmount ?: 0
                    ) // todo: make expectedFinishDaysAmount Int
                )

                viewModelScope.launch(Dispatchers.IO) {
                    addGoalUseCase(goalToAdd).onSuccess {
                        updateUiState(
                            SavingGoalState(
                                isLoading = false,
                                isGoalSaved = true
                            )
                        )
                    }
                }
            }
        }
    }

    private fun preparePagesAmount(selectedBook: BookResponse?): Int = selectedBook?.let {
        if(it.pagesAmount > 0) it.pagesAmount else 1
    } ?: 1

    private fun prepareBookAuthorName(newGoalSelectedBook: BookResponse?): String? =
        newGoalSelectedBook?.authorName?.joinToString(separator = ", ")

    private fun calculateExpectedFinishDaysAmount(
        expectedPagesPerDay: Int,
        booksPagesAmount: Int
    ): Float = booksPagesAmount / expectedPagesPerDay.toFloat()
}