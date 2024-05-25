package com.konopelko.booksgoals.presentation.totalstatistics.model

import com.konopelko.booksgoals.presentation.goalstatistics.model.StatisticsScale

data class TotalStatisticsData(
    val statisticsTab: TotalStatisticsTab = TotalStatisticsTab.PAGES,
    val statisticsScale: StatisticsScale = StatisticsScale.WEEK,
    val totalUnitsRead: Int = 0,
    val averageReadSpeed: Int = 0,
    val goalsAchieved: Int = 0
)
