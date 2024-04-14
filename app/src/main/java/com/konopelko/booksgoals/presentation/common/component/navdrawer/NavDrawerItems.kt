package com.konopelko.booksgoals.presentation.common.component.navdrawer

import com.konopelko.booksgoals.R.string
import com.konopelko.booksgoals.presentation.common.component.navdrawer.model.AppDrawerItemInfo
import com.konopelko.booksgoals.presentation.navigation.MainNavOption.AchievementsScreen
import com.konopelko.booksgoals.presentation.navigation.MainNavOption.BooksScreen
import com.konopelko.booksgoals.presentation.navigation.MainNavOption.GoalsScreen
import com.konopelko.booksgoals.presentation.navigation.MainNavOption.StatisticsScreen
import com.konopelko.booksgoals.presentation.navigation.MainNavOption.WishesScreen

object NavDrawerItems {
    val drawerButtons = arrayListOf(
        AppDrawerItemInfo(
            WishesScreen,
            string.side_menu_wishes_title
        ),
        AppDrawerItemInfo(
            GoalsScreen,
            string.side_menu_goals_title
        ),
        AppDrawerItemInfo(
            BooksScreen,
            string.side_menu_books_title
        ),
        AppDrawerItemInfo(
            StatisticsScreen,
            string.side_menu_statistics_title
        ),
        AppDrawerItemInfo(
            AchievementsScreen,
            string.side_menu_achievements_title
        )
    )
}