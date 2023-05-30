package com.example.szacunki.features.estimation.presentation.screens.general

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.szacunki.features.estimation.presentation.screens.destinations.EstimationScreenDestination
import com.example.szacunki.features.estimation.presentation.screens.destinations.SavedEstimationsScreenDestination
import com.example.szacunki.features.estimation.presentation.screens.estimation.EstimationViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel

@RootNavGraph(start = true)
@Destination
@Composable
fun GeneralScreen(navigator: DestinationsNavigator) {
    val viewModel = getViewModel<EstimationViewModel>()
    val text = remember {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedButton(onClick = {
            val navArg = EstimationScreenDestination.NavArgs(null)
            navigator.navigate(EstimationScreenDestination(navArg))
        }) {
            Text(text = "Utwórz nowy dokument")
        }
        OutlinedButton(onClick = { navigator.navigate(SavedEstimationsScreenDestination) }) {
            Text(text = "Zobacz Zapisane")
        }
        OutlinedButton(onClick = { viewModel.getSingleEstimation(9) }) {

        }

    }
}