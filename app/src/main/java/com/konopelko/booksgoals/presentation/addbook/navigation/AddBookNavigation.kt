package com.konopelko.booksgoals.presentation.addbook.navigation

import androidx.navigation.NavController
import com.konopelko.booksgoals.domain.model.book.Book
import com.konopelko.booksgoals.presentation.addbook.AddBookIntent.AddBookNavigationIntent
import com.konopelko.booksgoals.presentation.addbook.AddBookIntent.AddBookNavigationIntent.NavigateToAddGoalScreen
import com.konopelko.booksgoals.presentation.addbook.AddBookIntent.AddBookNavigationIntent.NavigateToWishesScreen
import com.konopelko.booksgoals.presentation.addgoal.AddGoalViewModel
import com.konopelko.booksgoals.presentation.navigation.MainNavOption
import com.konopelko.booksgoals.presentation.wishes.WishesViewModel

class AddBookNavigation(
    private val navController: NavController
) {

    fun onNavigate(intent: AddBookNavigationIntent) = when(intent) {
        NavigateToWishesScreen -> navigateToWishesScreen()
        is NavigateToAddGoalScreen -> navigateToAddGoalScreen(intent.book)
    }

    private fun navigateToWishesScreen() {
        navController.apply {
            navigate(MainNavOption.WishesScreen.name) {
                popUpTo(navController.graph.startDestinationId)
            }

            currentBackStackEntry?.apply {
                savedStateHandle[WishesViewModel.ARGS_WISH_BOOK_ADDED_KEY] = true
            }
        }
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