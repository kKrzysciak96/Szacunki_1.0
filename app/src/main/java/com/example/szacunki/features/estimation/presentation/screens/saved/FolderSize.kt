package com.example.szacunki.features.estimation.presentation.screens.saved

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.math.log10
import kotlin.math.pow


suspend fun calculateFolderSize(folderPath: String): Long =
    withContext(Dispatchers.IO) {
        val folder = File(folderPath)
        if (folder.exists() && folder.isDirectory) {
            calculateFolderSizeRecursive(folder)
        } else {
            0L
        }
    }

private fun calculateFolderSizeRecursive(folder: File): Long {
    var size = 0L
    folder.listFiles()?.forEach { file ->
        size += if (file.isFile) file.length() else calculateFolderSizeRecursive(file)
    }
    return size
}

fun formatSize(size: Long): String {
    if (size <= 0) {
        return "0 B"
    }
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
    return String.format("%.1f %s", size / 1024.0.pow(digitGroups.toDouble()), units[digitGroups])
}

fun deleteSavedPdfs(folder: File) {
    folder.listFiles()?.forEach { file ->
        file.delete()
    }
}