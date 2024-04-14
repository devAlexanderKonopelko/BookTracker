package com.konopelko.booksgoals.presentation.common.component.navdrawer.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.konopelko.booksgoals.R
import com.konopelko.booksgoals.presentation.common.component.navdrawer.model.AppDrawerItemInfo
import com.konopelko.booksgoals.presentation.common.theme.backgroundDark
import com.konopelko.booksgoals.presentation.common.theme.backgroundWhite
import com.konopelko.booksgoals.presentation.common.theme.textWhite
import kotlinx.coroutines.launch

private val navigationItemPaddingTop = 20.dp

@Composable
fun <T : Enum<T>> NavDrawerContent(
    drawerState: DrawerState,
    menuItems: List<AppDrawerItemInfo<T>>,
    defaultPick: T,
    onClick: (T) -> Unit
) {
    var currentPick by remember { mutableStateOf(defaultPick) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(color = backgroundDark),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileHeader(
            modifier = Modifier.padding(top = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            itemsIndexed(menuItems) { index, item ->
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(paddingValues = getDividerPaddingByIndex(index)),
                    thickness = getDividerThicknessByIndex(index),
                    color = backgroundWhite
                )

                AppDrawerItem(
                    modifier = Modifier.padding(
                        top = navigationItemPaddingTop,
                        start = 24.dp
                    ),
                    item = item
                ) { navOption ->
                    if (currentPick == navOption) {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        return@AppDrawerItem
                    }

                    currentPick = navOption

                    coroutineScope.launch {
                        drawerState.close()
                    }
                    onClick(navOption)
                }
            }
        }
    }
}

@Composable
private fun ProfileHeader(
    modifier: Modifier = Modifier
) = Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Image(
        painter = painterResource(id = R.drawable.ic_profile),
        contentDescription = ""
    )
    Text(
        text = stringResource(id = R.string.side_menu_header_username_text),
        style = MaterialTheme.typography.headlineMedium,
        color = textWhite
    )
}

private fun getDividerPaddingByIndex(index: Int): PaddingValues =
    if (index == 0) {
        PaddingValues(
            top = 24.dp,
            start = 6.dp,
            end = 6.dp
        )
    } else {
        PaddingValues(
            top = navigationItemPaddingTop,
            start = 24.dp,
            end = 24.dp
        )
    }

private fun getDividerThicknessByIndex(index: Int): Dp = if (index == 0) 1.dp else 0.5.dp