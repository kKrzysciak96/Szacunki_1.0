package com.eltescode.estimations.core.extensions

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
    val minutes = if (this.minute.toString().length == 1) {
        "0" + this.minute.toString()
    } else {
        this.minute.toString()
    }
    return "$year/$month/$day $hours:$minutes"
}

fun LocalDateTime.prepareDateToSave(): String {
    val year = this.year.toString()
    val month = this.monthValue.toString()
    val day = this.dayOfMonth.toString()
    val hours = this.hour.toString()
    val minutes = this.minute.toString()
    val floor = "_"
    val colon = ":"
    return year + floor + month + floor + day + floor + hours + colon + minutes
}