package com.konopelko.booksgoals.presentation.goaldetails

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.konopelko.booksgoals.domain.model.progress.ProgressMark
import com.konopelko.booksgoals.domain.usecase.addprogressmark.AddProgressMarkUseCase
import com.konopelko.booksgoals.domain.usecase.getgoal.GetGoalUseCase
import com.konopelko.booksgoals.domain.usecase.getgoalaveragereadspeed.GetGoalAverageReadSpeedUseCase
import com.konopelko.booksgoals.domain.usecase.updategoalprogress.UpdateGoalProgressUseCase
import com.konopelko.booksgoals.presentation.common.base.BaseViewModel
import com.konopelko.booksgoals.presentation.goaldetails.GoalDetailsIntent.OnAddProgressClicked
import com.konopelko.booksgoals.presentation.goaldetails.GoalDetailsIntent.OnArgsReceived
import com.konopelko.booksgoals.presentation.goaldetails.GoalDetailsIntent.OnCloseProgressMarkDialog
import com.konopelko.booksgoals.presentation.goaldetails.GoalDetailsIntent.OnSaveProgressClicked
import com.konopelko.booksgoals.presentation.goaldetails.GoalDetailsUiState.GoalDetailsPartialState
import com.konopelko.booksgoals.presentation.goaldetails.GoalDetailsUiState.GoalDetailsPartialState.AverageReadSpeedChanged
import com.konopelko.booksgoals.presentation.goaldetails.GoalDetailsUiState.GoalDetailsPartialState.GoalDataLoaded
import com.konopelko.booksgoals.presentation.goaldetails.GoalDetailsUiState.GoalDetailsPartialState.PagesCompletedChanged
import com.konopelko.booksgoals.presentation.goaldetails.GoalDetailsUiState.GoalDetailsPartialState.ProgressMarkDialogVisibilityChanged
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class GoalDetailsViewModel(
    initialState: GoalDetailsUiState,
    private val getGoalUseCase: GetGoalUseCase,
    private val getGoalAverageReadSpeedUseCase: GetGoalAverageReadSpeedUseCase,
    private val updateGoalProgressUseCase: UpdateGoalProgressUseCase,
    private val addProgressMarkUseCase: AddProgressMarkUseCase
) : BaseViewModel<GoalDetailsIntent, GoalDetailsUiState, GoalDetailsPartialState>(
    initialState = initialState
) {

    override fun acceptIntent(intent: GoalDetailsIntent) = when (intent) {
        OnAddProgressClicked -> onAddProgressClicked()
        OnCloseProgressMarkDialog -> onCloseProgressMarkDialog()
        is OnArgsReceived -> onArgsReceived(intent.goalId)
        is OnSaveProgressClicked -> onSaveProgressClicked(intent.pagesAmount)
    }

    private fun onArgsReceived(goalId: Int) {
        if (goalId > 0) {
            loadGoalData(goalId)
        }
    }

    private fun loadGoalData(goalId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            getGoalUseCase(goalId).onSuccess { goal ->
                getGoalAverageReadSpeedUseCase(goalId).onSuccess { averageReadSpeed ->
                    Log.e("GoalDetailsViewModel", "goal loaded: $goal")
                    updateUiState(GoalDataLoaded(goal.copy(averageReadSpeed = averageReadSpeed)))
                }.onError {
                    Log.e(
                        "GoalDetailsViewModel",
                        "error occurred when getting averageReadSpeed: ${it.exception}"
                    )
                    Log.e("GoalDetailsViewModel", "setting goal averageReadSpeed to 0")
                    updateUiState(GoalDataLoaded(goal))
                }
            }.onError {
                Log.e("GoalDetailsViewModel", "error occurred when getting a goal: ${it.exception}")
            }
        }
    }

    private fun onAddProgressClicked() {
        updateUiState(ProgressMarkDialogVisibilityChanged(isVisible = true))
    }

    private fun onCloseProgressMarkDialog() {
        updateUiState(ProgressMarkDialogVisibilityChanged(isVisible = false))
    }

    private fun onSaveProgressClicked(pagesReadAmount: Int) {
        val dateString = SimpleDateFormat(
            "yyyy/MM/dd HH:mm:ss",
            Locale.getDefault()
        ).format(Calendar.getInstance().time)

        Log.e("GoalDetailsViewModel", "saving progress mark. date: $dateString")

        val progressMark = with(uiState.value.goal) {
            ProgressMark(
                goalId = id,
                date = dateString,
                isBookFinished = completedPagesAmount + pagesReadAmount == bookPagesAmount,
                pagesAmount = pagesReadAmount
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            updateGoalProgressUseCase(
                goalId = uiState.value.goal.id,
                pagesReadAmount = uiState.value.goal.completedPagesAmount + pagesReadAmount
            ).onSuccess {
                updateUiState(PagesCompletedChanged(pagesCompleted = uiState.value.goal.completedPagesAmount + pagesReadAmount))
            }.onError {
                Log.e("GoalDetailsViewModel", "error occurred when updating goal progress, ${it.exception}")
            }

            addProgressMarkUseCase(progressMark)
                .onSuccess {
                    getGoalAverageReadSpeedUseCase(uiState.value.goal.id).onSuccess { averageReadSpeed ->
                        updateUiState(AverageReadSpeedChanged(readSpeed = averageReadSpeed))
                    }.onError {
                        Log.e("GoalDetailsViewModel", "error occurred when retrieving read speed, ${it.exception}")
                    }
                }
                .onError {
                    Log.e("GoalDetailsViewModel", "error occurred when saving progress mark, ${it.exception}")
                }
        }

        updateUiState(ProgressMarkDialogVisibilityChanged(isVisible = false))
    }

    override fun mapUiState(
        previousState: GoalDetailsUiState,
        partialState: GoalDetailsPartialState
    ): GoalDetailsUiState = when (partialState) {
        is AverageReadSpeedChanged -> previousState.copy(
            goal = previousState.goal.copy(
                averageReadSpeed = partialState.readSpeed
            )
        )
        is GoalDataLoaded -> previousState.copy(goal = partialState.goal)
        is PagesCompletedChanged -> previousState.copy(
            goal = previousState.goal.copy(
                completedPagesAmount = partialState.pagesCompleted,
                progress = getGoalProgress(
                    bookPagesAmount = previousState.goal.bookPagesAmount,
                    pagesCompletedAmount = partialState.pagesCompleted
                )
            )
        )
        is ProgressMarkDialogVisibilityChanged -> previousState.copy(showMarkProgressDialog = partialState.isVisible)
    }

    private fun getGoalProgress(
        bookPagesAmount: Int,
        pagesCompletedAmount: Int
    ): Int = ((pagesCompletedAmount / bookPagesAmount.toFloat()) * 100).toInt()

    companion object {
        const val ARGS_GOAL_ID_KEY = "goal_id"
    }
}