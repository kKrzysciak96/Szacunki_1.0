package com.example.szacunki.features.estimation.presentation.screens.estimation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.szacunki.features.estimation.presentation.model.EstimationDisplayable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel
import java.util.*


@Destination(route = "EstimationScreen")
@Composable
fun EstimationScreen(
    id: UUID?,
    sectionNumber: String?,
    treeNames: Array<String>?,
    navigator: DestinationsNavigator
) {
    val currentIDState = rememberSaveable { mutableStateOf(id) }
    val treeIndexState = rememberSaveable { mutableStateOf(0) }
    val diameterIndexState = rememberSaveable { mutableStateOf(0) }
    val classesDialogState = rememberSaveable { mutableStateOf(false) }
    val treeNameState = rememberSaveable { mutableStateOf(false) }
    val memoState = rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    val viewModel = getViewModel<EstimationViewModel>()
    val estimation = viewModel.estimationFlow.collectAsState()


    LaunchedEffect(key1 = Unit) {
        currentIDState.value?.let {
            viewModel.onIdPassed(it)
        } ?: viewModel.createNewSheet(
            sectionNumber, treeNames?.toList()
        ).also { currentIDState.value = viewModel.estimationFlow.value?.id }
    }


    if (estimation.value != null) {
        Scaffold(
            topBar = {
                TreeNamesTopBar(
                    estimation = estimation as State<EstimationDisplayable>,
                    viewModel = viewModel,
                    treeIndexState = treeIndexState,
                    treeNameState = treeNameState
                )
            },
            bottomBar = {
                BottomInfoBar(
                    estimation = estimation as State<EstimationDisplayable>,
                    memoState = memoState,
                    context = context,
                    navigator = navigator
                )
            },
            content = {
                if (classesDialogState.value) {
                    AllTreeClassesDialog(
                        estimation = estimation as State<EstimationDisplayable>,
                        treeIndex = treeIndexState.value,
                        diameterIndex = diameterIndexState.value,
                        viewModel = viewModel,
                        onDismissRequest = { classesDialogState.value = false },
                    )
                }

                if (treeNameState.value) {
                    AddSingleTreeDialog(
                        viewModel = viewModel,
                        estimation = estimation as State<EstimationDisplayable>,
                        onDismissRequest = { treeNameState.value = false }
                    )
                }
                if (memoState.value) {
                    MemoDialog(
                        estimation = estimation as State<EstimationDisplayable>,
                        viewModel = viewModel
                    ) {
                        memoState.value = false
                    }
                }

                Column(modifier = Modifier.fillMaxSize()) {
                    TitleParametersRow(index = treeIndexState.value)
                    LazyColumnTreeParametersRow(
                        estimation = estimation as State<EstimationDisplayable>,
                        viewModel = viewModel,
                        treeIndex = treeIndexState,
                        diameterIndexState = diameterIndexState,
                        classesDialogState = classesDialogState
                    )
                }
                it
            }
        )
    }

}

















