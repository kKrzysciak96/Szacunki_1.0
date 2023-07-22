package com.eltescode.estimations.core.screen.general

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
import androidx.compose.ui.res.stringResource
import com.eltescode.estimations.R
import com.eltescode.estimations.core.extensions.gradientBackground
import com.eltescode.estimations.destinations.MapScreenDestination
import com.eltescode.estimations.destinations.SavedEstimationsScreenDestination
import com.eltescode.estimations.destinations.SectionSelectionScreenDestination
import com.eltescode.estimations.ui.theme.brushList1
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
                onClick = { navigateToSectionSelectionScreen() })
            ShowSavedDocumentsButton(
                onClick = { navigateToSavedEstimationsScreen() })
            ShowMapButton(
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
private fun CreateNewDocumentButton( onClick: () -> Unit) {
    OutlinedButton(onClick = onClick) {
        Text(text = stringResource(id = R.string.hint45), color = Color.Black)
    }
}

@Composable
private fun ShowSavedDocumentsButton( onClick: () -> Unit) {
    OutlinedButton(onClick = onClick) {
        Text(text = stringResource(id = R.string.hint46), color = Color.Black)
    }
}

@Composable
private fun ShowMapButton( onClick: () -> Unit) {
    OutlinedButton(onClick = onClick) {
        Text(text = stringResource(id = R.string.hint47), color = Color.Black)
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