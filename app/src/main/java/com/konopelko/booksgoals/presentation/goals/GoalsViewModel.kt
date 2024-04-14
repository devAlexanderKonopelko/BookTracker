package com.konopelko.booksgoals.presentation.goals

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.konopelko.booksgoals.domain.model.goal.GoalMenuOption
import com.konopelko.booksgoals.domain.usecase.deletegoal.DeleteGoalUseCase
import com.konopelko.booksgoals.domain.usecase.getgoals.GetGoalsUseCase
import com.konopelko.booksgoals.presentation.common.base.BaseViewModel
import com.konopelko.booksgoals.presentation.goals.GoalsIntent.OnArgsReceived
import com.konopelko.booksgoals.presentation.goals.GoalsIntent.OnGoalOptionClicked
import com.konopelko.booksgoals.presentation.goals.GoalsUiState.PartialGoalsState
import com.konopelko.booksgoals.presentation.goals.GoalsUiState.PartialGoalsState.GoalsUpdated
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GoalsViewModel(
    initialState: GoalsUiState,
    private val getGoalsUseCase: GetGoalsUseCase,
    private val deleteGoalUseCase: DeleteGoalUseCase
) : BaseViewModel<GoalsIntent, GoalsUiState, PartialGoalsState>(
    initialState = initialState
) {

    init {
        Log.e("HomeViewModel", "init")
        updateGoalsList()
    }

    override fun acceptIntent(intent: GoalsIntent) = when (intent) {
        is OnArgsReceived -> onArgsReceived(intent.args)
        is OnGoalOptionClicked -> onGoalMenuOptionClicked(
            goalId = intent.goalId,
            goalMenuOption = intent.goalMenuOption
        )
    }

    private fun onGoalMenuOptionClicked(
        goalId: Int,
        goalMenuOption: GoalMenuOption
    ) {
        when(goalMenuOption) {
            GoalMenuOption.FREEZE -> onFreezeGoalClicked()
            GoalMenuOption.FINISH -> onFinishGoalClicked()
            GoalMenuOption.DELETE -> onDeleteGoalClicked(goalId)
        }
    }

    private fun onFreezeGoalClicked() {
        TODO("Not yet implemented")
    }

    private fun onFinishGoalClicked() {
        TODO("Not yet implemented")
    }

    private fun onDeleteGoalClicked(goalId: Int) {
        Log.e("GoalsViewModel", "delete goal clicked, id: $goalId")
        viewModelScope.launch(Dispatchers.IO) {
            deleteGoalUseCase(goalId).onSuccess {
                Log.e("GoalsViewModel", "goal deleted, id: $goalId")
            }.onError {
                Log.e("GoalsViewModel", "failed to delete goal, id: $goalId")
            }
        }
    }

    override fun mapUiState(
        previousState: GoalsUiState,
        partialState: PartialGoalsState
    ) = when (partialState) {
        is GoalsUpdated -> previousState.copy(goals = partialState.goals)
    }

    private fun onArgsReceived(isGoalAdded: Boolean) {
        if(isGoalAdded) {
            updateGoalsList()
        }
    }

    private fun updateGoalsList() {
        viewModelScope.launch(Dispatchers.IO) {
            getGoalsUseCase().collectLatest { goalsList ->
                Log.e("GoalsViewModel", "goals received: $goalsList")
                updateUiState(GoalsUpdated(goals = goalsList))
            }
        }
    }
}