package com.expensetracker.app.util

import java.text.NumberFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

private val thaiLocale = Locale("th", "TH")
private val amountFormat: NumberFormat = NumberFormat.getNumberInstance(Locale.US).apply {
    minimumFractionDigits = 2
    maximumFractionDigits = 2
}

fun formatAmount(value: Double): String = amountFormat.format(value)

fun formatAmountWithSign(value: Double, isIncome: Boolean): String {
    val sign = if (isIncome) "+" else "-"
    return "$sign${formatAmount(kotlin.math.abs(value))}"
}

fun formatDateThaiShort(date: LocalDate): String {
    // e.g. "15 พ.ค. 67"
    val day = date.dayOfMonth
    val month = thaiMonthAbbrev(date.monthValue)
    val yearBE = (date.year + 543) % 100
    return "$day $month $yearBE"
}

fun formatDateThaiFull(date: LocalDate): String {
    // e.g. "15 พฤษภาคม 2567"
    val day = date.dayOfMonth
    val month = thaiMonthFull(date.monthValue)
    val yearBE = date.year + 543
    return "$day $month $yearBE"
}

fun formatMonthYearThai(date: LocalDate): String {
    // e.g. "พฤษภาคม 2567"
    return "${thaiMonthFull(date.monthValue)} ${date.year + 543}"
}

fun formatTime(minuteOfDay: Int): String {
    val time = LocalTime.of(minuteOfDay / 60, minuteOfDay % 60)
    return time.format(DateTimeFormatter.ofPattern("HH:mm"))
}

private val thaiMonthsFull = listOf(
    "มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน", "พฤษภาคม", "มิถุนายน",
    "กรกฎาคม", "สิงหาคม", "กันยายน", "ตุลาคม", "พฤศจิกายน", "ธันวาคม"
)
private val thaiMonthsAbbrev = listOf(
    "ม.ค.", "ก.พ.", "มี.ค.", "เม.ย.", "พ.ค.", "มิ.ย.",
    "ก.ค.", "ส.ค.", "ก.ย.", "ต.ค.", "พ.ย.", "ธ.ค."
)

fun thaiMonthFull(month: Int) = thaiMonthsFull[month - 1]
fun thaiMonthAbbrev(month: Int) = thaiMonthsAbbrev[month - 1]

fun thaiWeekdayAbbrev(dayOfWeekValue: Int): String {
    // dayOfWeekValue: 1(Mon)..7(Sun) per java.time.DayOfWeek
    val names = listOf("จ", "อ", "พ", "พฤ", "ศ", "ส", "อา")
    return names[dayOfWeekValue - 1]
}
