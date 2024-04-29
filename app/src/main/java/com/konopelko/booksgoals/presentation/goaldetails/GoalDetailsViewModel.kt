package com.konopelko.booksgoals.presentation.goaldetails

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.konopelko.booksgoals.domain.usecase.getgoal.GetGoalUseCase
import com.konopelko.booksgoals.presentation.common.base.BaseViewModel
import com.konopelko.booksgoals.presentation.goaldetails.GoalDetailsIntent.OnAddProgressClicked
import com.konopelko.booksgoals.presentation.goaldetails.GoalDetailsIntent.OnArgsReceived
import com.konopelko.booksgoals.presentation.goaldetails.GoalDetailsIntent.OnBookStatisticsClicked
import com.konopelko.booksgoals.presentation.goaldetails.GoalDetailsIntent.OnEditGoalClicked
import com.konopelko.booksgoals.presentation.goaldetails.GoalDetailsUiState.GoalDetailsPartialState
import com.konopelko.booksgoals.presentation.goaldetails.GoalDetailsUiState.GoalDetailsPartialState.GoalDataLoaded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GoalDetailsViewModel(
    initialState: GoalDetailsUiState,
    private val getGoalUseCase: GetGoalUseCase
) : BaseViewModel<GoalDetailsIntent, GoalDetailsUiState, GoalDetailsPartialState>(
    initialState = initialState
) {

    override fun acceptIntent(intent: GoalDetailsIntent) = when(intent) {
        OnAddProgressClicked -> onAddProgressClicked()
        OnEditGoalClicked -> onEditGoalClicked()
        OnBookStatisticsClicked -> onBookStatisticsClicked()
        is OnArgsReceived -> onArgsReceived(intent.goalId)
    }

    private fun onArgsReceived(goalId: Int) {
        if(goalId > 0) {
            loadGoalData(goalId)
        }
    }

    private fun loadGoalData(goalId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            getGoalUseCase(goalId).onSuccess { goal ->
                updateUiState(GoalDataLoaded(goal))
            }.onError {
                Log.e("GoalDetailsViewModel", "error occurred when getting a goal: ${it.exception}")
            }
        }
    }

    private fun onBookStatisticsClicked() {
        //TODO("Not yet implemented")
    }

    private fun onEditGoalClicked() {
        //TODO("Not yet implemented")
    }

    private fun onAddProgressClicked() {
        //TODO("Not yet implemented")
    }

    override fun mapUiState(
        previousState: GoalDetailsUiState,
        partialState: GoalDetailsPartialState
    ): GoalDetailsUiState = when(partialState) {
        is GoalDataLoaded -> previousState.copy(goal = partialState.goal)
    }

    companion object {
        const val ARGS_GOAL_ID_KEY = "goal_id"
    }
}