package com.konopelko.booksgoals.presentation.addbook.navigation

import androidx.navigation.NavController
import com.konopelko.booksgoals.domain.model.book.Book
import com.konopelko.booksgoals.presentation.addbook.AddBookIntent.AddBookNavigationIntent
import com.konopelko.booksgoals.presentation.addbook.AddBookIntent.AddBookNavigationIntent.NavigateToAddGoalScreen
import com.konopelko.booksgoals.presentation.addgoal.AddGoalViewModel
import com.konopelko.booksgoals.presentation.navigation.MainNavOption

class AddBookNavigation(
    private val navController: NavController
) {

    fun onNavigate(intent: AddBookNavigationIntent) = when(intent) {
        is NavigateToAddGoalScreen -> navigateToAddGoalScreen(intent.book)
    }

    private fun navigateToAddGoalScreen(book: Book) {
        navController.apply {
            navigate(MainNavOption.AddGoalScreen.name) {
                popUpTo(MainNavOption.GoalsScreen.name)
            }

            currentBackStackEntry?.apply {
                savedStateHandle[AddGoalViewModel.ARGS_BOOK_KEY] = book
            }
        }
    }
}