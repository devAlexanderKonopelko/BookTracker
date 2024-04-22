package com.konopelko.booksgoals.presentation.goals

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.konopelko.booksgoals.domain.model.goal.Goal
import com.konopelko.booksgoals.presentation.goals.model.GoalMenuOption
import com.konopelko.booksgoals.domain.usecase.addbook.AddBookUseCase
import com.konopelko.booksgoals.domain.usecase.deletegoal.DeleteGoalUseCase
import com.konopelko.booksgoals.domain.usecase.getgoals.GetGoalsUseCase
import com.konopelko.booksgoals.domain.usecase.updatebookisfinished.UpdateBookIsFinishedUseCase
import com.konopelko.booksgoals.presentation.common.base.BaseViewModel
import com.konopelko.booksgoals.presentation.goals.GoalsIntent.HideGoalCompletedMessage
import com.konopelko.booksgoals.presentation.goals.GoalsIntent.OnArgsReceived
import com.konopelko.booksgoals.presentation.goals.GoalsIntent.OnGoalOptionClicked
import com.konopelko.booksgoals.presentation.goals.GoalsUiState.PartialGoalsState
import com.konopelko.booksgoals.presentation.goals.GoalsUiState.PartialGoalsState.GoalCompletedMessageHidden
import com.konopelko.booksgoals.presentation.goals.GoalsUiState.PartialGoalsState.GoalCompletedSuccessfullyState
import com.konopelko.booksgoals.presentation.goals.GoalsUiState.PartialGoalsState.GoalsUpdated
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GoalsViewModel(
    initialState: GoalsUiState,
    private val getGoalsUseCase: GetGoalsUseCase,
    private val deleteGoalUseCase: DeleteGoalUseCase,
    private val addBookUseCase: AddBookUseCase,
    private val updateBookIsFinishedUseCase: UpdateBookIsFinishedUseCase
) : BaseViewModel<GoalsIntent, GoalsUiState, PartialGoalsState>(
    initialState = initialState
) {

    init {
        Log.e("HomeViewModel", "init")
        updateGoalsList()
    }

    override fun acceptIntent(intent: GoalsIntent) = when (intent) {
        HideGoalCompletedMessage -> onHideGoalCompletedMessageClicked()
        is OnArgsReceived -> onArgsReceived(intent.args)
        is OnGoalOptionClicked -> onGoalMenuOptionClicked(
            goal = intent.goal,
            goalMenuOption = intent.goalMenuOption
        )
    }

    override fun mapUiState(
        previousState: GoalsUiState,
        partialState: PartialGoalsState
    ) = when (partialState) {
        GoalCompletedMessageHidden -> previousState.copy(showGoalCompletedMessage = false)
        is GoalsUpdated -> previousState.copy(goals = partialState.goals)
        is GoalCompletedSuccessfullyState -> previousState.copy(
            goals = previousState.goals.filter { it.id != partialState.goalId },
            showGoalCompletedMessage = true
        )
    }

    private fun onHideGoalCompletedMessageClicked() {
        updateUiState(GoalCompletedMessageHidden)
    }

    private fun onGoalMenuOptionClicked(
        goal: Goal,
        goalMenuOption: GoalMenuOption
    ) {
        when(goalMenuOption) {
            GoalMenuOption.FREEZE -> onFreezeGoalClicked()
            GoalMenuOption.FINISH -> onFinishGoalClicked(goal)
            GoalMenuOption.DELETE -> onDeleteGoalClicked(goal.id)
        }
    }

    private fun onFreezeGoalClicked() {
        //TODO("Not yet implemented")
    }

    //todo: при завершении цели подумать, как изменяется статистика
    //todo: подумать, нужно ли удалять цель при завершении
    private fun onFinishGoalClicked(goal: Goal) {
        viewModelScope.launch(Dispatchers.IO) {
            //todo: избавиться от вложенности, реализовать последовательное выполнение, что-то типа zip() из rx

            deleteGoalUseCase(goal.id).onSuccess {

                //todo: replace with update book [isFinished = true]
                updateBookIsFinishedUseCase(
                    isFinished = true,
                    bookId = goal.bookId
                ).onSuccess {
                    updateUiState(GoalCompletedSuccessfullyState(goal.id))
                }.onError {
                    Log.e("GoalsViewModel", "error occurred while updating a book: ${it.exception}")
                }
            }.onError {
                Log.e("GoalsViewModel", "error occurred while deleting a goal: ${it.exception}")
            }
        }
    }

    //todo: при удалении цели подумать, как изменяется статистика
    //todo: подумать, нужно ли удалять книгу из бд
    private fun onDeleteGoalClicked(goalId: Int) {
        Log.e("GoalsViewModel", "delete goal clicked, id: $goalId")
        viewModelScope.launch(Dispatchers.IO) {
            deleteGoalUseCase(goalId).onSuccess {
                //todo: add toast message
                Log.e("GoalsViewModel", "goal deleted, id: $goalId")
            }.onError {
                Log.e("GoalsViewModel", "failed to delete goal, id: $goalId")
            }
        }
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