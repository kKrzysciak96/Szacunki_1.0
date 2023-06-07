package com.example.szacunki.core.extensions

fun String.trimToDisplay() = if (this.length > 12) {
    this.substring(0..11) + "."
} else {
    this
}