package com.konopelko.booksgoals.presentation.wishes

import com.konopelko.booksgoals.domain.model.book.Book
import com.konopelko.booksgoals.presentation.wishes.model.WishBookMenuOption
import com.konopelko.booksgoals.presentation.wishes.model.WishesArgs

sealed interface WishesIntent {

    data object HideWishBookDeletedMessage: WishesIntent
    data object ResetNavigateToAddGoalScreen : WishesIntent

    data class OnArgsReceived(val args: WishesArgs) : WishesIntent

    data class OnWishBookMenuOptionClicked(
        val book: Book,
        val wishBookMenuOption: WishBookMenuOption
    ) : WishesIntent

    sealed interface WishesNavigationIntent {

        data object NavigateToSearchBooksScreen : WishesNavigationIntent

        data class NavigateToAddGoalScreen(val book: Book) : WishesNavigationIntent
    }
}