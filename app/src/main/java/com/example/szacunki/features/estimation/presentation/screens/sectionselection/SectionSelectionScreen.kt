package com.example.szacunki.features.estimation.presentation.screens.sectionselection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.szacunki.R
import com.example.szacunki.core.extensions.gradientBackground
import com.example.szacunki.destinations.TreeSelectionScreenDestination
import com.example.szacunki.ui.theme.brushList1
import com.example.szacunki.ui.theme.color2
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun SectionSelectionScreen(navigator: DestinationsNavigator) {
    val text = rememberSaveable { mutableStateOf("") }
    SectionSelectionScreen(
        text = text,
        navigateToTreeSelectionScreen = navigator::navigateToTreeSelectionScreen,
    )
}

@Composable
fun SectionSelectionScreen(
    text: MutableState<String>,
    navigateToTreeSelectionScreen: (String) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brushList1.gradientBackground()),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 100.dp)
        ) {
            OutlinedTextField(
                value = text.value,
                onValueChange = { text.value = it },
                label = { Text(text = stringResource(id = R.string.hint22)) },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = color2,
                    focusedLabelColor = color2
                ),
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = {
                    navigateToTreeSelectionScreen(text.value)
                }),
                modifier = Modifier.padding(bottom = 20.dp)
            )
            OutlinedButton(
                onClick = { navigateToTreeSelectionScreen(text.value) },
                enabled = text.value.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.Black,
                    disabledContentColor = Color.Gray,
                    backgroundColor = Color.White,
                    disabledBackgroundColor = Color.LightGray
                )
            ) {
                Text(
                    text = stringResource(id = R.string.hint23), textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h5,
                )
            }
        }
    }
}

private fun DestinationsNavigator.navigateToTreeSelectionScreen(text: String) {
    popBackStack().also { navigate(TreeSelectionScreenDestination(text)) }
}

