package com.konopelko.booksgoals.presentation.addgoal.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konopelko.booksgoals.data.api.response.searchbooks.SearchBooksResponse.BookResponse
import com.konopelko.booksgoals.presentation.addgoal.AddGoalIntent
import com.konopelko.booksgoals.presentation.addgoal.AddGoalIntent.AddGoalNavigationIntent
import com.konopelko.booksgoals.presentation.addgoal.AddGoalIntent.AddGoalNavigationIntent.NavigateToGoalsScreen
import com.konopelko.booksgoals.presentation.addgoal.AddGoalIntent.AddGoalNavigationIntent.NavigateToSearchBooksScreen
import com.konopelko.booksgoals.presentation.addgoal.AddGoalIntent.OnArgsReceived
import com.konopelko.booksgoals.presentation.addgoal.AddGoalIntent.OnCreateGoalClicked
import com.konopelko.booksgoals.presentation.addgoal.AddGoalIntent.OnPagesPerDayChanged
import com.konopelko.booksgoals.presentation.addgoal.AddGoalUiState
import com.konopelko.booksgoals.presentation.addgoal.AddGoalViewModel
import com.konopelko.booksgoals.presentation.common.component.button.BaseButton
import com.konopelko.booksgoals.presentation.common.component.slider.BookPagesPerDaySlider
import com.konopelko.booksgoals.presentation.common.theme.BooksGoalsAppTheme
import com.konopelko.booksgoals.presentation.common.theme.Purple40
import com.konopelko.booksgoals.presentation.common.theme.Typography
import org.koin.androidx.compose.getViewModel

private const val PAGES_PER_DAY_GAP = 20

private val pagesPerDayRange = 20f..200f

//todo
// - add string res
// - use dimensions
@Composable
fun AddGoalScreen(
    viewModel: AddGoalViewModel = getViewModel(),
    onNavigate: (AddGoalNavigationIntent) -> Unit,
    args: BookResponse?
) {
    Log.e("HomeScreen", "args = $args")

    //todo: add key constant
    LaunchedEffect("args_key") {
        viewModel.acceptIntent(OnArgsReceived(args))
    }

    val uiState by viewModel.uiState.collectAsState()

    AddGoalContent(
        uiState = uiState,
        onIntent = viewModel::acceptIntent,
        onNavigate = onNavigate
    )
}

@Composable
private fun AddGoalContent(
    uiState: AddGoalUiState,
    modifier: Modifier = Modifier,
    onIntent: (AddGoalIntent) -> Unit = {},
    onNavigate: (AddGoalNavigationIntent) -> Unit = {}
) = Column(
    modifier = modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    val context = LocalContext.current

    with(uiState) {
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = "Adding new goal!",
            textAlign = TextAlign.Center,
            style = Typography.titleLarge
        )

        SelectBookContent(
            selectedBook = selectedBook,
            onNavigate = onNavigate
        )

        AnimatedVisibility(visible = selectedBook != null) {
            SelectPagerPerDayContent(
                daysToFinishGoal = daysToFinishGoal,
                onIntent = onIntent
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        BaseButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 48.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
            text = "Create Goal",
            isLoading = isSavingGoal,
            enabled = isAddGoalButtonEnabled,
            onClick = { onIntent(OnCreateGoalClicked) }
        )

        if(isGoalSaved) {
            LaunchedEffect("toast key") { //todo: move toast key to constant
                Toast.makeText(context, "Goal saved successfully!", Toast.LENGTH_SHORT).show()
                onNavigate(NavigateToGoalsScreen)
            }
        }
    }
}

@Composable
private fun SelectBookContent(
    selectedBook: BookResponse?,
    modifier: Modifier = Modifier,
    onNavigate: (AddGoalNavigationIntent) -> Unit = {}
) = Column(
    modifier = modifier.padding(
        top = 48.dp,
        start = 16.dp,
        end = 16.dp
    ),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Text(
        text = "Select a book from your wishlist or search a new one to use for the new goal"
    )

    if(selectedBook != null) {
        Text(
            modifier = Modifier.padding(vertical = 16.dp),
            text = selectedBook.title,
            color = Purple40
        )
    }

    BaseButton(
        modifier = Modifier.padding(top = 16.dp),
        text = "Select a book",
        onClick = { onNavigate(NavigateToSearchBooksScreen) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectPagerPerDayContent(
    daysToFinishGoal: Int,
    onIntent: (AddGoalIntent) -> Unit,
    modifier: Modifier = Modifier
) = Column(
    modifier = modifier.padding(
        top = 48.dp,
        start = 16.dp,
        end = 16.dp
    ),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    var sliderValue by remember { mutableFloatStateOf(20f) }

    Text(text = "Select pages per day you think you will read")

    BookPagesPerDaySlider(
        value = sliderValue,
        onValueChange = { newSliderValue ->
            sliderValue = newSliderValue
            onIntent(OnPagesPerDayChanged(newSliderValue.toInt()))
        },
        valueRange = pagesPerDayRange,
        gap = PAGES_PER_DAY_GAP,
        showIndicator = true,
    )

    Text(
        modifier = Modifier.padding(top = 16.dp),
        text = "If you read ${sliderValue.toInt()} pages per day you will finish book for $daysToFinishGoal days.",
        textAlign = TextAlign.Center,
        style = Typography.bodyMedium
    )
}

@Preview(showBackground = true)
@Composable
private fun AddGoalScreenNoSelectedBookPreview() = BooksGoalsAppTheme {
    AddGoalContent(
        uiState = AddGoalUiState()
    )
}

@Preview(showBackground = true)
@Composable
private fun AddGoalScreenBookSelectedPreview() = BooksGoalsAppTheme {
    AddGoalContent(
        uiState = AddGoalUiState(
            selectedBook = BookResponse(
                title = "New Interesting Book",
                authorName = listOf("Sashka"),
                publishYear = 1999,
                pagesAmount = 496
            )
        )
    )
}