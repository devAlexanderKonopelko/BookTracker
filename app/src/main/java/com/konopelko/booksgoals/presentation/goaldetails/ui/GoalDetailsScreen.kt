package com.konopelko.booksgoals.presentation.goaldetails.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konopelko.booksgoals.R
import com.konopelko.booksgoals.domain.model.goal.Goal
import com.konopelko.booksgoals.presentation.goaldetails.GoalDetailsIntent
import com.konopelko.booksgoals.presentation.goaldetails.GoalDetailsIntent.GoalDetailsNavigationIntent
import com.konopelko.booksgoals.presentation.goaldetails.GoalDetailsIntent.GoalDetailsNavigationIntent.NavigateToEditGoal
import com.konopelko.booksgoals.presentation.goaldetails.GoalDetailsIntent.GoalDetailsNavigationIntent.NavigateToGoalStatistics
import com.konopelko.booksgoals.presentation.goaldetails.GoalDetailsIntent.OnAddProgressClicked
import com.konopelko.booksgoals.presentation.goaldetails.GoalDetailsIntent.OnArgsReceived
import com.konopelko.booksgoals.presentation.goaldetails.GoalDetailsIntent.OnBookStatisticsClicked
import com.konopelko.booksgoals.presentation.goaldetails.GoalDetailsIntent.OnCloseProgressMarkDialog
import com.konopelko.booksgoals.presentation.goaldetails.GoalDetailsIntent.OnSaveProgressClicked
import com.konopelko.booksgoals.presentation.goaldetails.GoalDetailsUiState
import com.konopelko.booksgoals.presentation.goaldetails.GoalDetailsViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun GoalDetailsScreen(
    viewModel: GoalDetailsViewModel = getViewModel(),
    onNavigate: (GoalDetailsNavigationIntent) -> Unit,
    args: Int?
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    GoalDetailsContent(
        uiState = uiState,
        onIntent = viewModel::acceptIntent,
        onNavigate = onNavigate
    )

    args?.let {
        LaunchedEffect("args_key") {
            viewModel.acceptIntent(OnArgsReceived(goalId = it))
        }
    }

    if(uiState.showMarkProgressDialog) {
        ProgressMarkDialog(
            pagesLeftAmount = uiState.goal.bookPagesAmount - uiState.goal.completedPagesAmount,
            onDismissRequest = { viewModel.acceptIntent(OnCloseProgressMarkDialog) },
            onSaveProgressClicked = { viewModel.acceptIntent(OnSaveProgressClicked(it)) }
        )
    }
}

@Composable
fun GoalDetailsContent(
    uiState: GoalDetailsUiState,
    onIntent: (GoalDetailsIntent) -> Unit,
    onNavigate: (GoalDetailsNavigationIntent) -> Unit,
) = Column(
    modifier = Modifier
        .fillMaxSize()
        .verticalScroll(state = rememberScrollState()),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    with(uiState.goal) {
        GoalHeader(
            bookName = bookName,
            bookAuthor = bookAuthor,
            bookPublishYear = bookPublishYear
        )
        GoalProgressContent(
            completedPagesAmount = completedPagesAmount,
            bookPagesAmount = bookPagesAmount,
            progress = progress,
            onIntent = onIntent
        )

        GoalInfoContent(
            daysInProgress = daysInProgress,
            averageReadSpeed = averageReadSpeed
        )
        ExpectedInfoContent(
            expectedPagesPerDay = expectedPagesPerDay,
            expectedFinishDaysAmount = expectedFinishDaysAmount
        )
        EditGoalContent(
            goal = this,
            enabled = progress != 100,
            onNavigate = onNavigate
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier
                .padding(top = 16.dp),
            onClick = { onNavigate(NavigateToGoalStatistics) }
        ) {
            Text(text = "Статистика книги")
        }
    }
}

@Composable
private fun EditGoalContent(
    goal: Goal,
    enabled: Boolean,
    onNavigate: (GoalDetailsNavigationIntent) -> Unit
) = Row(
    modifier = Modifier.padding(top = 16.dp),
    horizontalArrangement = Arrangement.Center
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_edit_goal),
        contentDescription = "",
        tint = if(enabled) Color(0xFF393939) else Color.Gray
    )
    Text(
        modifier = Modifier
            .padding(start = 4.dp)
            .clickable {
                if(enabled) onNavigate(NavigateToEditGoal(goal))
            },
        text = "Изменить цель",
        textDecoration = TextDecoration.Underline,
        color = if(enabled) Color.Black else Color.Gray
    )
}

@Composable
private fun GoalInfoContent(
    modifier: Modifier = Modifier,
    daysInProgress: Int,
    averageReadSpeed: Int
) = Column(
    modifier = modifier
        .fillMaxWidth()
        .padding(
            top = 32.dp,
            start = 32.dp
        )
) {
    Text(text = "Вы читаете книгу уже $daysInProgress дней")
    Text(
        modifier = Modifier.padding(top = 4.dp),
        text = "Средняя скорость чтения $averageReadSpeed стр./день"
    )
}

@Composable
private fun ExpectedInfoContent(
    expectedPagesPerDay: Int,
    expectedFinishDaysAmount: Int
) = Column(
    modifier = Modifier
        .fillMaxWidth()
        .padding(start = 32.dp)
) {
    Row(
        modifier = Modifier.padding(top = 32.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_goal_flag),
            contentDescription = "",
            tint = Color(0xFF4B6CA1)
        )
        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = "Цель"
        )
    }
    Text(text = "Страниц в день: $expectedPagesPerDay")
    Text(
        modifier = Modifier.padding(top = 4.dp),
        text = "Ожидаемое время прочтения: $expectedFinishDaysAmount дней"
    )
}

@Composable
private fun GoalProgressContent(
    completedPagesAmount: Int,
    bookPagesAmount: Int,
    progress: Int,
    onIntent: (GoalDetailsIntent) -> Unit
) = Column(
    modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Text(
        text = "Выполнение",
        style = MaterialTheme.typography.titleLarge
    )
    LinearProgressIndicator(
        modifier = Modifier
            .padding(top = 8.dp)
            .height(16.dp)
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = RoundedCornerShape(2.dp)
            ),
        progress = { progress.toFloat() / 100 },
        trackColor = Color.White,
        color = Color(0xFF88DEAE)
    )
    Text(
        modifier = Modifier.padding(top = 8.dp),
        text = "$completedPagesAmount/$bookPagesAmount стр."
    )

    Button(
        modifier = Modifier
            .padding(top = 16.dp),
        onClick = { onIntent(OnAddProgressClicked) },
        enabled = progress != 100
    ) {
        Text(text = "Отметить выполнение")
    }
}

@Composable
private fun GoalHeader(
    bookName: String,
    bookAuthor: String,
    bookPublishYear: Int
) = Column(
    modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 24.dp,
                start = 16.dp,
                end = 16.dp
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = bookName,
            style = MaterialTheme.typography.titleLarge,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            modifier = Modifier
                .weight(0.4f)
                .padding(start = 8.dp),
            text = "$bookAuthor, $bookPublishYear",
            overflow = TextOverflow.Ellipsis,
        )
    }

    Image(
        modifier = Modifier
            .fillMaxWidth(0.4f)
            .fillMaxHeight(0.3f)
            .padding(top = 16.dp),
        painter = painterResource(id = R.drawable.ic_default_book),
        contentDescription = ""
    )
}

@Preview(showBackground = true)
@Composable
fun GoalDetailsPreview() {
    GoalDetailsContent(
        uiState = GoalDetailsUiState(
            goal = Goal(
                bookName = "The Lord of the Rings asdf asdf adf asdf asdf asdf asdf adsf",
                bookAuthor = "Tolkien asdf asdf adf asdf asdf asdf asdf adsf",
                daysInProgress = 10,
                averageReadSpeed = 20,
                bookPublishYear = 1960,
                completedPagesAmount = 435,
                bookPagesAmount = 870,
                progress = 100,
                expectedPagesPerDay = 40,
                expectedFinishDaysAmount = 8
            )
        ),
        onIntent = {},
        onNavigate = {},
    )
}
