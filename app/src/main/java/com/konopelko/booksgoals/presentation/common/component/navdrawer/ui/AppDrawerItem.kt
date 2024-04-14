package com.konopelko.booksgoals.presentation.common.component.navdrawer.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.konopelko.booksgoals.presentation.common.component.navdrawer.model.AppDrawerItemInfo
import com.konopelko.booksgoals.presentation.common.theme.textWhite

@Composable
fun <T> AppDrawerItem(
    modifier: Modifier = Modifier,
    item: AppDrawerItemInfo<T>,
    onClick: (options: T) -> Unit
) = Row(
    modifier = modifier.clickable { onClick(item.drawerOption) },
    verticalAlignment = Alignment.CenterVertically
) {
    Text(
        text = stringResource(id = item.title),
        style = MaterialTheme.typography.titleMedium,
        color = textWhite
    )
}
