package com.example.szacunki.core.screen.general

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.szacunki.destinations.MapScreenDestination
import com.example.szacunki.destinations.SavedEstimationsScreenDestination
import com.example.szacunki.destinations.SectionSelectionScreenDestination
import com.example.szacunki.features.map.presentation.screen.MapViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel

@RootNavGraph(start = true)
@Destination(route = "GeneralScreen")
@Composable
fun GeneralScreen(navigator: DestinationsNavigator) {
    val viewModel = getViewModel<MapViewModel>()
    val locationPermissionResultLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { isGranted ->
                if (isGranted[Manifest.permission.ACCESS_COARSE_LOCATION] == true && isGranted[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                    navigator.navigateToMapScreen()
                }
            })
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
            OutlinedButton(onClick = {
                locationPermissionResultLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                )
            }) {
                Text(text = "Pokaż mapę")
            }
            OutlinedButton(onClick = {
            }) {
                Text(text = "USTAWIENIA")
            }
        }
    }
}

fun DestinationsNavigator.navigateToMapScreen() {
    this.navigate(MapScreenDestination)
}