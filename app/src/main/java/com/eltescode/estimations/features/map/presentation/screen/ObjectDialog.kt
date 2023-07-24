package com.eltescode.estimations.features.map.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.eltescode.estimations.R
import com.eltescode.estimations.features.map.presentation.model.GeoNoteDisplayable
import com.eltescode.estimations.ui.theme.color2
import org.osmdroid.util.GeoPoint
import java.util.*

@Composable
fun ObjectDialog(
    geoNoteToUpdate: GeoNoteDisplayable? = null,
    latitude: Double,
    longitude: Double,
    updateGeoNote: (GeoNoteDisplayable) -> Unit,
    saveGeoNoteToLocal: (GeoNoteDisplayable) -> Unit,
    updateLocationToZoom: (GeoPoint) -> Unit,
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
            title = title,
            description = description,
            section = section,
            promptTitle = stringResource(id = R.string.hint28),
            promptDescription = stringResource(id = R.string.hint9),
            promptSection = stringResource(id = R.string.hint29),
            buttonText = stringResource(id = R.string.hint30),
            updateGeoNote = updateGeoNote,
            onDismissRequest = onDismiss
        )
    } else {
        DialogToSaveObject(
            latitude = latitude,
            longitude = longitude,
            title = title,
            description = description,
            section = section,
            promptTitle = stringResource(id = R.string.hint28),
            promptDescription = stringResource(id = R.string.hint9),
            promptSection = stringResource(id = R.string.hint29),
            buttonText = stringResource(id = R.string.hint31),
            saveGeoNoteToLocal = saveGeoNoteToLocal,
            updateLocationToZoom = updateLocationToZoom,
            onDismissRequest = onDismiss
        )
    }
}

@Composable
fun DialogToSaveObject(
    latitude: Double,
    longitude: Double,
    title: MutableState<String>,
    description: MutableState<String>,
    section: MutableState<String>,
    promptTitle: String,
    promptSection: String,
    promptDescription: String,
    buttonText: String,
    saveGeoNoteToLocal: (GeoNoteDisplayable) -> Unit,
    updateLocationToZoom: (GeoPoint) -> Unit,
    onDismissRequest: (Double, Double) -> Unit
) {
    BaseObjectView(
        latitude = latitude,
        longitude = longitude,
        title = title,
        description = description,
        section = section,
        promptTitle = promptTitle,
        promptSection = promptSection,
        promptDescription = promptDescription,
        buttonText = buttonText,
        onDismissRequest = onDismissRequest,
        onConfirmButtonClick = {
            saveGeoNoteToLocal(
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
            updateLocationToZoom(GeoPoint(latitude, longitude))
            onDismissRequest(latitude, longitude)
        }
    )
}

@Composable
fun DialogToUpdateObject(
    geoNoteToUpdate: GeoNoteDisplayable,
    title: MutableState<String>,
    description: MutableState<String>,
    section: MutableState<String>,
    promptTitle: String,
    promptSection: String,
    promptDescription: String,
    buttonText: String,
    updateGeoNote: (GeoNoteDisplayable) -> Unit,
    onDismissRequest: (Double, Double) -> Unit,
) {
    BaseObjectView(
        latitude = geoNoteToUpdate.latitude,
        longitude = geoNoteToUpdate.longitude,
        title = title,
        description = description,
        section = section,
        promptTitle = promptTitle,
        promptSection = promptSection,
        promptDescription = promptDescription,
        buttonText = buttonText,
        onDismissRequest = onDismissRequest,
        onConfirmButtonClick = {
            updateGeoNote(
                geoNoteToUpdate.copy(
                    section = section.value,
                    title = title.value,
                    note = description.value
                )
            )
            onDismissRequest(geoNoteToUpdate.latitude, geoNoteToUpdate.longitude)
        }
    )
}

@Composable
private fun BaseObjectView(
    latitude: Double,
    longitude: Double,
    title: MutableState<String>,
    description: MutableState<String>,
    section: MutableState<String>,
    promptTitle: String,
    promptSection: String,
    promptDescription: String,
    buttonText: String,
    onDismissRequest: (Double, Double) -> Unit,
    onConfirmButtonClick: () -> Unit
) {
    Dialog(onDismissRequest = {
        onDismissRequest(latitude, longitude)
    }) {
        Card {
            Box(contentAlignment = Alignment.Center) {
                CancelButton(
                    latitude = latitude,
                    longitude = longitude,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 5.dp, end = 5.dp)
                        .size(30.dp),
                    onDismiss = onDismissRequest
                )

                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Header(
                        latitude = latitude.toFloat(),
                        longitude = longitude.toFloat()
                    )
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TitleTextField(
                            title = title,
                            promptTitle = promptTitle,
                            onValueChange = { title.value = it },
                            modifier = Modifier
                                .height(80.dp)
                                .padding(2.dp)
                                .weight(0.5f)
                        )
                        SectionTextField(
                            section = section,
                            promptSection = promptSection,
                            onValueChange = { section.value = it },
                            modifier = Modifier
                                .height(80.dp)
                                .padding(2.dp)
                                .weight(0.5f),
                        )
                    }
                    DescriptionTextField(
                        description = description,
                        promptDescription = promptDescription,
                        onValueChange = { description.value = it },
                        modifier = Modifier
                            .width(300.dp)
                            .height(150.dp)
                    )
                    ConfirmButton(buttonText = buttonText, onClick = onConfirmButtonClick)
                }
            }
        }
    }
}

@Composable
private fun Header(latitude: Float, longitude: Float) {
    Text(
        text = stringResource(id = R.string.hint32)
                + " "
                + "$latitude, $longitude",
        style = MaterialTheme.typography.h6,
        modifier = Modifier.padding(5.dp)
    )
}

@Composable
private fun TitleTextField(
    title: MutableState<String>,
    promptTitle: String,
    modifier: Modifier,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = title.value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = { Text(text = promptTitle) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = color2, focusedLabelColor = color2
        ),
        singleLine = true
    )
}

@Composable
private fun SectionTextField(
    section: MutableState<String>,
    promptSection: String,
    modifier: Modifier,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = section.value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = { Text(text = promptSection) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = color2, focusedLabelColor = color2
        ),
        singleLine = true
    )
}

@Composable
private fun DescriptionTextField(
    description: MutableState<String>,
    promptDescription: String,
    modifier: Modifier,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = description.value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = { Text(text = promptDescription) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = color2, focusedLabelColor = color2
        )
    )
}

@Composable
private fun ConfirmButton(buttonText: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.Black,
            disabledContentColor = Color.Gray,
            backgroundColor = Color.White,
            disabledBackgroundColor = Color.LightGray
        )
    ) {
        Text(text = buttonText)
    }
}

@Composable
private fun CancelButton(
    latitude: Double,
    longitude: Double,
    modifier: Modifier,
    onDismiss: (Double, Double) -> Unit
) {
    Icon(
        imageVector = ImageVector.vectorResource(id = R.drawable.ic_cancel),
        contentDescription = null,
        modifier = modifier
            .clip(CircleShape)
            .clickable { onDismiss(latitude, longitude) }
    )
}