package com.example.szacunki.features.estimation.presentation.screens.estimation

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.szacunki.core.extensions.getBaseDiameterList
import com.example.szacunki.destinations.PdfViewerScreenDestination
import com.example.szacunki.features.estimation.presentation.model.EstimationDisplayable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import java.util.*


@Destination
@Composable
fun EstimationScreen(
    id: UUID?, sectionNumber: String?, treeNames: Array<String>?, navigator: DestinationsNavigator
) {
    val currentIDState = rememberSaveable { mutableStateOf(id) }
    val treeIndexState = rememberSaveable { mutableStateOf(0) }
    val diameterIndexState = rememberSaveable { mutableStateOf(0) }
    val classesDialogState = rememberSaveable { mutableStateOf(false) }
    val treeNameState = rememberSaveable { mutableStateOf(false) }
    val memoState = rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    val viewModel = getViewModel<EstimationViewModel>()
    val estimation = viewModel.estimation.collectAsState()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val keyToScrollTopAppBarAfterTreeIsAdded = rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(
        key1 = Unit,
        block = {
            currentIDState.value?.let {
                viewModel.onIdPassed(it)
            } ?: viewModel.createNewSheet(
                sectionNumber = sectionNumber,
                treeNameList = treeNames?.toList(),
                baseDiameterList = context.getBaseDiameterList()
            ).also { currentIDState.value = viewModel.estimation.value?.id }
        })

    LaunchedEffect(
        key1 = keyToScrollTopAppBarAfterTreeIsAdded.value,
        block = {
            if (keyToScrollTopAppBarAfterTreeIsAdded.value) {
                scope.launch { estimation.value?.trees?.let { listState.scrollToItem(index = it.size) } }
                keyToScrollTopAppBarAfterTreeIsAdded.value = false
            }
        })

    estimation.value?.let { estimationDisplayable ->

        EstimationScreen(
            context = context,
            treeIndexState = treeIndexState,
            diameterIndexState = diameterIndexState,
            classesDialogState = classesDialogState,
            treeNameState = treeNameState,
            memoState = memoState,
            estimation = estimationDisplayable,
            listState = listState,
            scope = scope,
            keyToScrollTopAppBarAfterTreeIsAdded = keyToScrollTopAppBarAfterTreeIsAdded,
            navigateToPdfViewerScreen = navigator::navigateToPdfViewerScreen,
            updateEstimation = viewModel::updateEstimationState

        )
    }
}

@Composable
private fun EstimationScreen(
    context: Context,
    treeIndexState: MutableState<Int>,
    diameterIndexState: MutableState<Int>,
    classesDialogState: MutableState<Boolean>,
    treeNameState: MutableState<Boolean>,
    memoState: MutableState<Boolean>,
    estimation: EstimationDisplayable,
    listState: LazyListState,
    scope: CoroutineScope,
    keyToScrollTopAppBarAfterTreeIsAdded: MutableState<Boolean>,
    navigateToPdfViewerScreen: (String) -> Unit,
    updateEstimation: (EstimationDisplayable) -> Unit

) {
    Scaffold(topBar = {

        TreeNamesTopBar(
            estimation = estimation,
            updateEstimation = updateEstimation,
            treeIndexState = treeIndexState,
            treeNameState = treeNameState,
            listState = listState,
            scope = scope
        )

    }, bottomBar = {
        BottomInfoBar(
            estimation = estimation,
            memoState = memoState,
            context = context,
            navigateToPdfViewerScreen = navigateToPdfViewerScreen,
            treeIndexState = treeIndexState,
            listState = listState,
            scope = scope
        )

    }, content = {
        ShowAllTreeClassesDialog(
            visibility = classesDialogState.value,
            estimation = estimation,
            treeIndex = treeIndexState.value,
            diameterIndex = diameterIndexState.value,
            updateEstimation = updateEstimation,
            onDismissRequest = { classesDialogState.value = false },
        )

        ShowAddSingleTreeDialog(
            visibility = treeNameState.value,
            context = context,
            updateEstimation = updateEstimation,
            estimation = estimation,
            treeIndexState = treeIndexState,

            onDismissRequest = {
                keyToScrollTopAppBarAfterTreeIsAdded.value = true
                treeNameState.value = false
            })

        ShowMemoDialog(
            visibility = memoState.value,
            estimation = estimation,
            updateEstimation = updateEstimation,
            onDismissRequest = { memoState.value = false }
        )

        ShowBaseContent(
            estimation = estimation,
            updateEstimation = updateEstimation,
            treeIndex = treeIndexState,
            diameterIndexState = diameterIndexState,
            classesDialogState = classesDialogState
        )
        it
    })
}

@Composable
private fun ShowAllTreeClassesDialog(
    visibility: Boolean,
    estimation: EstimationDisplayable,
    treeIndex: Int,
    diameterIndex: Int,
    updateEstimation: (EstimationDisplayable) -> Unit,
    onDismissRequest: () -> Unit
) {
    if (visibility) {
        AllTreeClassesDialog(
            estimation = estimation,
            treeIndex = treeIndex,
            diameterIndex = diameterIndex,
            updateEstimation = updateEstimation,
            onDismissRequest = onDismissRequest,
        )
    }
}

@Composable
private fun ShowAddSingleTreeDialog(
    visibility: Boolean,
    context: Context,
    estimation: EstimationDisplayable,
    treeIndexState: MutableState<Int>,
    updateEstimation: (EstimationDisplayable) -> Unit,
    onDismissRequest: () -> Unit
) {
    if (visibility) {
        AddSingleTreeDialog(
            context = context,
            updateEstimation = updateEstimation,
            estimation = estimation,
            treeIndexState = treeIndexState,
            onDismissRequest = onDismissRequest
        )

    }
}

@Composable
private fun ShowMemoDialog(
    visibility: Boolean,
    estimation: EstimationDisplayable,
    updateEstimation: (EstimationDisplayable) -> Unit,
    onDismissRequest: () -> Unit
) {
    if (visibility) {
        MemoDialog(
            estimation = estimation,
            updateEstimation = updateEstimation,
            onDismissRequest = onDismissRequest
        )
    }
}

@Composable
private fun ShowBaseContent(
    estimation: EstimationDisplayable,
    updateEstimation: (EstimationDisplayable) -> Unit,
    treeIndex: MutableState<Int>,
    diameterIndexState: MutableState<Int>,
    classesDialogState: MutableState<Boolean>
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TitleParametersRow()
        ScrollableColumnTreeParametersRow(
            estimation = estimation,
            updateEstimation = updateEstimation,
            treeIndex = treeIndex,
            diameterIndexState = diameterIndexState,
            classesDialogState = classesDialogState
        )
    }
}

private fun DestinationsNavigator.navigateToPdfViewerScreen(path: String) {
    navigate(PdfViewerScreenDestination(path))
}










