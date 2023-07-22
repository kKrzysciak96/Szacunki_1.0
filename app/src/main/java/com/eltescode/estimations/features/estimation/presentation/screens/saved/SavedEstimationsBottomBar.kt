package com.eltescode.estimations.features.estimation.presentation.screens.saved

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomAppBar
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eltescode.estimations.core.extensions.deleteSavedPdfs
import com.eltescode.estimations.ui.theme.color2
import com.eltescode.estimations.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@Composable
fun SavedEstimationsBottomBar(
    folderSize: String,
    calculateFolderSize: (Context) -> Unit,
    emitMessage: (String) -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    BottomAppBar(
        modifier = Modifier.fillMaxWidth(), backgroundColor = color2
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = {
                        scope.launch {
                            val folderPath =
                                context.filesDir.absolutePath + File.separator + "estimationPdf"
                            withContext(Dispatchers.IO) {
                                File(folderPath).deleteSavedPdfs()
                                emitMessage(context.getString(R.string.hint41))
                                calculateFolderSize(context)
                            }
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
                if (folderSize.isNotBlank()) {
                    Text(text = stringResource(R.string.hint20, folderSize))
                }
            }
        }
    }
}