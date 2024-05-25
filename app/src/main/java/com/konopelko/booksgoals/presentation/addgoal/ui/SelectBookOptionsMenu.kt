package com.konopelko.booksgoals.presentation.addgoal.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TextButton
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konopelko.booksgoals.presentation.addgoal.model.SelectBookOption
import com.konopelko.booksgoals.presentation.common.theme.BooksGoalsAppTheme
import kotlinx.coroutines.launch

// todo: create [BaseMenuBottomSheet]
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SelectBookOptionsMenu(
    onDismiss: () -> Unit,
    onOptionClicked: (SelectBookOption) -> Unit,
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
            bottomSheetState = modalBottomSheetState,
            onDismiss = onDismiss,
            onOptionClicked = onOptionClicked
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GoalOptionsList(
    bottomSheetState: SheetState,
    onDismiss: () -> Unit,
    onOptionClicked: (SelectBookOption) -> Unit,
    modifier: Modifier = Modifier
) = Column(
    modifier = modifier
        .fillMaxWidth()
        .padding(bottom = 16.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
) {
    val coroutineScope = rememberCoroutineScope()
    val options = SelectBookOption.entries

    Text(
        modifier = Modifier.padding(vertical = 16.dp),
        text = "Каким образом вы хотите выбрать книгу?",
        style = MaterialTheme.typography.titleLarge,
        textAlign = TextAlign.Center
    )

    options.forEach { option ->
        TextButton(
            onClick = {
                onOptionClicked(option)

                coroutineScope.launch {
                    bottomSheetState.hide()
                    onDismiss()
                }
            }
        ) {
            Text(
                text = option.text,
                color = Color.Black
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun SelectBookOptionsMenuPreview() = BooksGoalsAppTheme {
    GoalOptionsList(
        bottomSheetState = rememberModalBottomSheetState(),
        onDismiss = {},
        onOptionClicked = {}
    )
}