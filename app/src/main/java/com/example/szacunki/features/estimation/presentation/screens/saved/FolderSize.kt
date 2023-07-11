package com.example.szacunki.features.estimation.presentation.screens.saved

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.File.separator
import kotlin.math.log10
import kotlin.math.pow

@Destination(route = "FolderSizeCalculatorScreen")
@Composable
fun FolderSizeCalculatorScreen() {
    var folderSize by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = context.filesDir.absolutePath)
        Text(text = context.filesDir.path)
        context.filesDir.parentFile?.let { Text(text = it.path) }
        Button(
            onClick = {
                scope.launch {
                    val size = withContext(Dispatchers.IO) {
                        calculateFolderSize(
                            context.filesDir.absolutePath + separator + "estimationPdf",
                            context
                        )
                    }
                    folderSize = formatSize(size)
                }
            }
        ) {
            Text("Calculate Folder Size")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Folder Size: $folderSize")
    }
}

suspend fun calculateFolderSize(folderPath: String, context: Context): Long =
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

fun deleteSavedPdfs(folder: File) {
    folder.listFiles()?.forEach { file ->
        file.delete()
    }
}

fun formatSize(size: Long): String {
    if (size <= 0) {
        return "0 B"
    }
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
    return String.format("%.1f %s", size / 1024.0.pow(digitGroups.toDouble()), units[digitGroups])
}