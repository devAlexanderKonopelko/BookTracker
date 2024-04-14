package com.konopelko.booksgoals.presentation.goals.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konopelko.booksgoals.R
import com.konopelko.booksgoals.domain.model.goal.Goal
import com.konopelko.booksgoals.presentation.common.theme.BooksGoalsAppTheme
import com.konopelko.booksgoals.presentation.common.theme.Typography
import com.konopelko.booksgoals.presentation.common.theme.backgroundCream
import com.konopelko.booksgoals.presentation.goals.GoalsIntent
import com.konopelko.booksgoals.presentation.goals.GoalsIntent.OnArgsReceived
import com.konopelko.booksgoals.presentation.goals.GoalsIntent.GoalsNavigationIntent
import com.konopelko.booksgoals.presentation.goals.GoalsIntent.GoalsNavigationIntent.NavigateToAddGoalScreen
import com.konopelko.booksgoals.presentation.goals.GoalsViewModel
import org.koin.androidx.compose.getViewModel

//todo:
// - Move strings to res
// - Use Theme Dimensions
@Composable
fun GoalsScreen(
    modifier: Modifier = Modifier,
    viewModel: GoalsViewModel = getViewModel(),
    onNavigate: (GoalsNavigationIntent) -> Unit,
    args: Boolean?
) = Column(
    modifier = modifier
        .fillMaxSize()
        .background(color = backgroundCream),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    val uiState by viewModel.uiState.collectAsState()

    var showGoalMenuOptions by remember { mutableStateOf(false) }
    var menuOptionGoalId by remember { mutableStateOf(0) }

    //todo: add key constant
    args?.let {
        LaunchedEffect("args_key") {
            viewModel.acceptIntent(OnArgsReceived(args))
        }
    }

    when {
        uiState.goals.isEmpty() -> HomeScreenContentNoGoals(
            onNavigate = onNavigate
        )
        else -> HomeScreenGoalsContent(
            goals = uiState.goals,
            onIntent = viewModel::acceptIntent,
            onNavigate = onNavigate,
            onGoalMenuClicked = { goalId ->
                showGoalMenuOptions = true
                menuOptionGoalId = goalId
            }
        )
    }

    if(showGoalMenuOptions) {
        GoalOptionsMenu(
            goalId = menuOptionGoalId,
            onOptionClicked = viewModel::acceptIntent,
            onDismiss = { showGoalMenuOptions = false }
        )
    }
}

@Composable
private fun HomeScreenGoalsContent(
    goals: List<Goal>,
    onIntent: (GoalsIntent) -> Unit,
    onNavigate: (GoalsNavigationIntent) -> Unit,
    onGoalMenuClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) = Box(
    modifier = modifier.fillMaxSize(),

) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "My goals",
            style = Typography.headlineLarge,
        )

        GoalsListContent(
            goals = goals,
            onIntent = onIntent,
            onGoalMenuClicked = onGoalMenuClicked
        )
    }

    FloatingActionButton(
        modifier = Modifier
            .padding(
                end = 16.dp,
                bottom = 16.dp
            )
            .align(Alignment.BottomEnd),
        onClick = { onNavigate(NavigateToAddGoalScreen) }
    ) {
        Icon(Icons.Filled.Add,"")
    }
}

@Composable
private fun GoalsListContent(
    goals: List<Goal>,
    onIntent: (GoalsIntent) -> Unit,
    onGoalMenuClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .padding(
                top = 24.dp,
                start = 16.dp,
                end = 16.dp
            )
    ) {
        items(goals) { goal ->
            GoalCard(
                goal = goal,
                onIntent = onIntent,
                onGoalMenuClicked = onGoalMenuClicked
            )
        }
    }
}

@Composable
private fun GoalCard(
    goal: Goal,
    onIntent: (GoalsIntent) -> Unit,
    onGoalMenuClicked: (Int) -> Unit
) = Row(
    modifier = Modifier
        .fillMaxWidth()
        .padding(top = 16.dp)
        .background(color = Color.LightGray),
    verticalAlignment = Alignment.CenterVertically
) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .background(
                color = Color.Gray,
                shape = RoundedCornerShape(4.dp)
            )
    )

    Column(
        modifier = Modifier.padding(start = 8.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(0.85f),
                text = goal.bookName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            IconButton(
                onClick = { onGoalMenuClicked(goal.id) }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_options_menu),
                    contentDescription = ""
                )
            }
        }
        Row {
            Text(text = goal.bookAuthor)

            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = goal.publishYear.toString()
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            LinearProgressIndicator(
                progress = { (goal.progress.toFloat() / 100) },
                trackColor = Color.Gray
            )

            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = "${goal.progress}%" // todo: make res with param
            )
        }
    }
}

@Composable
private fun HomeScreenContentNoGoals(
    modifier: Modifier = Modifier,
    onNavigate: (GoalsNavigationIntent) -> Unit = {}
) = Column(
    modifier = modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Text(
        text = "No goals yet",
        textAlign = TextAlign.Center
    )

    Button(onClick = { onNavigate(NavigateToAddGoalScreen) }) {
        Text(text = "Add Goal")
    }
}

//todo: remove
@Composable
private fun WishListOptionContent(
    title: String,
    isSelected: Boolean,
    onSelected: () -> Unit
) = Column(
    modifier = Modifier,
    verticalArrangement = Arrangement.Center
) {
    var expanded by remember { mutableStateOf(false) }
    val items = listOf("AAAAA", "BBBBB", "CCCCC", "DDDDD", "EEEEE", "FFFFF")
    var selectedIndex by remember { mutableStateOf(0) }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = {
                onSelected()
            }
        )

        Text(
            text = title
        )

        AnimatedVisibility(visible = isSelected) {
            Box(
                modifier = Modifier
                    .padding(start = 16.dp)
            ) {
                Text(
                    modifier = Modifier
                        .clickable(
                            onClick = {
                                expanded = true
                            }
                        ),
                    text = items[selectedIndex]
                )

                DropdownMenu(
                    modifier = Modifier
                        .height(100.dp),
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    items.forEachIndexed { index, item ->
                        DropdownMenuItem(
                            text = { Text(text = item) },
                            onClick = {
                                selectedIndex = index
                                expanded = false
                            },
                            colors = MenuDefaults.itemColors(
                                textColor = Color.Black
                            )
                        )
                    }
                }

            }
        }
    }
}

//todo: remove
@Composable
private fun NewBookOptionContent(
    title: String,
    isSelected: Boolean,
    onSelected: () -> Unit,
) = Column {
    var inputText by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val items = listOf("New A", "New B", "New C", "Add new book")
    var selectedIndex: Int? by remember { mutableStateOf(null) }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = { onSelected() }
        )

        Text(
            text = title
        )
    }

    AnimatedVisibility(visible = isSelected) {
        Column {
            TextField(
                value = inputText,
                onValueChange = {
                    inputText = it
                    expanded = it.isNotEmpty()
                },
                placeholder = {
                    Text(text = "Type book name")
                }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "smth"
                )
                DropdownMenu(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    items.forEachIndexed { index, item ->
                        DropdownMenuItem(
                            text = { Text(text = item) },
                            onClick = {
                                selectedIndex = index
                                expanded = false
                            },
                            colors = MenuDefaults.itemColors(
                                textColor = Color.Black
                            )
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreviewGoalsList() = BooksGoalsAppTheme {
    HomeScreenGoalsContent(
        goals = listOf(
            Goal(
                id = 0,
                bookName = "Book Asdfgsdfg dsfgsdfgsdfg sdfgsdfgsdfg",
                bookAuthor = "Author A",
                publishYear = 2005,
                progress = 1
            ),
            Goal(
                id = 0,
                bookName = "Book B",
                bookAuthor = "Author B",
                publishYear = 2006,
                progress = 65
            )
        ),
        onIntent = {},
        onNavigate = {},
        onGoalMenuClicked = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreviewNoGoals() = BooksGoalsAppTheme {
    HomeScreenContentNoGoals()
}
