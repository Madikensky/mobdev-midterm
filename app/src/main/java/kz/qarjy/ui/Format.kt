package kz.qarjy.ui

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/** 12450 -> "12 450 ₸" (manual grouping, so the result does not depend on the device locale). */
fun formatKzt(amount: Int): String =
    amount.toString().reversed().chunked(3).joinToString(" ").reversed() + " ₸"

private val dateFormatter = DateTimeFormatter.ofPattern("MMM d", Locale.ENGLISH)

fun formatDate(date: LocalDate): String = date.format(dateFormatter)
