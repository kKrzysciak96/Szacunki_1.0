package com.example.szacunki.core.extensions

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

fun Date.toLocalDate(): LocalDate = this.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

fun Date.toLocalDateTime(): LocalDateTime =
    this.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()

fun LocalDateTime.prepareDateToDisplay(): String {
    val year = this.year.toString()
    val month = this.monthValue.toString()
    val day = this.dayOfMonth.toString()
    val hours = this.hour.toString()
    val minutes = this.minute.toString()

    return "$year/$month/$day $hours:$minutes"
}