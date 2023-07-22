package com.eltescode.estimations.features.estimation.presentation.screens.saved

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.eltescode.estimations.core.extensions.prepareDateToDisplay
import com.eltescode.estimations.core.extensions.shareFile
import com.eltescode.estimations.core.extensions.toLocalDateTime
import com.eltescode.estimations.core.extensions.trimToDisplaySectionNumber
import com.eltescode.estimations.destinations.EstimationScreenDestination
import com.eltescode.estimations.destinations.PdfViewerScreenDestination
import com.eltescode.estimations.features.estimation.presentation.model.EstimationDisplayable
import com.eltescode.estimations.features.pdf.creator.PdfGenerator.generatePdf
import com.eltescode.estimations.ui.theme.color3
import com.eltescode.estimations.R
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel
import java.io.File
import java.util.*

@Destination
@Composable
fun SavedEstimationsScreen(
    navigator: DestinationsNavigator, viewModel: SavedEstimationsViewModel = koinViewModel()
) {
    val estimations = viewModel.estimations.collectAsState(emptyList())
    val folderSize = viewModel.folderSize.collectAsState()
    val deleteDialogVisible = rememberSaveable { mutableStateOf(false) }
    val estimationToDelete = viewModel.estimationToDelete.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit, block = {
        viewModel.message.collect { message ->
            Toast.makeText(
                context,
                message,
                Toast.LENGTH_LONG
            ).show()
        }
    })
    LaunchedEffect(key1 = Unit, block = {
        viewModel.calculateFolderSize(context)
    })

    SavedEstimationsScreen(
        folderSize = folderSize.value,
        estimations = estimations.value,
        deleteDialogVisible = deleteDialogVisible,
        estimationToDelete = estimationToDelete.value,
        updateEstimation = viewModel::updateEstimation,
        deleteEstimation = viewModel::deleteEstimation,
        emitMessage = viewModel::emitMessage,
        calculateFolderSize = viewModel::calculateFolderSize,
        navigateToEstimationsScreen = navigator::navigateToEstimationsScreen,
        navigateToPdfViewerScreen = navigator::navigateToPdfViewerScreen
    )
}

@Composable
private fun SavedEstimationsScreen(
    folderSize: String,
    estimations: List<EstimationDisplayable>,
    deleteDialogVisible: MutableState<Boolean>,
    estimationToDelete: EstimationDisplayable?,
    updateEstimation: (EstimationDisplayable) -> Unit,
    deleteEstimation: (EstimationDisplayable) -> Unit,
    emitMessage: (String) -> Unit,
    calculateFolderSize: (Context) -> Unit,
    navigateToEstimationsScreen: (UUID?) -> Unit,
    navigateToPdfViewerScreen: (String) -> Unit
) {
    key(deleteDialogVisible.value) {
        Scaffold(
            topBar = { SavedEstimationsTopBar() },
            bottomBar = {
                SavedEstimationsBottomBar(
                    folderSize = folderSize,
                    emitMessage = emitMessage,
                    calculateFolderSize = calculateFolderSize
                )
            },
            content = {
                estimationToDelete?.let { estimation ->
                    if (deleteDialogVisible.value) {
                        DeleteDialog(
                            estimation = estimation,
                            deleteEstimation = deleteEstimation,
                            onDismiss = { deleteDialogVisible.value = false })
                    }
                }
                AllSavedEstimationsContent(
                    estimations = estimations,
                    navigateToEstimationsScreen = navigateToEstimationsScreen,
                    navigateToPdfViewerScreen = navigateToPdfViewerScreen,
                    calculateFolderSize = calculateFolderSize,
                    onSwipeToDismiss = { estimation ->
                        updateEstimation(estimation)
                        deleteDialogVisible.value = true
                    })
                it
            })
    }
}

@Composable
fun AllSavedEstimationsContent(
    estimations: List<EstimationDisplayable>,
    navigateToEstimationsScreen: (UUID?) -> Unit,
    navigateToPdfViewerScreen: (String) -> Unit,
    calculateFolderSize: (Context) -> Unit,
    onSwipeToDismiss: (EstimationDisplayable) -> Unit,
) {
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 50.dp)
    ) {
        items(
            items = estimations.sortedByDescending { it.date.time },
            key = { it.id }) { estimation ->
            EstimationRow(
                context = context,
                estimation = estimation,
                navigateToEstimationsScreen = navigateToEstimationsScreen,
                navigateToPdfViewerScreen = navigateToPdfViewerScreen,
                calculateFolderSize = calculateFolderSize,
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
    navigateToEstimationsScreen: (UUID?) -> Unit,
    navigateToPdfViewerScreen: (String) -> Unit,
    calculateFolderSize: (Context) -> Unit,
    onSwipeToDismiss: (EstimationDisplayable) -> Unit
) {
    val dismissState = rememberDismissState(confirmStateChange = {
        if (it == DismissValue.DismissedToStart) {
            onSwipeToDismiss(estimation)
        }
        true
    })
    SwipeToDismiss(
        state = dismissState,
        background = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 100.dp, end = 20.dp, top = 20.dp, bottom = 10.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                BackgroundSwipeToDismissIcon()
            }
        },
        dismissThresholds = { FractionalThreshold(0.75F) },
        directions = setOf(DismissDirection.EndToStart)
    ) {
        Card(
            modifier = Modifier.padding(10.dp),
            backgroundColor = color3,
            elevation = 10.dp,
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, Color.Black)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navigateToEstimationsScreen(estimation.id) },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                EstimationDetailsView(
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
                    SendIcon(
                        modifier = Modifier.weight(0.5F),
                        context = context,
                        estimation = estimation,
                        calculateFolderSize = calculateFolderSize
                    )
                    ShowPdfIcon(
                        modifier = Modifier.weight(0.5F),
                        context = context,
                        estimation = estimation,
                        navigateToPdfViewerScreen = navigateToPdfViewerScreen
                    )
                }
            }
        }
    }
}

@Composable
fun BackgroundSwipeToDismissIcon() {
    Icon(
        painter = painterResource(id = R.drawable.ic_delete),
        contentDescription = null,
        modifier = Modifier
            .size(60.dp)
            .padding(end = 10.dp),
        tint = Color.Red
    )
}

@Composable
fun SendIcon(
    modifier: Modifier,
    context: Context,
    estimation: EstimationDisplayable,
    calculateFolderSize: (Context) -> Unit,
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_send),
        contentDescription = null,
        modifier = modifier
            .size(50.dp)
            .padding(end = 10.dp)
            .clickable {
                val path = generatePdf(context = context, estimation = estimation)
                val file = File(path)
                val uri = FileProvider.getUriForFile(
                    context, "com.eltescode.estimations.fileprovider", file
                )
                context.shareFile(uri)
                calculateFolderSize(context)
            })
}

@Composable
fun ShowPdfIcon(
    modifier: Modifier,
    context: Context,
    estimation: EstimationDisplayable,
    navigateToPdfViewerScreen: (String) -> Unit
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_pdf),
        contentDescription = null,
        modifier = modifier
            .size(50.dp)
            .padding(end = 10.dp)
            .clickable {
                val path = generatePdf(context, estimation)
                navigateToPdfViewerScreen(path)
            })
}

@Composable
fun EstimationDetailsView(name: String, date: Date, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier, verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.hint6, name),
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

private fun DestinationsNavigator.navigateToEstimationsScreen(id: UUID?) {
    navigate(EstimationScreenDestination(id = id, sectionNumber = null, treeNames = null))
}

private fun DestinationsNavigator.navigateToPdfViewerScreen(path: String) {
    navigate(PdfViewerScreenDestination(path))
}
