package com.konopelko.booksgoals.presentation.common.utils.date

import android.icu.util.Calendar
import android.util.Log
import com.konopelko.booksgoals.presentation.goalstatistics.model.StatisticsScale
import com.konopelko.booksgoals.presentation.goalstatistics.model.StatisticsScale.MONTH
import com.konopelko.booksgoals.presentation.goalstatistics.model.StatisticsScale.WEEK
import com.konopelko.booksgoals.presentation.goalstatistics.model.StatisticsScale.YEAR
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.temporal.TemporalAdjusters
import java.util.Locale

fun String.isDateInScaleRange(scale: StatisticsScale): Boolean = when (scale) {
    WEEK -> this.isInWeekScale()
    MONTH -> this.isInMonthScale()
    YEAR -> this.isInYearScale()
}

private fun String.isInWeekScale(): Boolean {
    val today = LocalDate.now()
    val monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    val sunday = monday.plusDays(6)

    // 00:00 of monday
    val mondayMillis = monday.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

    // 00:00 of sunday + 23h * 60m * 60s * 1000ms
    val sundayMillis = sunday.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli() + 23 * 60 * 60 * 1000

    val progressMarkMillis = SimpleDateFormat(
        "yyyy/MM/dd HH:mm:ss",
        Locale.getDefault()
    ).parse(this)
        ?.toInstant()
        ?.toEpochMilli()
        ?: throw Exception("Failed to parse mark date. date: $this")


    Log.e("DateUtils", "mondayMillis, $mondayMillis")
    Log.e("DateUtils", "sundayMillis, $sundayMillis")
    Log.e("DateUtils", "progressMarkMillis, $progressMarkMillis")

    Log.e("DateUtils", "isInWeekScale: ${progressMarkMillis in mondayMillis..sundayMillis}")


    return progressMarkMillis in mondayMillis..sundayMillis
}

private fun String.isInMonthScale(): Boolean {
    val monthDays = Calendar.getInstance().getActualMaximum(java.util.Calendar.DAY_OF_MONTH)
    val today = LocalDate.now()
    val monthFirstDay = today.with(TemporalAdjusters.firstDayOfMonth())
    val monthLastDay = monthFirstDay.plusDays((monthDays - 1).toLong())

    val monthFirstDayMillis =
        monthFirstDay.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
    val monthLastDayMillis =
        monthLastDay.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
    val progressMarkMillis = SimpleDateFormat(
        "yyyy/MM/dd HH:mm:ss",
        Locale.getDefault()
    ).parse(this)
        ?.toInstant()
        ?.toEpochMilli()
        ?: throw Exception("Failed to parse mark date. date: $this")

    /*
    Log.e("DateUtils", "monthFirstDayMillis, $monthFirstDayMillis")
    Log.e("DateUtils", "monthLastDayMillis, $monthLastDayMillis")
    Log.e("DateUtils", "progressMarkMillis, $progressMarkMillis")
     */
    Log.e("DateUtils", "isInMonthScale: ${progressMarkMillis in monthFirstDayMillis..monthLastDayMillis}")


    return progressMarkMillis in monthFirstDayMillis..monthLastDayMillis
}

private fun String.isInYearScale(): Boolean {
    val today = LocalDate.now()
    val yearFirstDay = today.with(TemporalAdjusters.firstDayOfYear())
    val yearLastDay = yearFirstDay.plusDays(364)

    val yearFirstDayMillis =
        yearFirstDay.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
    val yearLastDayMillis = yearLastDay.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
    val progressMarkMillis = SimpleDateFormat(
        "yyyy/MM/dd HH:mm:ss",
        Locale.getDefault()
    ).parse(this)
        ?.toInstant()
        ?.toEpochMilli()
        ?: throw Exception("Failed to parse mark date. date: $this")

    /*
    Log.e("DateUtils", "yearFirstDayMillis, $yearFirstDayMillis")
    Log.e("DateUtils", "yearLastDayMillis, $yearLastDayMillis")
    Log.e("DateUtils", "progressMarkMillis, $progressMarkMillis")
     */
    Log.e("DateUtils", "isInYearScale: ${progressMarkMillis in yearFirstDayMillis..yearLastDayMillis}")

    return progressMarkMillis in yearFirstDayMillis..yearLastDayMillis
}