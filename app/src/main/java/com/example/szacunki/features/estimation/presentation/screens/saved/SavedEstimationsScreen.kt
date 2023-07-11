package com.example.szacunki.features.estimation.presentation.screens.saved

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.example.szacunki.R
import com.example.szacunki.core.calculations.color1
import com.example.szacunki.core.calculations.color2
import com.example.szacunki.core.extensions.prepareDateToDisplay
import com.example.szacunki.core.extensions.toLocalDateTime
import com.example.szacunki.core.extensions.trimToDisplaySectionNumber
import com.example.szacunki.destinations.EstimationScreenDestination
import com.example.szacunki.destinations.PdfViewerScreenDestination
import com.example.szacunki.features.estimation.presentation.model.EstimationDisplayable
import com.example.szacunki.features.pdf.creator.PdfGenerator
import com.example.szacunki.features.pdf.creator.PdfGenerator.generatePdf
import com.example.szacunki.features.pdf.viewer.share
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.getViewModel
import java.io.File
import java.util.*

@Destination
@Composable
fun SavedEstimationsScreen(navigator: DestinationsNavigator) {
    val viewModel = getViewModel<SavedEstimationsViewModel>()
    val estimations = viewModel.estimations.collectAsState(emptyList())
    val deleteDialogVisible = rememberSaveable { mutableStateOf(false) }
    val estimationToDelete = viewModel.estimationFlow.collectAsState()

    key(deleteDialogVisible.value) {
        Scaffold(
            topBar = { SavedEstimationsTopBar() },
            bottomBar = { SavedEstimationsBottomBar(viewModel = viewModel) },
            content = { padding ->
                estimationToDelete.value?.let {
                    if (deleteDialogVisible.value) {
                        DeleteDialog(
                            estimation = it,
                            viewModel = viewModel,
                            onDismiss = {
                                deleteDialogVisible.value = false

                            })
                    }
                }
                AllSavedEstimationsContent(
                    estimations = estimations,
                    navigator = navigator,
                    onSwipeToDismiss = {
                        viewModel.updateEstimationFlow(it)
                        deleteDialogVisible.value = true
                    })
                padding
            })
    }
}

@Composable
fun SavedEstimationsTopBar() {
    TopAppBar(modifier = Modifier.fillMaxWidth(), backgroundColor = color2) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "Zapisane Szacunki Brakarskie",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h5
            )
        }

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AllSavedEstimationsContent(
    estimations: State<List<EstimationDisplayable>>,
    navigator: DestinationsNavigator,
    onSwipeToDismiss: (EstimationDisplayable) -> Unit,
) {

    val context = LocalContext.current

    LazyColumn(
        reverseLayout = false,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 50.dp)
    ) {

        items(
            items = estimations.value.sortedByDescending { it.date.time },
            key = { it.id }) { estimation ->

            EstimationRow(
                context = context,
                estimation = estimation,
                navigator = navigator,
                onSwipeToDismiss = onSwipeToDismiss
            )


        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EstimationRow(
    context: Context,
    estimation: EstimationDisplayable,
    navigator: DestinationsNavigator,
    onSwipeToDismiss: (EstimationDisplayable) -> Unit
) {
    val dismissState = rememberDismissState(confirmStateChange = {
        if (it == DismissValue.DismissedToStart) {
            onSwipeToDismiss(estimation)
        }
        true
    })
    dismissState.currentValue
    SwipeToDismiss(
        state = dismissState,
        background = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 100.dp, end = 20.dp, top = 20.dp, bottom = 10.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .padding(end = 10.dp),
                    tint = Color.Red
                )
            }
        },
        dismissThresholds = { FractionalThreshold(0.75F) },
        directions = setOf(DismissDirection.EndToStart)
    ) {

        Card(
            modifier = Modifier
                .padding(10.dp),
            backgroundColor = color1,
            elevation = 5.dp,
            shape = RoundedCornerShape(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val id = estimation.id
                        val navArg = EstimationScreenDestination.NavArgs(
                            id = id,
                            sectionNumber = null,
                            treeNames = null
                        )
                        navigator.navigate(EstimationScreenDestination(navArg))
//                        val navArg = EstimationScreenScrollDestination.NavArgs(
//                            id = id,
//                            sectionNumber = null,
//                            treeNames = null
//                        )
//                        navigator.navigate(EstimationScreenScrollDestination(navArg))

                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                EstimationView(
                    name = estimation.sectionNumber.trimToDisplaySectionNumber(),
                    date = estimation.date,
                    modifier = Modifier.weight(0.7F)
                )
                Row(
                    modifier = Modifier
                        .weight(0.3F)
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_send),
                        contentDescription = null,
                        modifier = Modifier
                            .weight(0.5F)
                            .size(50.dp)
                            .padding(end = 10.dp)
                            .clickable {
                                val path =
                                    generatePdf(context = context, estimation = estimation)
                                val file = File(path)
                                val uri = FileProvider.getUriForFile(
                                    context,
                                    "com.example.szacunki.fileprovider",
                                    file
                                )
                                share(uri, context)
                            }
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_pdf),
                        contentDescription = null,
                        modifier = Modifier
                            .weight(0.5F)
                            .size(50.dp)
                            .padding(end = 10.dp)
                            .clickable {
                                val path = PdfGenerator.generatePdf(context, estimation)
                                val navArg = PdfViewerScreenDestination.NavArgs(path = path)
                                navigator.navigate(
                                    com.example.szacunki.destinations.PdfViewerScreenDestination(
                                        navArg
                                    )
                                )
                            }
                    )
                }
            }
        }
    }
}

@Composable
fun EstimationView(name: String, date: Date, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier, verticalArrangement = Arrangement.Center
    ) {
        val title = "Oddział nr.: $name"
        Text(
            text = title,
            modifier = Modifier.padding(5.dp),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.h5,
            maxLines = 1
        )
        Text(
            text = date.toLocalDateTime().prepareDateToDisplay(),
            modifier = Modifier.padding(5.dp),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.h6,
            fontStyle = FontStyle.Italic,
            color = Color.DarkGray
        )
    }

}

@Composable
fun SavedEstimationsBottomBar(viewModel: SavedEstimationsViewModel) {
    var folderSize by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth(), backgroundColor = color2
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            OutlinedButton(
                onClick = {
                    scope.launch {
                        val folderPath =
                            context.filesDir.absolutePath + File.separator + "estimationPdf"
                        val size = withContext(Dispatchers.IO) {
                            calculateFolderSize(folderPath, context)
                        }
                        folderSize = formatSize(size)
                        withContext(Dispatchers.IO) {
                            deleteSavedPdfs(File(folderPath))
                        }
                        Toast
                            .makeText(
                                context,
                                "Zwolniono $folderSize pamięci",
                                Toast.LENGTH_LONG
                            )
                            .show()
                    }
                },
                modifier = Modifier
                    .fillMaxHeight()
                    .width(200.dp)
                    .padding(top = 10.dp, bottom = 10.dp)

            ) {
                Text(
                    text = "Usuń stare Pdf-y",
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }
        }
    }
}



