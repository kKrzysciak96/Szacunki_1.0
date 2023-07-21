package com.example.szacunki.core.screen.general

import android.Manifest
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.szacunki.core.extensions.gradientBackground
import com.example.szacunki.destinations.MapScreenDestination
import com.example.szacunki.destinations.SavedEstimationsScreenDestination
import com.example.szacunki.destinations.SectionSelectionScreenDestination
import com.example.szacunki.ui.theme.brushList1
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph(start = true)
@Destination
@Composable
fun GeneralScreen(navigator: DestinationsNavigator) {
    val context = LocalContext.current
    GeneralScreen(
        context = context,
        navigateToMapScreen = navigator::navigateToMapScreen,
        navigateToSectionSelectionScreen = navigator::navigateToSectionSelectionScreen,
        navigateToSavedEstimationsScreen = navigator::navigateToSavedEstimationsScreen
    )
}

@Composable
private fun GeneralScreen(
    context: Context,
    navigateToMapScreen: () -> Unit,
    navigateToSectionSelectionScreen: () -> Unit,
    navigateToSavedEstimationsScreen: () -> Unit
) {
    val locationPermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { isGranted ->
            if (isGranted[Manifest.permission.ACCESS_COARSE_LOCATION] == true && isGranted[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                navigateToMapScreen()
            }
        })
    Box(
        contentAlignment = Alignment.Center, modifier = Modifier
            .fillMaxSize()
            .background(
                brushList1.gradientBackground()
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CreateNewDocumentButton(
                context = context,
                onClick = { navigateToSectionSelectionScreen() })
            ShowSavedDocumentsButton(
                context = context,
                onClick = { navigateToSavedEstimationsScreen() })
            ShowMapButton(
                context = context,
                onClick = {
                    locationPermissionResultLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    )
                }
            )
        }
    }
}

@Composable
private fun CreateNewDocumentButton(context: Context, onClick: () -> Unit) {
    OutlinedButton(onClick = onClick) {
        Text(text = "Utwórz nowy dokument", color = Color.Black)
    }
}

@Composable
private fun ShowSavedDocumentsButton(context: Context, onClick: () -> Unit) {
    OutlinedButton(onClick = onClick) {
        Text(text = "Zobacz Zapisane", color = Color.Black)
    }
}

@Composable
private fun ShowMapButton(context: Context, onClick: () -> Unit) {
    OutlinedButton(onClick = onClick) {
        Text(text = "Pokaż mapę", color = Color.Black)
    }
}

private fun DestinationsNavigator.navigateToMapScreen() {
    navigate(MapScreenDestination)
}

private fun DestinationsNavigator.navigateToSectionSelectionScreen() {
    navigate(SectionSelectionScreenDestination)

}

private fun DestinationsNavigator.navigateToSavedEstimationsScreen() {
    navigate(SavedEstimationsScreenDestination)
}