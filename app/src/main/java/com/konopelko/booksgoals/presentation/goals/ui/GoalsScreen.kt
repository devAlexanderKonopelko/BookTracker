package com.konopelko.booksgoals.presentation.goals.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.konopelko.booksgoals.R
import com.konopelko.booksgoals.domain.model.goal.Goal
import com.konopelko.booksgoals.presentation.common.theme.BookTrackerTheme
import com.konopelko.booksgoals.presentation.common.theme.Typography
import com.konopelko.booksgoals.presentation.common.utils.border.drawRightBorder
import com.konopelko.booksgoals.presentation.goals.GoalsIntent.OnArgsReceived
import com.konopelko.booksgoals.presentation.goals.GoalsIntent.GoalsNavigationIntent
import com.konopelko.booksgoals.presentation.goals.GoalsIntent.GoalsNavigationIntent.NavigateToAddGoalScreen
import com.konopelko.booksgoals.presentation.goals.GoalsIntent.GoalsNavigationIntent.NavigateToGoalDetailsScreen
import com.konopelko.booksgoals.presentation.goals.GoalsIntent.HideGoalCompletedMessage
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
    modifier = modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    var showGoalMenuOptions by remember { mutableStateOf(false) }
    var menuOptionGoal by remember { mutableStateOf(Goal()) }

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
            onNavigate = onNavigate,
            onGoalMenuClicked = { goal ->
                showGoalMenuOptions = true
                menuOptionGoal = goal
            }
        )
    }

    if (showGoalMenuOptions) {
        GoalOptionsMenu(
            goal = menuOptionGoal,
            onOptionClicked = viewModel::acceptIntent,
            onDismiss = { showGoalMenuOptions = false }
        )
    }

    if (uiState.showGoalCompletedMessage) {
        LaunchedEffect("toast key") {
            Toast.makeText(context, "Цель успешна завершена!", Toast.LENGTH_SHORT).show()
            viewModel.acceptIntent(HideGoalCompletedMessage)
        }
    }
}

@Composable
private fun HomeScreenGoalsContent(
    goals: List<Goal>,
    onNavigate: (GoalsNavigationIntent) -> Unit,
    onGoalMenuClicked: (Goal) -> Unit,
    modifier: Modifier = Modifier
) = Box(
    modifier = modifier.fillMaxSize(),
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = "Мои цели",
            style = Typography.headlineLarge,
        )

        GoalsListContent(
            goals = goals,
            onNavigate = onNavigate,
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
        Icon(Icons.Filled.Add, "")
    }
}

@Composable
private fun GoalsListContent(
    goals: List<Goal>,
    onNavigate: (GoalsNavigationIntent) -> Unit,
    onGoalMenuClicked: (Goal) -> Unit,
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
                onGoalMenuClicked = onGoalMenuClicked,
                onNavigate = onNavigate
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun GoalCard(
    goal: Goal,
    onGoalMenuClicked: (Goal) -> Unit,
    onNavigate: (GoalsNavigationIntent) -> Unit
) = Row(
    modifier = Modifier
        .fillMaxWidth()
        .padding(top = 16.dp)
        .let {
            if(goal.isFrozen.not()) {
                it.clickable { onNavigate(NavigateToGoalDetailsScreen(goal.id)) }
            } else it
        }

        .background(
            color = getGoalCardBackgroundColor(goal),
            shape = RoundedCornerShape(4.dp)
        )
        .drawRightBorder(
            strokeWidth = 20.dp,
            color = getGoalCardRightBorderColor(goal),
            shape = RoundedCornerShape(4.dp)
        ),
    verticalAlignment = Alignment.CenterVertically
) {

    if(goal.bookCoverUrl.isNotEmpty()) {
        GlideImage(
            modifier = Modifier
                .size(60.dp)
                .padding(start = 8.dp),
            model = goal.bookCoverUrl,
            contentDescription = "Book image",
            loading = placeholder(R.drawable.ic_default_book),
            failure = placeholder(R.drawable.ic_default_book)
        )
    } else {
        Image(
            modifier = Modifier
                .size(60.dp)
                .padding(start = 8.dp),
            painter = painterResource(id = R.drawable.ic_default_book),
            contentDescription = ""
        )
    }

    Column(
        modifier = Modifier.padding(
            top = 8.dp,
            start = 12.dp
        ),
        verticalArrangement = Arrangement.Center
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(0.8f),
                text = goal.bookName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            IconButton(
                onClick = { onGoalMenuClicked(goal) }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_options_menu),
                    contentDescription = ""
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp)
        ) {
            Text(
                modifier = Modifier.weight(1f, fill = false),
                text = goal.bookAuthor,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = ", ${goal.bookPublishYear}",
                color = Color.Gray
            )
        }

        Row(
            modifier = Modifier.padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(0.7f),
                progress = { (goal.progress.toFloat() / 100) },
                trackColor = Color.LightGray,
                color = getGoalProgressIndicatorColor(goal)
            )

            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = "${goal.progress}%" // todo: make res with param
            )

            if(goal.isFrozen) {
                Icon(
                    modifier = Modifier
                        .size(28.dp)
                        .padding(start = 8.dp),
                    painter = painterResource(id = R.drawable.ic_snowflake),
                    contentDescription = "",
                    tint = Color(0xFF5185D6)
                )
            }
        }
    }
}

private fun getGoalProgressIndicatorColor(goal: Goal): Color =
    when {
        goal.progress == 100 -> Color(0xFF00A45F)
        goal.isFrozen -> Color(0xFF5185D6)
        else -> Color.DarkGray
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
        text = "У вас ещё нет целей. Добавьте новую цель прямо сейчас!",
        textAlign = TextAlign.Center
    )

    Button(onClick = { onNavigate(NavigateToAddGoalScreen) }) {
        Text(text = "Добавить цель")
    }
}

private fun getGoalCardBackgroundColor(goal: Goal): Color =
    when {
        goal.progress == 100 -> Color(0xFFCEF7E6)
        goal.isFrozen -> Color(0xFFD0E3FF)
        else -> Color(0xFFE7E7E7)
    }

private fun getGoalCardRightBorderColor(goal: Goal): Color =
    when {
        goal.progress == 100 -> Color(0xFF00A45F)
        goal.isFrozen -> Color(0xFF5185D6)
        else -> Color(0xFF818181)
    }

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreviewGoalsList() = BookTrackerTheme {
    HomeScreenGoalsContent(
        goals = listOf(
            Goal(
                id = 0,
                bookName = "Book Asdfgsdfg dsfgsdfgsdfg sdfgsdfgsdfg",
                bookAuthor = "Author A",
                bookPublishYear = 2005,
                progress = 99,
                isFrozen = true
            ),
            Goal(
                id = 0,
                bookName = "Book B",
                bookAuthor = "Author B sdfg sdfg sdfg sdfg sdfg sdfg sdfg sdfg sdfg",
                bookPublishYear = 2006,
                progress = 65
            ),
            Goal(
                id = 0,
                bookName = "Book C",
                bookAuthor = "Author C",
                bookPublishYear = 2045,
                progress = 100
            )
        ),
        onNavigate = {},
        onGoalMenuClicked = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreviewNoGoals() = BookTrackerTheme {
    HomeScreenContentNoGoals()
}
