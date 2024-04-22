package com.konopelko.booksgoals.presentation.wishes.navigation

import android.net.Uri
import android.util.Log
import androidx.navigation.NavController
import com.google.gson.Gson
import com.konopelko.booksgoals.domain.model.book.Book
import com.konopelko.booksgoals.presentation.searchbooks.model.SearchScreenOrigin
import com.konopelko.booksgoals.presentation.addbook.AddBookViewModel
import com.konopelko.booksgoals.presentation.addgoal.AddGoalViewModel
import com.konopelko.booksgoals.presentation.addgoal.model.AddGoalArgs
import com.konopelko.booksgoals.presentation.addgoal.model.AddGoalScreenOrigin
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
                //todo: remove and check
                currentBackStackEntry?.apply {
                    Log.e("WishesNavigation", "set search screen origin")
                    savedStateHandle[SearchBooksViewModel.ARGS_SCREEN_ORIGIN_KEY] =
                        SearchScreenOrigin.ADD_WISH_BOOK
                }
            }
        }
    }

    private fun onNavigateToAddGoalScreen(book: Book) {
       navController.navigate(prepareAddGoalScreenNameWithArgs(book))
    }

    //todo: move to SearchScreenNavigation
    private fun prepareSearchScreenNameWithArgs(screenOrigin: SearchScreenOrigin): String =
        "${MainNavOption.SearchBooksScreen.name}?screen_origin=$screenOrigin"

    //todo: move to AddGoalScreenNavigation
    private fun prepareAddGoalScreenNameWithArgs(book: Book): String =
        "${MainNavOption.AddGoalScreen.name}?${AddGoalViewModel.ARGS_ADD_GOAL_KEY}=" +
                Uri.encode(
                    Gson().toJson(
                        AddGoalArgs(
                            screenOrigin = AddGoalScreenOrigin.ADD_WISH_BOOK,
                            selectedBook = book
                        )
                    )
                )
}