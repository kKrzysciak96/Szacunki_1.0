package com.eltescode.estimations.core.screen.general

import android.Manifest
import android.content.res.Configuration
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.eltescode.estimations.BuildConfig
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
    GeneralScreen(
        navigateToMapScreen = navigator::navigateToMapScreen,
        navigateToSectionSelectionScreen = navigator::navigateToSectionSelectionScreen,
        navigateToSavedEstimationsScreen = navigator::navigateToSavedEstimationsScreen
    )
}

@Composable
private fun GeneralScreen(
    navigateToMapScreen: () -> Unit,
    navigateToSectionSelectionScreen: () -> Unit,
    navigateToSavedEstimationsScreen: () -> Unit
) {
    val configuration = LocalConfiguration.current


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
        AppNameHeaderIcon(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(
                    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        160.dp
                    } else {
                        320.dp
                    }
                )
        )
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
        Text(
            text = "ver." + BuildConfig.VERSION_NAME,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(5.dp),
            style = MaterialTheme.typography.subtitle2
        )
    }
}

@Composable
fun AppNameHeaderIcon(modifier: Modifier) {
    Icon(
        imageVector = ImageVector.vectorResource(id = R.drawable.ic_main_text),
        contentDescription = null,
        modifier = modifier
            .padding(10.dp)
    )
}

@Composable
private fun CreateNewDocumentButton(onClick: () -> Unit) {
    OutlinedButton(onClick = onClick) {
        Text(text = stringResource(id = R.string.hint45), color = Color.Black)
    }
}

@Composable
private fun ShowSavedDocumentsButton(onClick: () -> Unit) {
    OutlinedButton(onClick = onClick) {
        Text(text = stringResource(id = R.string.hint46), color = Color.Black)
    }
}

@Composable
private fun ShowMapButton(onClick: () -> Unit) {
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