package com.konopelko.booksgoals.presentation.wishes.navigation

import android.util.Log
import androidx.navigation.NavController
import com.konopelko.booksgoals.domain.model.book.Book
import com.konopelko.booksgoals.domain.model.booksearch.SearchScreenOrigin
import com.konopelko.booksgoals.presentation.addgoal.AddGoalViewModel
import com.konopelko.booksgoals.presentation.navigation.MainNavOption
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksViewModel
import com.konopelko.booksgoals.presentation.wishes.WishesIntent.WishesNavigationIntent
import com.konopelko.booksgoals.presentation.wishes.WishesIntent.WishesNavigationIntent.NavigateToAddGoalScreen
import com.konopelko.booksgoals.presentation.wishes.WishesIntent.WishesNavigationIntent.NavigateToSearchBooksScreen

class WishesNavigation(
    private val navController: NavController
) {

    fun onNavigate(intent: WishesNavigationIntent) = when(intent) {
        NavigateToSearchBooksScreen -> onNavigateToSearchBooksScreen()
        is NavigateToAddGoalScreen -> onNavigateToAddGoalScreen(intent.book)
    }

    private fun onNavigateToSearchBooksScreen() {
        with(navController) {
            navigate(prepareSearchScreenNameWithArgs(SearchScreenOrigin.ADD_WISH_BOOK)) {
                currentBackStackEntry?.apply {
                    Log.e("WishesNavigation", "set search screen origin")
                    savedStateHandle[SearchBooksViewModel.ARGS_SCREEN_ORIGIN_KEY] =
                        SearchScreenOrigin.ADD_WISH_BOOK
                }
            }
        }
    }

    //todo: move to SearchScreenNavigation
    private fun prepareSearchScreenNameWithArgs(screenOrigin: SearchScreenOrigin): String =
        "SearchBooksScreen/{$screenOrigin}"

    private fun onNavigateToAddGoalScreen(book: Book) {
        with(navController) {
            navigate(MainNavOption.AddGoalScreen.name) {
                currentBackStackEntry?.apply {
                    savedStateHandle[AddGoalViewModel.ARGS_BOOK_KEY] = book
                }
            }
        }
    }
}