package com.konopelko.booksgoals.presentation.wishes

import com.konopelko.booksgoals.domain.model.book.Book

data class WishesUiState(
    val wishesBooks: List<Book> = emptyList(),
    val showWishBookDeletedMessage: Boolean = false,
    val isSelectBookForGoal: Boolean = false,

    //todo: refactor somehow to use NOT in uiState
    val shouldNavigateToAddGoalScreen: Boolean = false,
    val bookToStartGoalWith: Book = Book()
) {

    sealed interface WishesPartialState {

        data object HideWishBookDeletedMessageState : WishesPartialState
        data object ResetNavigateToAddGoalState : WishesPartialState
        data object SelectBookForGoalState : WishesPartialState

        data class WishesBooksLoaded(val books: List<Book>) : WishesPartialState
        data class WishBookDeletedSuccessfully(val books: List<Book>) : WishesPartialState
        data class StartGoalClickedState(val book: Book) : WishesPartialState
    }
}
