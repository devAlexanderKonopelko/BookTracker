package com.konopelko.booksgoals.presentation.goalstatistics

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.konopelko.booksgoals.domain.model.book.Book
import com.konopelko.booksgoals.domain.model.progress.ProgressMark
import com.konopelko.booksgoals.domain.usecase.getgoal.GetGoalUseCase
import com.konopelko.booksgoals.domain.usecase.getgoalprogress.GetGoalProgressUseCase
import com.konopelko.booksgoals.domain.utils.progressmaks.prepareProgressMarksInScale
import com.konopelko.booksgoals.presentation.common.base.BaseViewModel
import com.konopelko.booksgoals.presentation.goalstatistics.GoalStatisticsIntent.ArgsReceived
import com.konopelko.booksgoals.presentation.goalstatistics.GoalStatisticsIntent.SelectedScaleChanged
import com.konopelko.booksgoals.presentation.goalstatistics.GoalStatisticsUiState.PartialState
import com.konopelko.booksgoals.presentation.goalstatistics.GoalStatisticsUiState.PartialState.BookLoaded
import com.konopelko.booksgoals.presentation.goalstatistics.GoalStatisticsUiState.PartialState.ProgressMarksLoaded
import com.konopelko.booksgoals.presentation.goalstatistics.GoalStatisticsUiState.PartialState.StatisticsScaleChanged
import com.konopelko.booksgoals.presentation.goalstatistics.model.StatisticsScale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GoalStatisticsViewModel(
    initialState: GoalStatisticsUiState,
    private val getGoalProgressUseCase: GetGoalProgressUseCase,
    private val getGoalUseCase: GetGoalUseCase
) : BaseViewModel<GoalStatisticsIntent, GoalStatisticsUiState, PartialState>(initialState) {

    private var goalFullProgressList: List<ProgressMark> = emptyList()

    override fun acceptIntent(intent: GoalStatisticsIntent) = when (intent) {
        is ArgsReceived -> onArgsReceived(intent.goalId)
        is SelectedScaleChanged -> onSelectedScaleChanged(intent.scale)
    }

    override fun mapUiState(
        previousState: GoalStatisticsUiState,
        partialState: PartialState
    ): GoalStatisticsUiState = when (partialState) {
        is BookLoaded -> previousState.copy(book = partialState.book)
        is ProgressMarksLoaded -> previousState.copy(
            visibleProgressMarks = partialState.progressMarks
        )

        is StatisticsScaleChanged -> previousState.copy(
            selectedStatisticsScale = partialState.scale,
            visibleProgressMarks = goalFullProgressList.prepareProgressMarksInScale(partialState.scale)
        )
    }

    private fun onArgsReceived(goalId: Int) {
        if (goalId > 0) {
            loadGoalData(goalId)
        }
    }

    private fun onSelectedScaleChanged(scale: StatisticsScale) {
        if (scale != uiState.value.selectedStatisticsScale) {
            updateUiState(StatisticsScaleChanged(scale))
        }
    }

    private fun loadGoalData(goalId: Int) {
        viewModelScope.launch(Dispatchers.IO) {

            getGoalUseCase(goalId).onSuccess {
                updateUiState(
                    BookLoaded(
                        Book(
                            title = it.bookName,
                            authorName = it.bookAuthor,
                            publishYear = it.bookPublishYear.toString()
                        )
                    )
                )

                getGoalProgressUseCase(goalId).onSuccess { progressMarks ->
                    goalFullProgressList = progressMarks

                    val visibleProgressMarks = progressMarks.prepareProgressMarksInScale(
                        scale = uiState.value.selectedStatisticsScale
                    )

                    updateUiState(ProgressMarksLoaded(progressMarks = visibleProgressMarks))
                }.onError {
                    Log.e("GoalStatisticsViewModel", "Error loading goal progress, ${it.exception}")
                }
            }.onError {
                Log.e("GoalStatisticsViewModel", "Error loading goal, ${it.exception}")
            }
        }
    }

    companion object {

        const val ARGS_GOAL_ID = "goal_id"
    }
}