package com.example.szacunki.features.estimation.presentation.screens.saved

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.szacunki.features.estimation.presentation.screens.destinations.EstimationScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel

@Destination
@Composable
fun SavedEstimationsScreen(navigator: DestinationsNavigator) {
    val viewModel = getViewModel<SavedEstimationsViewModel>()
    val estimations = viewModel.estimations.collectAsState(emptyList())

    LazyColumn() {
        items(estimations.value) {
            Card(modifier = Modifier.clickable {
                val id = it.id
                val navArg = EstimationScreenDestination.NavArgs(id = id)
                navigator.navigate(EstimationScreenDestination(navArg))

            }) {
                Text(text = it.sectionNumber + " " + it.date.time.toString())

            }
        }
        item {
            Button(onClick = { viewModel.dropDataBase() }) {
                Text(text = "Drop DataBase")
            }
        }
    }

}