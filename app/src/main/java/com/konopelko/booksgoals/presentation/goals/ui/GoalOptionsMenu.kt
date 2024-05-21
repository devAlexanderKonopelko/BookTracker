package com.konopelko.booksgoals.presentation.goals.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.TextButton
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import com.konopelko.booksgoals.domain.model.goal.Goal
import com.konopelko.booksgoals.presentation.goals.model.GoalMenuOption
import com.konopelko.booksgoals.presentation.goals.model.GoalMenuOption.DELETE
import com.konopelko.booksgoals.presentation.goals.model.GoalMenuOption.FREEZE
import com.konopelko.booksgoals.presentation.common.theme.BooksGoalsAppTheme
import com.konopelko.booksgoals.presentation.goals.GoalsIntent
import com.konopelko.booksgoals.presentation.goals.GoalsIntent.OnGoalOptionClicked
import kotlinx.coroutines.launch

// todo: create [BaseMenuBottomSheet]
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GoalOptionsMenu(
    goal: Goal,
    onDismiss: () -> Unit,
    onOptionClicked: (GoalsIntent) -> Unit,
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
            goal = goal,
            bottomSheetState = modalBottomSheetState,
            onDismiss = onDismiss,
            onOptionClicked = onOptionClicked
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GoalOptionsList(
    goal: Goal,
    bottomSheetState: SheetState,
    onDismiss: () -> Unit,
    onOptionClicked: (GoalsIntent) -> Unit,
    modifier: Modifier = Modifier
) = Column(
    modifier = modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
) {
    val coroutineScope = rememberCoroutineScope()
    val options = getGoalMenuOptions(goal)

    options.forEach { option ->
        TextButton(
            onClick = {
                onOptionClicked(
                    OnGoalOptionClicked(
                        goal = goal,
                        goalMenuOption = option
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
                color = if (option == DELETE) Color.Red else Color.Black
            )
        }
    }
}

private fun getGoalMenuOptions(goal: Goal): List<GoalMenuOption> = if(goal.progress == 100) {
    GoalMenuOption.entries.filter { it != FREEZE }
} else GoalMenuOption.entries

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun NotFullProgressGoalOptionMenuPreview() = BooksGoalsAppTheme {
    GoalOptionsList(
        goal = Goal(),
        bottomSheetState = rememberModalBottomSheetState(),
        onDismiss = {},
        onOptionClicked = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun FullProgressGoalOptionMenuPreview() = BooksGoalsAppTheme {
    GoalOptionsList(
        goal = Goal(progress = 100),
        bottomSheetState = rememberModalBottomSheetState(),
        onDismiss = {},
        onOptionClicked = {}
    )
}