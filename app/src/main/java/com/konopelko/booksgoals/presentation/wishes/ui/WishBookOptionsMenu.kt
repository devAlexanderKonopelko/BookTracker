package com.konopelko.booksgoals.presentation.wishes.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import com.konopelko.booksgoals.domain.model.book.Book
import com.konopelko.booksgoals.presentation.wishes.model.WishBookMenuOption
import com.konopelko.booksgoals.presentation.common.theme.BooksGoalsAppTheme
import com.konopelko.booksgoals.presentation.wishes.WishesIntent
import com.konopelko.booksgoals.presentation.wishes.WishesIntent.OnWishBookMenuOptionClicked
import kotlinx.coroutines.launch

// todo: create [BaseMenuBottomSheet]
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WishBookOptionsMenu(
    book: Book,
    onDismiss: () -> Unit,
    onOptionClicked: (WishesIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val modalBottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        GoalOptionsList(
            book = book,
            bottomSheetState = modalBottomSheetState,
            onDismiss = onDismiss,
            onOptionClicked = onOptionClicked
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GoalOptionsList(
    book: Book,
    bottomSheetState: SheetState,
    onDismiss: () -> Unit,
    onOptionClicked: (WishesIntent) -> Unit,
    modifier: Modifier = Modifier
) = Column(
    modifier = modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
) {
    val coroutineScope = rememberCoroutineScope()
    val options = WishBookMenuOption.entries

    options.forEach { option ->
        TextButton(
            onClick = {
                onOptionClicked(
                    OnWishBookMenuOptionClicked(
                        book = book,
                        wishBookMenuOption = option
                    )
                )

                coroutineScope.launch {
                    bottomSheetState.hide()
                    onDismiss()
                }
            }
        ) {
            Text(
                text = option.text,
                color = if (option == WishBookMenuOption.DELETE) Color.Red else Color.Black
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun NotFullProgressGoalOptionMenuPreview() = BooksGoalsAppTheme {
    GoalOptionsList(
        book = Book(),
        bottomSheetState = rememberModalBottomSheetState(),
        onDismiss = {},
        onOptionClicked = {}
    )
}