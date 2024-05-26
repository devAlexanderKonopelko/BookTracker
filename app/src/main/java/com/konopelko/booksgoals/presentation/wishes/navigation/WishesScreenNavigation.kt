package com.konopelko.booksgoals.presentation.wishes.navigation

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.google.gson.Gson
import com.konopelko.booksgoals.domain.model.book.Book
import com.konopelko.booksgoals.presentation.searchbooks.model.SearchScreenOrigin
import com.konopelko.booksgoals.presentation.addgoal.AddGoalViewModel
import com.konopelko.booksgoals.presentation.addgoal.model.AddGoalArgs
import com.konopelko.booksgoals.presentation.addgoal.model.AddGoalScreenOrigin
import com.konopelko.booksgoals.presentation.common.base.navigation.BaseScreenNavigation
import com.konopelko.booksgoals.presentation.common.utils.uri.serializeNavParam
import com.konopelko.booksgoals.presentation.navigation.MainNavOption
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksViewModel
import com.konopelko.booksgoals.presentation.searchbooks.model.SearchBooksArgs
import com.konopelko.booksgoals.presentation.wishes.WishesIntent.WishesNavigationIntent
import com.konopelko.booksgoals.presentation.wishes.WishesIntent.WishesNavigationIntent.NavigateToAddGoalScreen
import com.konopelko.booksgoals.presentation.wishes.WishesIntent.WishesNavigationIntent.NavigateToSearchBooksScreen
import com.konopelko.booksgoals.presentation.wishes.WishesViewModel
import com.konopelko.booksgoals.presentation.wishes.model.WishesArgs
import com.konopelko.booksgoals.presentation.wishes.model.WishesArgsType
import com.konopelko.booksgoals.presentation.wishes.ui.WishesScreen

class WishesScreenNavigation(
    private val navController: NavController
) : BaseScreenNavigation<WishesNavigationIntent, WishesArgs>(
    screenName = MainNavOption.WishesScreen.name,
    defaultOptionalArgs = WishesArgs(),
    optionalArgsKey = WishesViewModel.ARGS_KEY,
    optionalArgsType = WishesArgsType()
) {

    @Composable
    override fun ScreenComposable(
        onNavigate: (WishesNavigationIntent) -> Unit,
        args: WishesArgs
    ) = WishesScreen(
        onNavigate = onNavigate,
        args = args
    )

    override fun onNavigate(intent: WishesNavigationIntent) = when (intent) {
        NavigateToSearchBooksScreen -> onNavigateToSearchBooksScreen()
        is NavigateToAddGoalScreen -> onNavigateToAddGoalScreen(intent.book)
    }

    private fun onNavigateToSearchBooksScreen() {
        with(navController) {
            navigate(prepareSearchScreenNameWithArgs(SearchScreenOrigin.ADD_WISH_BOOK))
        }
    }

    private fun onNavigateToAddGoalScreen(book: Book) {
        navController.navigate(prepareAddGoalScreenNameWithArgs(book))
    }

    //todo: move to SearchScreenNavigation
    private fun prepareSearchScreenNameWithArgs(screenOrigin: SearchScreenOrigin): String =
        "${MainNavOption.SearchBooksScreen.name}?" +
                "${SearchBooksViewModel.ARGS_SCREEN_ORIGIN_KEY}=${
                    serializeNavParam(
                        SearchBooksArgs(
                            screenOrigin
                        )
                    )
                }"

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

    companion object {

        //todo: refactor and apply to every screen name preparation
        fun prepareWishesScreenNameWithArgs(
            isBookAdded: Boolean,
            isSelectBookForGoal: Boolean
        ): String =
            "${MainNavOption.WishesScreen.name}?" +
                    "${WishesViewModel.ARGS_KEY}=${
                        serializeNavParam(
                            WishesArgs(
                                isWishBookAdded = isBookAdded,
                                isSelectBookForGoal = isSelectBookForGoal
                            )
                        )
                    }"
    }
}