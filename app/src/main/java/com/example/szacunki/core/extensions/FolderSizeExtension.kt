package com.example.szacunki.core.extensions

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.math.log10
import kotlin.math.pow


suspend fun File.calculateFolderSize(): Long =
    withContext(Dispatchers.IO) {
        val folder = this@calculateFolderSize
        if (folder.exists() && folder.isDirectory) {
            folder.calculateFolderSizeRecursive()
        } else {
            0L
        }
    }

fun File.calculateFolderSizeRecursive(): Long {
    var size = 0L
    this.listFiles()?.forEach { file ->
        size += if (file.isFile) file.length() else this.calculateFolderSizeRecursive()
    }
    return size
}

fun Long.formatSize(): String {
    if (this <= 0) {
        return ""
    }
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    val digitGroups = (log10(this.toDouble()) / log10(1024.0)).toInt()
    return String.format("%.1f %s", this / 1024.0.pow(digitGroups.toDouble()), units[digitGroups])
}

fun File.deleteSavedPdfs() {
    listFiles()?.forEach { file ->
        file.delete()
    }
}