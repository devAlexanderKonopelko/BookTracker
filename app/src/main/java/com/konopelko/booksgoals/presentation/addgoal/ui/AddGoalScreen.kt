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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konopelko.booksgoals.domain.model.book.Book
import com.konopelko.booksgoals.presentation.addgoal.AddGoalIntent
import com.konopelko.booksgoals.presentation.addgoal.AddGoalIntent.AddGoalNavigationIntent
import com.konopelko.booksgoals.presentation.addgoal.AddGoalIntent.AddGoalNavigationIntent.NavigateToGoalDetailsScreen
import com.konopelko.booksgoals.presentation.addgoal.AddGoalIntent.AddGoalNavigationIntent.NavigateToGoalsScreen
import com.konopelko.booksgoals.presentation.addgoal.AddGoalIntent.AddGoalNavigationIntent.NavigateToSearchBooksScreen
import com.konopelko.booksgoals.presentation.addgoal.AddGoalIntent.AddGoalNavigationIntent.NavigateToWishesScreen
import com.konopelko.booksgoals.presentation.addgoal.AddGoalIntent.OnArgsReceived
import com.konopelko.booksgoals.presentation.addgoal.AddGoalIntent.OnCreateGoalClicked
import com.konopelko.booksgoals.presentation.addgoal.AddGoalIntent.OnPagesPerDayChanged
import com.konopelko.booksgoals.presentation.addgoal.AddGoalUiState
import com.konopelko.booksgoals.presentation.addgoal.AddGoalViewModel
import com.konopelko.booksgoals.presentation.addgoal.model.AddGoalArgs
import com.konopelko.booksgoals.presentation.addgoal.model.AddGoalScreenOrigin
import com.konopelko.booksgoals.presentation.addgoal.model.AddGoalScreenOrigin.ADD_WISH_BOOK
import com.konopelko.booksgoals.presentation.addgoal.model.SelectBookOption.SEARCH_BOOK
import com.konopelko.booksgoals.presentation.addgoal.model.SelectBookOption.WISHLIST
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
    args: AddGoalArgs?
) {
    val showSelectBookOptions = remember { mutableStateOf(false) }

    args?.let {
        Log.e("AddGoalScreen", "args = $args")

        //todo: add key constant
        LaunchedEffect("args_key") {
            viewModel.acceptIntent(OnArgsReceived(args))
        }
    }


    val uiState by viewModel.uiState.collectAsState()

    AddGoalContent(
        uiState = uiState,
        onIntent = viewModel::acceptIntent,
        onNavigate = onNavigate,
        onSelectBookClicked = {
            showSelectBookOptions.value = true
        }
    )

    if (showSelectBookOptions.value) {
        SelectBookOptionsMenu(
            onOptionClicked = { selectBookOption ->
                when (selectBookOption) {
                    SEARCH_BOOK -> onNavigate(NavigateToSearchBooksScreen)
                    WISHLIST -> onNavigate(NavigateToWishesScreen)
                }
            },
            onDismiss = { showSelectBookOptions.value = false }
        )
    }

    handleNavigationActions(
        onNavigate = onNavigate,
        uiState = uiState
    )
}

@Composable
private fun handleNavigationActions(
    onNavigate: (AddGoalNavigationIntent) -> Unit,
    uiState: AddGoalUiState
) = LaunchedEffect(uiState.shouldNavigateToGoalDetailsScreen) {
    if (uiState.shouldNavigateToGoalDetailsScreen) {
        onNavigate(NavigateToGoalDetailsScreen)
    }
}

@Composable
private fun AddGoalContent(
    uiState: AddGoalUiState,
    modifier: Modifier = Modifier,
    onIntent: (AddGoalIntent) -> Unit = {},
    onNavigate: (AddGoalNavigationIntent) -> Unit = {},
    onSelectBookClicked: () -> Unit
) = Column(
    modifier = modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    val context = LocalContext.current

    with(uiState) {
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = "Добавляем новую цель!",
            textAlign = TextAlign.Center,
            style = Typography.titleLarge
        )

        SelectBookContent(
            selectedBook = selectedBook,
            isSelectBookButtonEnabled = uiState.isSelectBookButtonEnabled,
            onSelectBookClicked = onSelectBookClicked
        )

        AnimatedVisibility(visible = selectedBook.title.isNotEmpty()) {
            SelectPagerPerDayContent(
                daysToFinishGoal = daysToFinishGoal,
                selectedPagesPerDay = selectedPagesPerDay,
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
            text = "Создать цель",
            isLoading = isSavingGoal,
            enabled = isAddGoalButtonEnabled,
            onClick = { onIntent(OnCreateGoalClicked) }
        )

        if (isGoalSaved) {
            LaunchedEffect("toast key") { //todo: move toast key to constant
                Toast.makeText(context, "Цель успешно создана!", Toast.LENGTH_SHORT).show()
                onNavigate(NavigateToGoalsScreen)
            }
        }
    }
}

@Composable
private fun SelectBookContent(
    selectedBook: Book?,
    isSelectBookButtonEnabled: Boolean,
    onSelectBookClicked: () -> Unit,
    modifier: Modifier = Modifier,
) = Column(
    modifier = modifier.padding(
        top = 48.dp,
        start = 16.dp,
        end = 16.dp
    ),
    horizontalAlignment = Alignment.CenterHorizontally
) {

    Text(
        text = "Выберите книгу из вашего списка желаний либо воспользуйтесь поиском для выбора книги."
    )

    if (selectedBook != null) {
        Text(
            modifier = Modifier.padding(vertical = 16.dp),
            text = selectedBook.title,
            color = Purple40
        )
    }

    BaseButton(
        modifier = Modifier.padding(top = 16.dp),
        text = "Выбрать книгу",
        onClick = { onSelectBookClicked() },
        enabled = isSelectBookButtonEnabled
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectPagerPerDayContent(
    daysToFinishGoal: Int,
    selectedPagesPerDay: Int,
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
    var sliderValue by remember { mutableFloatStateOf(selectedPagesPerDay.toFloat()) }

    Text(text = "Выберите желаемое количество страниц в день")

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
        text = "Читая по ${sliderValue.toInt()} страниц в день Вы прочитаете книгу за $daysToFinishGoal дней.",
        textAlign = TextAlign.Center,
        style = Typography.bodyMedium
    )
}

@Preview(showBackground = true)
@Composable
private fun AddGoalScreenNoSelectedBookPreview() = BooksGoalsAppTheme {
    AddGoalContent(
        uiState = AddGoalUiState(),
        onSelectBookClicked = {}

    )
}

@Preview(showBackground = true)
@Composable
private fun AddGoalScreenBookSelectedPreview() = BooksGoalsAppTheme {
    AddGoalContent(
        uiState = AddGoalUiState(
            selectedBook = Book(
                title = "New Interesting Book",
                authorName = "Sashka",
                publishYear = "1999",
                pagesAmount = "496"
            )
        ),
        onSelectBookClicked = {}
    )
}