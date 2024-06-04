package com.konopelko.booksgoals.presentation.goals

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.konopelko.booksgoals.domain.model.goal.Goal
import com.konopelko.booksgoals.domain.model.progress.ProgressMark
import com.konopelko.booksgoals.domain.usecase.addprogressmark.AddProgressMarkUseCase
import com.konopelko.booksgoals.presentation.goals.model.GoalMenuOption
import com.konopelko.booksgoals.domain.usecase.deletegoal.DeleteGoalUseCase
import com.konopelko.booksgoals.domain.usecase.getgoals.GetGoalsUseCase
import com.konopelko.booksgoals.domain.usecase.updatebookisfinished.UpdateBookIsFinishedUseCase
import com.konopelko.booksgoals.domain.usecase.updategoalfrozen.UpdateGoalFrozenUseCase
import com.konopelko.booksgoals.presentation.common.base.BaseViewModel
import com.konopelko.booksgoals.presentation.goals.GoalsIntent.HideGoalCompletedMessage
import com.konopelko.booksgoals.presentation.goals.GoalsIntent.OnArgsReceived
import com.konopelko.booksgoals.presentation.goals.GoalsIntent.OnGoalOptionClicked
import com.konopelko.booksgoals.presentation.goals.GoalsUiState.PartialGoalsState
import com.konopelko.booksgoals.presentation.goals.GoalsUiState.PartialGoalsState.GoalCompletedMessageHidden
import com.konopelko.booksgoals.presentation.goals.GoalsUiState.PartialGoalsState.GoalCompletedSuccessfullyState
import com.konopelko.booksgoals.presentation.goals.GoalsUiState.PartialGoalsState.GoalIsFrozenChanged
import com.konopelko.booksgoals.presentation.goals.GoalsUiState.PartialGoalsState.GoalsUpdated
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class GoalsViewModel(
    initialState: GoalsUiState,
    private val getGoalsUseCase: GetGoalsUseCase,
    private val deleteGoalUseCase: DeleteGoalUseCase,
    private val updateBookIsFinishedUseCase: UpdateBookIsFinishedUseCase,
    private val updateGoalFrozenUseCase: UpdateGoalFrozenUseCase,
    private val addProgressMarkUseCase: AddProgressMarkUseCase
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
        is GoalIsFrozenChanged -> previousState.copy(
            goals = previousState.goals.map {
                if(it.id == partialState.goalId) it.copy(isFrozen = partialState.isFrozen)
                else it
            }
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
            GoalMenuOption.FREEZE, GoalMenuOption.UNFREEZE -> onFreezeGoalClicked(goal.id, goal.isFrozen)
            GoalMenuOption.FINISH -> onFinishGoalClicked(goal)
            GoalMenuOption.DELETE -> onDeleteGoalClicked(goal.id)
        }
    }

    private fun onFreezeGoalClicked(goalId: Int, previousIsGoalFrozen: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            updateGoalFrozenUseCase(goalId, !previousIsGoalFrozen).onSuccess {
                updateUiState(GoalIsFrozenChanged(goalId, !previousIsGoalFrozen))
            }.onError {
                Log.e("GoalsViewModel", "error occurred while freezing a goal: ${it.exception.stackTrace}")
            }
        }
    }

    private fun onFinishGoalClicked(goal: Goal) {
        val date = SimpleDateFormat(
            "yyyy/MM/dd HH:mm:ss",
            Locale.getDefault()
        ).format(Calendar.getInstance().time)

        val progressMark = ProgressMark(
            goalId = goal.id,
            date = date,
            isBookFinished = true,
            pagesAmount = 0
        )

        viewModelScope.launch(Dispatchers.IO) {
            //todo: избавиться от вложенности, реализовать последовательное выполнение, что-то типа zip() из rx

            addProgressMarkUseCase(progressMark).onSuccess {
                deleteGoalUseCase(goal.id).onSuccess {

                    //todo: replace with update book [isFinished = true]
                    updateBookIsFinishedUseCase(
                        isFinished = true,
                        bookId = goal.bookId,
                        finishDate = date
                    ).onSuccess {
                        updateUiState(GoalCompletedSuccessfullyState(goal.id))
                    }.onError {
                        Log.e("GoalsViewModel", "error occurred while updating a book: ${it.exception}")
                    }
                }.onError {
                    Log.e("GoalsViewModel", "error occurred while deleting a goal: ${it.exception}")
                }
            }.onError {
                Log.e("GoalsViewModel", "error occurred while adding a progress mark: ${it.exception}")
            }
        }
    }

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

    companion object {

        const val ARGS_GOAL_ADDED_KEY = "goal_added"
    }
}