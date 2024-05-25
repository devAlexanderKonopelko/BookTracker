package com.konopelko.booksgoals.domain.model.progress

import android.icu.util.Calendar
import com.konopelko.booksgoals.domain.model.progress.WeekDay.FRIDAY
import com.konopelko.booksgoals.domain.model.progress.WeekDay.MONDAY
import com.konopelko.booksgoals.domain.model.progress.WeekDay.SATURDAY
import com.konopelko.booksgoals.domain.model.progress.WeekDay.SUNDAY
import com.konopelko.booksgoals.domain.model.progress.WeekDay.THURSDAY
import com.konopelko.booksgoals.domain.model.progress.WeekDay.TUESDAY
import com.konopelko.booksgoals.domain.model.progress.WeekDay.WEDNESDAY

enum class WeekDay {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY
}

fun Int.toWeekDay(): WeekDay = when(this) {
    Calendar.MONDAY -> MONDAY
    Calendar.TUESDAY -> TUESDAY
    Calendar.WEDNESDAY -> WEDNESDAY
    Calendar.THURSDAY -> THURSDAY
    Calendar.FRIDAY -> FRIDAY
    Calendar.SATURDAY -> SATURDAY
    Calendar.SUNDAY -> SUNDAY
    else -> throw Exception("Invalid week day")
}