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
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.konopelko.booksgoals.R
import com.konopelko.booksgoals.domain.model.goal.Goal
import com.konopelko.booksgoals.presentation.goaldetails.GoalDetailsIntent
import com.konopelko.booksgoals.presentation.goaldetails.GoalDetailsIntent.GoalDetailsNavigationIntent
import com.konopelko.booksgoals.presentation.goaldetails.GoalDetailsIntent.GoalDetailsNavigationIntent.NavigateToEditGoal
import com.konopelko.booksgoals.presentation.goaldetails.GoalDetailsIntent.GoalDetailsNavigationIntent.NavigateToGoalStatistics
import com.konopelko.booksgoals.presentation.goaldetails.GoalDetailsIntent.OnAddProgressClicked
import com.konopelko.booksgoals.presentation.goaldetails.GoalDetailsIntent.OnArgsReceived
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
            bookPublishYear = bookPublishYear,
            bookCoverUrl = bookCoverUrl
        )
        GoalProgressContent(
            completedPagesAmount = completedPagesAmount,
            bookPagesAmount = bookPagesAmount,
            progress = progress,
            onIntent = onIntent
        )

        Spacer(modifier = Modifier.height(32.dp))

        GoalInfoContent(
            daysInProgress = daysInProgress,
            averageReadSpeed = averageReadSpeed
        )
        ExpectedInfoContent(
            expectedPagesPerDay = expectedPagesPerDay,
            expectedFinishDaysAmount = expectedFinishDaysAmount
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            EditGoalButton(
                goal = this@with,
                enabled = progress != 100,
                onNavigate = onNavigate
            )

            Button(
                modifier = Modifier.padding(top = 16.dp),
                onClick = { onNavigate(NavigateToGoalStatistics(uiState.goal.id)) }
            ) {
                Text(text = "Статистика книги")
            }
        }
    }
}

@Composable
private fun EditGoalButton(
    goal: Goal,
    enabled: Boolean,
    onNavigate: (GoalDetailsNavigationIntent) -> Unit
) = Button(
    modifier = Modifier.padding(top = 16.dp),
    onClick = { if (enabled) onNavigate(NavigateToEditGoal(goal)) },
    enabled = enabled
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_edit_goal),
        contentDescription = "",
        tint = if (enabled) Color.White else Color.Gray
    )
    Text(
        modifier = Modifier
            .padding(start = 4.dp)
            .clickable {
                if (enabled) onNavigate(NavigateToEditGoal(goal))
            },
        text = "Изменить цель",
        textDecoration = TextDecoration.Underline,
        color = if (enabled) Color.White else Color.Gray,
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
        .padding(start = 32.dp)
) {
    Text(
        text = "Вы читаете книгу уже $daysInProgress дней",
        style = MaterialTheme.typography.bodyLarge
    )
    Text(
        modifier = Modifier.padding(top = 4.dp),
        text = "Средняя скорость чтения $averageReadSpeed стр./день",
        style = MaterialTheme.typography.bodyLarge
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
        modifier = Modifier.padding(top = 32.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_goal_flag),
            contentDescription = "",
            tint = Color(0xFF4B6CA1)
        )
        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = "Цель",
            style = MaterialTheme.typography.titleMedium
        )
    }
    Text(
        modifier = Modifier.padding(top = 8.dp),
        text = "Страниц в день: $expectedPagesPerDay",
        style = MaterialTheme.typography.bodyLarge
    )
    Text(
        modifier = Modifier.padding(top = 4.dp),
        text = "Ожидаемое время прочтения: $expectedFinishDaysAmount дней",
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
private fun GoalProgressContent(
    completedPagesAmount: Int,
    bookPagesAmount: Int,
    progress: Int,
    onIntent: (GoalDetailsIntent) -> Unit
) = Column(
    modifier = Modifier
        .fillMaxWidth()
        .padding(top = 32.dp),
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

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun GoalHeader(
    bookName: String,
    bookAuthor: String,
    bookPublishYear: Int,
    bookCoverUrl: String
) = Column(
    modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally
) {

    if(bookCoverUrl.isNotEmpty()) {
        GlideImage(
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .fillMaxHeight(0.3f)
                .padding(top = 32.dp),
            model = bookCoverUrl,
            contentDescription = "Book image",
            loading = placeholder(R.drawable.ic_default_book),
            failure = placeholder(R.drawable.ic_default_book)
        )
    } else {
        Image(
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .fillMaxHeight(0.3f)
                .padding(top = 32.dp),
            painter = painterResource(id = R.drawable.ic_default_book),
            contentDescription = ""
        )
    }


    Text(
        modifier = Modifier.padding(top = 16.dp),
        text = bookName,
        style = MaterialTheme.typography.titleLarge,
        textAlign = TextAlign.Center,
        overflow = TextOverflow.Ellipsis
    )
    Text(
        modifier = Modifier,
        text = "$bookAuthor, $bookPublishYear",
        style = MaterialTheme.typography.titleSmall,
        textAlign = TextAlign.Center,
        overflow = TextOverflow.Ellipsis,
    )
}

@Preview(showBackground = true)
@Composable
fun GoalDetailsPreview() {
    GoalDetailsContent(
        uiState = GoalDetailsUiState(
            goal = Goal(
                bookName = "The Lord of the Rings",
                bookAuthor = "J.R.R.Tolkien",
                daysInProgress = 10,
                averageReadSpeed = 20,
                bookPublishYear = 1960,
                completedPagesAmount = 435,
                bookPagesAmount = 870,
                progress = 78,
                expectedPagesPerDay = 40,
                expectedFinishDaysAmount = 8
            )
        ),
        onIntent = {},
        onNavigate = {},
    )
}
