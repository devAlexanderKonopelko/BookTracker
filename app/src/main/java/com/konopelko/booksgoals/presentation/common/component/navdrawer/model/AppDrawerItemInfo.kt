package com.konopelko.booksgoals.presentation.common.component.navdrawer.model

import androidx.annotation.StringRes

data class AppDrawerItemInfo<T>(
    val drawerOption: T,
    @StringRes val title: Int
)
