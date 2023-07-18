package com.example.szacunki.features.map.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.szacunki.features.map.presentation.model.GeoNoteDisplayable
import com.example.szacunki.ui.theme.color2
import org.osmdroid.util.GeoPoint
import java.util.*

@Composable
fun ObjectDialog(
    geoNoteToUpdate: GeoNoteDisplayable? = null,
    latitude: Double,
    longitude: Double,
    viewModel: MapViewModel,
    onDismiss: (Double, Double) -> Unit
) {
    val title = rememberSaveable { mutableStateOf("") }
    val description = rememberSaveable { mutableStateOf("") }
    val section = rememberSaveable { mutableStateOf("") }

    if (geoNoteToUpdate != null) {

        LaunchedEffect(key1 = Unit, block = {
            title.value = title.value.ifBlank {
                geoNoteToUpdate.title
            }
            description.value = description.value.ifBlank { geoNoteToUpdate.note }
            section.value = section.value.ifBlank { geoNoteToUpdate.section }
        })
        DialogToUpdateObject(
            geoNoteToUpdate = geoNoteToUpdate,
            viewModel = viewModel,
            title = title,
            description = description,
            section = section,
            promptTitle = "Wpisz Tytuł",
            promptDescription = "Wpisz Notatkę",
            promptSection = "Wpisz Oddział",
            buttonText = "Aktualizuj Obiekt",
            onDismiss = onDismiss
        )
    } else {
        DialogToSaveObject(
            latitude = latitude,
            longitude = longitude,
            viewModel = viewModel,
            title = title,
            description = description,
            section = section,
            promptTitle = "Wpisz Tytuł",
            promptDescription = "Wpisz Notatkę",
            promptSection = "Wpisz Oddział",
            buttonText = "Dodaj Obiekt",
            onDismiss = onDismiss
        )
    }

}

@Composable
fun DialogToUpdateObject(
    geoNoteToUpdate: GeoNoteDisplayable,
    viewModel: MapViewModel,
    title: MutableState<String>,
    description: MutableState<String>,
    section: MutableState<String>,
    promptTitle: String,
    promptSection: String,
    promptDescription: String,
    buttonText: String,
    onDismiss: (Double, Double) -> Unit,

    ) {
    Dialog(onDismissRequest = { onDismiss(geoNoteToUpdate.latitude, geoNoteToUpdate.longitude) }) {
        Card {
            Box(contentAlignment = Alignment.Center) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Loc: ${geoNoteToUpdate.latitude}, ${geoNoteToUpdate.longitude}",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(5.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = title.value,
                            onValueChange = { title.value = it },
                            modifier = Modifier
                                .height(80.dp)
                                .padding(2.dp)
                                .weight(0.5f),
                            label = { Text(text = promptTitle) },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = color2,
                                focusedLabelColor = color2
                            )
                        )
                        OutlinedTextField(
                            value = section.value,
                            onValueChange = { section.value = it },
                            modifier = Modifier
                                .height(80.dp)
                                .padding(2.dp)
                                .weight(0.5f),
                            label = { Text(text = promptSection) },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = color2,
                                focusedLabelColor = color2
                            )
                        )
                    }
                    OutlinedTextField(
                        value = description.value,
                        onValueChange = { description.value = it },
                        modifier = Modifier.size(300.dp),
                        label = { Text(text = promptDescription) },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = color2,
                            focusedLabelColor = color2
                        )
                    )
                    OutlinedButton(onClick = {
                        viewModel.updateGeoNote(
                            geoNote = geoNoteToUpdate.copy(
                                section = section.value,
                                title = title.value,
                                note = description.value
                            )
                        )
                        onDismiss(geoNoteToUpdate.latitude, geoNoteToUpdate.longitude)
                    }) {
                        Text(text = buttonText)
                    }
                }
            }

        }
    }

}

@Composable
fun DialogToSaveObject(
    latitude: Double,
    longitude: Double,
    viewModel: MapViewModel,
    title: MutableState<String>,
    description: MutableState<String>,
    section: MutableState<String>,
    promptTitle: String,
    promptSection: String,
    promptDescription: String,
    buttonText: String,
    onDismiss: (Double, Double) -> Unit
) {
    Dialog(onDismissRequest = { onDismiss(latitude, longitude) }) {
        Card {
            Box(contentAlignment = Alignment.Center) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Loc: $latitude, $longitude",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(5.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = title.value,
                            onValueChange = { title.value = it },
                            modifier = Modifier
                                .height(80.dp)
                                .padding(2.dp)
                                .weight(0.5f),
                            label = { Text(text = promptTitle) },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = color2,
                                focusedLabelColor = color2
                            )
                        )
                        OutlinedTextField(
                            value = section.value,
                            onValueChange = { section.value = it },
                            modifier = Modifier
                                .height(80.dp)
                                .padding(2.dp)
                                .weight(0.5f),
                            label = { Text(text = promptSection) },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = color2,
                                focusedLabelColor = color2
                            )
                        )
                    }
                    OutlinedTextField(
                        value = description.value,
                        onValueChange = { description.value = it },
                        modifier = Modifier.size(300.dp),
                        label = { Text(text = promptDescription) },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = color2,
                            focusedLabelColor = color2
                        )
                    )
                    OutlinedButton(
                        onClick = {
                            viewModel.saveGeoNoteToLocal(
                                GeoNoteDisplayable(
                                    id = UUID.randomUUID(),
                                    section = section.value,
                                    title = title.value,
                                    note = description.value,
                                    date = Date(),
                                    latitude = latitude,
                                    longitude = longitude
                                )
                            )
                            viewModel.updateLocationToZoom(GeoPoint(latitude, longitude))
                            onDismiss(latitude, longitude)
                        },
                        enabled = (latitude != 0.0 && longitude != 0.0)
                    ) {
                        Text(text = buttonText)
                    }
                }
            }

        }
    }
}
