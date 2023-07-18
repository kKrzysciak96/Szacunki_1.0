package com.example.szacunki.features.estimation.presentation.screens.saved

import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomAppBar
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.szacunki.R
import com.example.szacunki.core.extensions.showLongHint
import com.example.szacunki.ui.theme.color2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@Composable
fun SavedEstimationsBottomBar() {
    var folderSize by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    BottomAppBar(
        modifier = Modifier.fillMaxWidth(), backgroundColor = color2
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            OutlinedButton(
                onClick = {
                    scope.launch {
                        val folderPath =
                            context.filesDir.absolutePath + File.separator + "estimationPdf"
                        val size = withContext(Dispatchers.IO) {
                            calculateFolderSize(folderPath)
                        }
                        folderSize = formatSize(size)
                        withContext(Dispatchers.IO) {
                            deleteSavedPdfs(File(folderPath))
                        }
                        context.showLongHint(R.string.hint20, folderSize)
                    }
                },
                modifier = Modifier
                    .fillMaxHeight()
                    .width(200.dp)
                    .padding(top = 10.dp, bottom = 10.dp)

            ) {
                Text(
                    text = stringResource(id = R.string.hint21),
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }
        }
    }
}