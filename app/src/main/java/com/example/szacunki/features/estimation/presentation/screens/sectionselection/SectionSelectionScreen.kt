package com.example.szacunki.features.estimation.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.szacunki.core.calculations.color1
import com.example.szacunki.features.estimation.presentation.screens.destinations.TreeSelectionScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Destination(route = "SectionSelectionScreen")
@Composable
fun SectionSelectionScreen(navigator: DestinationsNavigator) {
    val text = remember {
        mutableStateOf("")
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = color1), contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier, contentAlignment = Alignment.Center) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(value = text.value, onValueChange = {
                    text.value = it
                }, label = { Text(text = "Wpisz numer oddziału") })
                OutlinedButton(onClick = {
                    val navArg =
                        TreeSelectionScreenDestination.NavArgs(text.value)
                    navigator.popBackStack()
                    navigator.navigate(TreeSelectionScreenDestination(navArg))
                }) {
                    Text(text = text.value)
                }
            }
        }
    }
}
