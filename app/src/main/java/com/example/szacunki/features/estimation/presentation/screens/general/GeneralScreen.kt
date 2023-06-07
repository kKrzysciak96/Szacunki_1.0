package com.example.szacunki.features.estimation.presentation.screens.general

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.szacunki.features.estimation.presentation.screens.destinations.SavedEstimationsScreenDestination
import com.example.szacunki.features.estimation.presentation.screens.destinations.SectionSelectionScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph(start = true)
@Destination(route = "GeneralScreen")
@Composable
fun GeneralScreen(navigator: DestinationsNavigator) {
    Box(contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedButton(onClick = { navigator.navigate(SectionSelectionScreenDestination) }) {
                Text(text = "Utwórz nowy dokument")
            }
            OutlinedButton(onClick = { navigator.navigate(SavedEstimationsScreenDestination) }) {
                Text(text = "Zobacz Zapisane")
            }
        }
    }

}