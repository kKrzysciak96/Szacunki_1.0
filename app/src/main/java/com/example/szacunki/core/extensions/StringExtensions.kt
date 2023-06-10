package com.example.szacunki.core.extensions

fun String.trimToDisplay() = if (this.length > 12) {
    this.substring(0..11) + "."
} else {
    this
}

fun String.trimToDisplaySectionNumber() = if (this.length > 7) {
    this.substring(0..6) + "."
} else {
    this
}