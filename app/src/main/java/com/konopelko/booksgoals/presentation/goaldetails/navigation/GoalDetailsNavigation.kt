package com.konopelko.booksgoals.presentation.goaldetails.navigation

import androidx.navigation.NavController
import com.konopelko.booksgoals.domain.model.book.Book
import com.konopelko.booksgoals.domain.model.goal.Goal
import com.konopelko.booksgoals.presentation.addgoal.AddGoalViewModel
import com.konopelko.booksgoals.presentation.addgoal.model.AddGoalArgs
import com.konopelko.booksgoals.presentation.addgoal.model.AddGoalScreenOrigin
import com.konopelko.booksgoals.presentation.common.utils.uri.serializeNavParam
import com.konopelko.booksgoals.presentation.goaldetails.GoalDetailsIntent.GoalDetailsNavigationIntent
import com.konopelko.booksgoals.presentation.goaldetails.GoalDetailsIntent.GoalDetailsNavigationIntent.NavigateToEditGoal
import com.konopelko.booksgoals.presentation.navigation.MainNavOption

class GoalDetailsNavigation(
    private val navController: NavController
) {

    fun onNavigate(intent: GoalDetailsNavigationIntent) = when(intent) {
        is NavigateToEditGoal -> navigateToEditGoal(intent.goal)
    }

    private fun navigateToEditGoal(goal: Goal) {
        with(goal) {
            val addGoalArgs = AddGoalArgs(
                screenOrigin = AddGoalScreenOrigin.GOAL_DETAILS,
                selectedBook = Book(
                    id = bookId,
                    title = bookName,
                    authorName = bookAuthor,
                    publishYear = bookPublishYear.toString(),
                    pagesAmount = bookPagesAmount.toString(),
                    isStarted = true,
                    isFinished = false
                ),
                selectedPagesPerDay = expectedPagesPerDay,
                goalId = goal.id
            )

            navController.navigate(prepareAddGoalScreenNameWithArgs(addGoalArgs))
        }
    }

    private fun prepareAddGoalScreenNameWithArgs(addGoalArgs: AddGoalArgs): String =
        "${MainNavOption.AddGoalScreen.name}?" +
                "${AddGoalViewModel.ARGS_ADD_GOAL_KEY}=" + serializeNavParam(addGoalArgs)
}