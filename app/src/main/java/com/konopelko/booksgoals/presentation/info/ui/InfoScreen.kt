package com.konopelko.booksgoals.presentation.info.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konopelko.booksgoals.presentation.common.theme.BookTrackerTheme
import com.konopelko.booksgoals.presentation.common.theme.Typography

@Composable
fun InfoScreen() = Column(
    modifier = Modifier
        .fillMaxSize()
        .verticalScroll(state = rememberScrollState())
) {
    Text(
        modifier = Modifier.padding(top = 16.dp),
        text = "Справочная система приложения",
        style = Typography.headlineLarge,
        textAlign = TextAlign.Center
    )

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "BooksTracker: ваш литературный спутник",
            style = Typography.titleMedium
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = "BooksTracker — это приложение, созданное помогать вам следить за вашими литературными достижениями, мотивируя вас на новые прочтения и помогая достичь ваших целей.",
            style = Typography.bodyMedium
        )

        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = "Начало работы с приложением",
            style = Typography.titleMedium
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = "При первом входе в приложение вы можете добавить книгу в список желаний либо создать цель.\n" +
                    "Для создания цели на главном экране приложения нажмите кнопку “Добавить цель”. После нажмите кнопку “Выбрать книгу” и выберите способ добавления книги в цель. \n" +
                    "\n" +
                    "При выборе пункта “Поиск новой книги”  вам становится доступен поиск книги (поиск книги происходит через онлайн библиотеку OpenLibrary, с данной библиотекой вы можете ознакомиться выбрав в меню пункт “Онлайн библиотека”). \n" +
                    "\n" +
                    "Если при поиске вы не нашли нужную вам книги вы можете нажать кнопку “Добавить новую книгу”. После заполнения всех необходимых полей вам будет доступно окно выбора количества страниц. \n" +
                    "\n" +
                    "В окне выбора количества страниц вы выбираете с помощью ползунка то количество страниц, которое вы планируете читать в день. \n" +
                    "\n" +
                    "После выбора нужного количества страниц нажмите кнопку “Создать цель”. \n" +
                    "\n" +
                    "Супер! Ваша первая цель готова!\n",
            style = Typography.bodyMedium
        )

        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = "Список желаний и список прочитанных книг",
            style = Typography.titleMedium
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = "Список желаний - это список книг, которые вы планируете прочитать в будущем. Для добавления книги в список желаний нажмите кнопку “+” в нижней правой части экрана. Процесс поиска книги такой же, как и при добавлении цели, отличие только в том, что не требуется выбирать количество читаемых страниц в день. \n" +
                    "После добавления книги в список желаний ее можно использовать при создании цели или начать прочтение книги прямо из списка желаний. Для этого необходимо нажать три точки возле книги и нажать “Начать”. \n" +
                    "\n" +
                    "Список прочитанных книг содержит завершенные цели (прочитанные книги)",
            style = Typography.bodyMedium
        )

        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = "Управление целями",
            style = Typography.titleMedium
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = "Для того, чтобы отметить выполнение нажмите на нужную цель, затем нажмите кнопку “Отметить выполнение” и введите количество прочитанных страниц. \n" +
                    "\n" +
                    "Для изменения цели (изменения количества читаемых страниц в день) нажмите кнопку “Изменить цель”, после чего выберите новое количество читаемых страниц. В окне целей при нажатии на три кнопки около нужной цели станут доступны три функции: заморозить, завершить, удалить.\n" +
                    "\n" +
                    "При заморозке цели неактивные дни не учитываются в статистике.\n" +
                    "При завершении цели книга считается прочитанной вне зависимости от количества прочитанных и отмеченых страниц. \n" +
                    "При удалении цели цель удаляется полностью, книга не считается прочитанной и не учитывается в статистике. ",
            style = Typography.bodyMedium
        )

        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = "Статистика",
            style = Typography.titleMedium
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = "В приложении существует два вида ститистики: общая и статистика книги.\n" +
                    "\n" +
                    "Для перехода к статистике книги нужно нажать на нужную цель и в окне цели нажать кнопку “Статистика книги”. В статистике можно увидеть график прочтения данной книги за текущую неделю, текущий месяц и год.\n" +
                    "\n" +
                    "В общей статистике также можно увидеть графики чтения за неделю месяц и год, однако здесь учитываются все книги. \n" +
                    "\n" +
                    "Общая статистика разделена на два раздела: “Книги” и “Страницы”. В разделе книг отображена статиска по книгам, а разделе страниц - по страницам. ",
            style = Typography.bodyMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun InfoScreenPreview() = BookTrackerTheme {
    InfoScreen()
}