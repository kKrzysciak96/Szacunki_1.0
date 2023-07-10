package com.example.szacunki.features.estimation.presentation.screens.sectionselection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.szacunki.core.calculations.color1
import com.example.szacunki.destinations.TreeSelectionScreenDestination
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
            .background(color = color1), contentAlignment = Alignment.TopCenter
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 100.dp)
        ) {
            OutlinedTextField(
                value = text.value,
                onValueChange = { text.value = it },
                label = { Text(text = "Wpisz numer oddziału") },
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = {
                    onConfirmation(
                        text.value,
                        navigator
                    )
                }),
                modifier = Modifier.padding(bottom = 20.dp)
            )
            OutlinedButton(
                onClick = { onConfirmation(text.value, navigator) },
                enabled = text.value.isNotBlank(),
            ) {
                Text(text = "Potwierdź")
            }
        }

    }
}

fun onConfirmation(text: String, navigator: DestinationsNavigator) {
    val navArg =
        TreeSelectionScreenDestination.NavArgs(text)
    navigator.popBackStack()
    navigator.navigate(TreeSelectionScreenDestination(navArg))
}
